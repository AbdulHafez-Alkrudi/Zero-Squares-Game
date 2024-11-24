import java.util.*;

public class Algo_DFS implements Algorithm{
    @Override
    public List<String> run(GameState gameState , View view) {
        HashSet<GameState> vis = new HashSet<>();
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();
        Stack<GameState> stk = new Stack<>();
        List<String> path = new LinkedList<>();


        parent.put(gameState, new Pair<>("stop", new GameState()));
        vis.add(new GameState(gameState));
        stk.add(new GameState(gameState));

        while(!stk.isEmpty())
        {
            GameState current_state = stk.pop();
            if(current_state.check_winning()){
                gameState = current_state;
                break;
            }
            List<Pair<String, GameState>> states = current_state.nextStates();
            states.forEach(nextStatesPair -> {
                GameState nextState = nextStatesPair.second;
                String move = nextStatesPair.first ;
                if(!vis.contains(nextState)){
                    vis.add(nextState);
                    stk.add(nextState);
                    parent.put(nextState, new Pair<>(move, current_state));
                }
            } );
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
