import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class Algo_Hill_Climbing_Simple implements Algorithm{
    @Override
    public List<String> run(GameState gameState, BufferedWriter logWriter) {
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();
        HashSet<GameState> vis = new HashSet<>();

        parent.put(gameState, new Pair<>("stop", new GameState()));
        //vis.add(gameState);
        double current = Heuristic.get(gameState) ;
        while(true)
        {
            List<Pair<String, GameState>> states = gameState.nextStates();
            GameState child = null ;
            boolean find = false ;
            for (Pair<String, GameState> nextStatesPair : states) {
                GameState nextState = nextStatesPair.second;
                String move = nextStatesPair.first;
                double cost = Heuristic.get(nextState) ;
                if (cost < current) {
                    parent.put(nextState , new Pair<>(move, gameState));
                    gameState = new GameState(nextState);
                    current = cost ;
                    find = true ;
                    break;
                }
            }
            if(!find) break;
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
