import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Algo_A_Star implements Algorithm{
    @Override
    public List<String> run(GameState gameState) {


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
                    if (!cost.containsKey(nextState) || cost.get(nextState) > w + 1 + ManhattanDistanceAll(nextState)) {
                        cost.put(nextState, w + 1);
                        q.add(new Pair<>(w + 1 , nextState));
                        parent.put(nextState, new Pair<>(move, current_state));


                    }

                }
            }
        }
        while(!parent.get(gameState).first.equals("stop")){
            path.add(parent.get(gameState).first);
            gameState = parent.get(gameState).second;
        }

        Collections.reverse(path);
        System.out.println("Number of tried grids to find the solution: " + cost.size());
        return path ;
    }
    private int ManhattanDistance(int x1 , int y1 , int x2, int y2){
            return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    private int ManhattanDistanceAll(GameState nextState) {
        HashMap<String, Pair<Integer , Integer>> pos = new HashMap<>();

        nextState.coloredCells.forEach(elm -> {
            int x = elm.first ;
            int y = elm.second;


            if(!nextState.get_current_board_shallow()[x][y].getCellColor().equals("W"))
                pos.put(nextState.get_current_board_shallow()[x][y].getCellColor().toUpperCase() , new Pair<>(x , y));
        });
        AtomicInteger cost = new AtomicInteger();
        nextState.destinationCells.forEach(elm ->{
            int x = elm.first ;
            int y = elm.first ;

            if(pos.containsKey(nextState.get_current_board_shallow()[x][y].getDestinationColor().toUpperCase())){
                int x2 = pos.get(nextState.get_current_board_shallow()[x][y].getDestinationColor().toUpperCase()).first;
                int y2 = pos.get(nextState.get_current_board_shallow()[x][y].getDestinationColor().toUpperCase()).second;
                cost.set(ManhattanDistance(x, y, x2, y2));
            }

        });

        return cost.get();
    }

}
