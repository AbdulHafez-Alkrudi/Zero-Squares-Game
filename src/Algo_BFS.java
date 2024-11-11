import java.util.*;

public class Algo_BFS implements Algorithm{

    @Override
    public List<String> run(GameState gameState) {
        Map<GameState, Boolean> vis = new HashMap<>();
        Map<GameState, Pair<String , GameState>> parent = new HashMap<>();

        Queue<GameState> q = new LinkedList<>();
        List<String> path = new LinkedList<>();

        q.add(gameState);

        while(!q.isEmpty())
        {
            GameState current_state = q.poll();

//            List<> nextStates = current_state.
        }

        return path ;
    }
}
