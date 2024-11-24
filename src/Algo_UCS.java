import java.util.*;

public class Algo_UCS implements Algorithm{
    @Override
    public List<String> run(GameState gameState, View view) {
        Set<GameState> vis = new HashSet<>();
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();

        PriorityQueue<Pair<Integer, GameState>> q = new PriorityQueue<>();

        List<String> path = new LinkedList<>();


        parent.put(gameState, new Pair<>("stop", new GameState()));
        vis.add(gameState);
        q.add(new Pair<>(1 , gameState));

        int Level = 0 ;
        boolean solved = false ;
        while(!q.isEmpty() && !solved)
        {
            Level++;
//            System.out.println("Boards created so far : " + GameState.numberOfCreatedObjects + " Current Level : " + Level );
            int sz = q.size() ;
            while(sz > 0 && !solved){
                sz--;
                Pair<Integer, GameState> temp = q.poll();

                GameState current_state = temp.second;
                int cost = temp.first;
                List<Pair<String, GameState>> states = current_state.nextStates();
                for (Pair<String, GameState> nextStatesPair : states) {
                    GameState nextState = nextStatesPair.second;
                    String move = nextStatesPair.first;
                    if (!vis.contains(nextState)) {
                        vis.add(nextState);
                        q.add(new Pair<>(1 , nextState));
                        parent.put(nextState, new Pair<>(move, current_state));

                        if (nextState.check_winning()) {
                            System.out.println("I've found I solution !!!!!!!!");
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
        System.out.println("Number of tried grids to find the solution: " + vis.size());
        return path ;
    }
}
