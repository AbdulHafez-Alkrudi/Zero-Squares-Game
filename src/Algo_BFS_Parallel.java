import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Algo_BFS_Parallel implements Algorithm {

    @Override
    public List<String> run(GameState gameState, BufferedWriter logWriter) {
        final Set<GameState> vis = ConcurrentHashMap.newKeySet();
        final Map<GameState, Pair<String, GameState>> parent = new ConcurrentHashMap<>();
        final Queue<GameState> q = new ConcurrentLinkedQueue<>();
        final List<String> path = new LinkedList<>();

        parent.put(gameState, new Pair<>("stop", null));
        q.add(gameState);
        vis.add(gameState);

        final AtomicBoolean found = new AtomicBoolean(false);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int level = 0 ;
        while (!q.isEmpty() && !found.get()) {
            int levelSize = q.size();
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                GameState current_state = q.poll();
                final GameState cs = current_state; // Make 'cs' final for the lambda
                Future<?> future = executor.submit(() -> {
                    if (cs.check_winning()) {
                        synchronized (this) {
                            if (!found.get()) { // Double-checked locking
                                found.set(true);
                                synchronized (path) {
                                    reconstructPath(parent, cs, path);
                                }
                            }
                        }
                        return;
                    }

                    List<Pair<String, GameState>> states = cs.nextStates();
                    for (Pair<String, GameState> nextStatesPair : states) {
                        GameState nextState = nextStatesPair.second;
                        String move = nextStatesPair.first;
                        if (vis.add(nextState)) { // Thread-safe add operation
                            q.add(nextState);
                            parent.put(nextState, new Pair<>(move, cs));
                        }
                    }
                });
                futures.add(future);
            }

            // Wait for all tasks to complete
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                    return path;
                }
            }
        }
        try {
            logWriter.write("Number of tried grids to find the solution: " + vis.size() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();

        if (!path.isEmpty()) {
            Collections.reverse(path);
        }

        return path;
    }

    private void reconstructPath(Map<GameState, Pair<String, GameState>> parent, GameState gameState, List<String> path) {
        while (parent.get(gameState) != null && !parent.get(gameState).first.equals("stop")) {
            path.add(parent.get(gameState).first);
            gameState = parent.get(gameState).second;
        }
    }
}
