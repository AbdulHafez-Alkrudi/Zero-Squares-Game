import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class Algo_Hill_Climbing_Steepest implements Algorithm{
    @Override
    public List<String> run(GameState gameState, BufferedWriter logWriter) {
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();

        parent.put(gameState, new Pair<>("stop", new GameState()));
        double current = Heuristic.get(gameState) ;
        while(true)
        {
            List<Pair<String, GameState>> states = gameState.nextStates();
            GameState child = null ;
            double mn = Double.MAX_VALUE ;
            String Move = null;
            for (Pair<String, GameState> nextStatesPair : states) {
                GameState nextState = nextStatesPair.second;
                String move = nextStatesPair.first;
                double cost = Heuristic.get(nextState) ;
                if (cost < mn) {
                    mn = cost ;
                    Move = move;
                    child = nextState ;
                }
            }
            if(mn >= current){
                break;
            }
            assert child != null;
            parent.put(child , new Pair<>(Move, gameState));
            gameState = new GameState(child);
            current = mn ;
        }
        List<String> path = new LinkedList<>();

        while(!parent.get(gameState).first.equals("stop")){
            path.add(parent.get(gameState).first);
            gameState = parent.get(gameState).second;
        }
        try {
            logWriter.write("Number of tried grids to find the solution: " + (path.size() + 1)+ "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Collections.reverse(path);
        System.out.println("Number of tried grids to find the solution: " + (path.size() + 1));
        return path ;

    }
}
