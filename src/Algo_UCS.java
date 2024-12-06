import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class Algo_UCS implements Algorithm{
    @Override
    public List<String> run(GameState gameState, BufferedWriter logWriter) {


        Map<GameState, Integer> cost = new HashMap<>();
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();

        PriorityQueue<Pair<Integer, GameState>> q = new PriorityQueue<>();

        List<String> path = new LinkedList<>();


        parent.put(gameState, new Pair<>("stop", new GameState()));
        cost.put(gameState, 0);
        q.add(new Pair<>(0 , gameState));

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
                int w = temp.first;
                if(cost.get(current_state) < w) continue ;
                if (current_state.check_winning()) {
                    System.out.println("I've found I solution !!!!!!!!");
                    gameState = current_state;
                    solved = true;
                    break;
                }
                List<Pair<String, GameState>> states = current_state.nextStates();
                for (Pair<String, GameState> nextStatesPair : states) {
                    GameState nextState = nextStatesPair.second;
                    String move = nextStatesPair.first;
                    if (!cost.containsKey(nextState) || cost.get(nextState) > w + nextState.cost) {
                        cost.put(nextState, w + nextState.cost);
                        q.add(new Pair<>(w + nextState.cost , nextState));
                        parent.put(nextState, new Pair<>(move, current_state));
                    }
                }
            }
        }
        try {
            logWriter.write("Number of tried grids to find the solution: " + cost.size()+ "\n");
            logWriter.write("Total Cost: " + cost.get(gameState)+ "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(!parent.get(gameState).first.equals("stop")){
            path.add(parent.get(gameState).first);
            gameState = parent.get(gameState).second;
        }

        Collections.reverse(path);
        System.out.println("Number of tried grids to find the solution: " + cost.size());

        return path ;
    }

}
