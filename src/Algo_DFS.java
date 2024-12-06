import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class Algo_DFS implements Algorithm{
    @Override
    public List<String> run(GameState gameState, BufferedWriter logWriter) {
        HashSet<GameState> vis = new HashSet<>(1_000_000);
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>(1_000_000);
        Deque<GameState> stack = new ArrayDeque<>();

        List<String> path = new LinkedList<>();

        vis.add(new GameState(gameState));
        stack.addFirst(new GameState(gameState));
        parent.put(new GameState(gameState), new Pair<>("stop", null));
        while(!stack.isEmpty())
        {
            GameState current_state = stack.removeFirst();
            if(current_state.check_winning()){
                gameState = current_state;
                break;
            }
            List<Pair<String, GameState>> states = current_state.nextStates();
            for (Pair<String, GameState> nextStatesPair : states) {
                GameState nextState = nextStatesPair.second;
                String move = nextStatesPair.first;
                if (!vis.contains(nextState)) {
                    vis.add(nextState);
                    stack.addFirst(nextState);
                    parent.put(nextState, new Pair<>(move, current_state));
                }
            }
        }
        try {
            logWriter.write("Number of tried grids to find the solution: " + vis.size() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(!parent.get(gameState).first.equals("stop")){
            path.add(parent.get(gameState).first);
            gameState = parent.get(gameState).second;
        }

        Collections.reverse(path);
        System.out.println("Number of tried grids to find the solution: " + vis.size());
        return path ;
    }
}
