import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class Algo_DFS_Recursive implements Algorithm{
    HashSet<GameState> vis = new HashSet<>();
    Map<GameState, Pair<String, GameState>> parent = new HashMap<>();
    Stack<GameState> stk = new Stack<>();
    List<String> path = new LinkedList<>();
    GameState lastState = new GameState();
    boolean stop = false ;


    @Override
    public List<String> run(GameState gameState, BufferedWriter logWriter) {
        this.vis = new HashSet<>();
        this.parent = new HashMap<>();
        this.stk = new Stack<>();
        this.path = new LinkedList<>();
        this.lastState = new GameState();
        this.stop = false ;

        parent.put(gameState, new Pair<>("stop", new GameState()));
        vis.add(new GameState(gameState));
        dfs(gameState);

        try {
            logWriter.write("Number of tried grids to find the solution: " + vis.size()+ "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(!parent.get(lastState).first.equals("stop")){
            path.add(parent.get(lastState).first);
            lastState = parent.get(lastState).second;
        }

        Collections.reverse(path);
        System.out.println("Number of tried grids to find the solution: " + vis.size() );
        return path ;
    }
    public void dfs(GameState gameState)
    {
        if(stop) return ;
        if(gameState.check_winning()){
            stop = true ;
            lastState = gameState ;
            return ;
        }
        List<Pair<String, GameState>> states = gameState.nextStates();
        for (Pair<String, GameState> nextStatesPair : states) {
            GameState nextState = nextStatesPair.second;
            String move = nextStatesPair.first;
            if (!vis.contains(nextState)) {
                vis.add(nextState);
                parent.put(nextState, new Pair<>(move, gameState));
                dfs(nextState);
            }
        }
    }
}
