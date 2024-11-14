import java.util.*;

public class Algo_BFS implements Algorithm{

    @Override
    public List<String> run(GameState gameState , View view) {
        Map<GameState, Boolean> vis = new HashMap<>();
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();
        Queue<GameState> q = new LinkedList<>();
        List<String> path = new LinkedList<>();


        parent.put(new GameState(gameState), new Pair<>("stop", new GameState()));
        vis.put(new GameState(gameState), true);
        q.add(new GameState(gameState));
        int Level = 0 ;
        boolean solved = false ;
        while(!q.isEmpty() && !solved)
        {
            Level++;
            int sz = q.size() ;
            while(sz > 0 && !solved){
                sz--;
                GameState current_state = q.poll();
                List<Pair<String, GameState>> states = current_state.nextStates();
                for (Pair<String, GameState> nextStatesPair : states) {
                    GameState nextState = nextStatesPair.second;
                    String move = nextStatesPair.first;
                    if (!vis.containsKey(nextState)) {
                        vis.put(nextState, true);
                        q.add(nextState);
                        parent.put(nextState, new Pair<>(move, current_state));

                        if (nextState.check_winning()) {
                            gameState = nextState;
                            solved = true;
                            break;
                        }
                    }
                }
            }
        }
        while(!parent.get(gameState).first.equals("stop")){
            path.add(parent.get(gameState).first);
            gameState = parent.get(gameState).second;
        }

        Collections.reverse(path);
        return path ;
    }

}
