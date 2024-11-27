import java.util.*;

public class Algo_BFS implements Algorithm{

    @Override
    public List<String> run(GameState gameState) {
        Set<GameState> vis = new HashSet<>();
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();
        Deque<GameState> q = new ArrayDeque<>();
        List<String> path = new LinkedList<>();


        parent.put(gameState, new Pair<>("stop", new GameState()));
        vis.add(gameState);
        q.add(gameState);
        int Level = 0 ;
        boolean solved = false ;
        while(!q.isEmpty() && !solved)
        {
/*            Level++;
//            System.out.println("Boards created so far : " + GameState.numberOfCreatedObjects + " Current Level : " + Level );
            int sz = q.size() ;
            while(sz > 0 && !solved){*/
//                sz--;
                GameState current_state = q.poll();
                List<Pair<String, GameState>> states = current_state.nextStates();
                for (Pair<String, GameState> nextStatesPair : states) {
                    GameState nextState = nextStatesPair.second;
                    String move = nextStatesPair.first;
                    if (!vis.contains(nextState)) {
                        vis.add(nextState);
                        q.add(nextState);
                        parent.put(nextState, new Pair<>(move, current_state));

                        if (nextState.check_winning()) {
                            System.out.println("I've found I solution !!!!!!!!");
                            gameState = nextState;
                            solved = true;
                            break;
                        }
                    }
                }
//            }
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
