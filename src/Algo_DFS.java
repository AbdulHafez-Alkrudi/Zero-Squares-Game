import java.util.*;

public class Algo_DFS implements Algorithm{
    @Override
    public List<String> run(GameState gameState , View view) {
        Map<GameState, Boolean> vis = new HashMap<>();
        Map<GameState, Pair<String, GameState>> parent = new HashMap<>();
        Stack<GameState> stk = new Stack<>();
        List<String> path = new LinkedList<>();


        parent.put(gameState, new Pair<>("stop", new GameState()));
        vis.put(new GameState(gameState), true);
        stk.add(new GameState(gameState));
        int cnt = 0 ;
        while(!stk.isEmpty())
        {
            cnt++;
            System.out.println(cnt);
            GameState current_state = stk.pop();
            if(current_state.check_winning()){
                gameState = current_state;
                break;
            }
            // view.display(current_state.get_current_board_shallow(), current_state.get_size());

            List<Pair<String, GameState>> states = current_state.nextStates();
            System.out.println(states.size() + " " + stk.size());
            states.forEach(nextStatesPair -> {
                // view.display(current_state.get_current_board_shallow(), current_state.get_size());
                GameState nextState = nextStatesPair.second;
                String move = nextStatesPair.first ;
                if(!vis.containsKey(nextState)){
                    vis.put(nextState, true);

                    stk.add(nextState);
                    parent.put(nextState, new Pair<>(move, current_state));
                }
            } );
        }
        while(!parent.get(gameState).first.equals("stop")){
            path.add(parent.get(gameState).first);
            gameState = parent.get(gameState).second;
        }

        return reverse(path) ;

    }
    private List<String> reverse(List<String> lst){
        Stack<String> stk = new Stack<>();
        stk.addAll(lst);
        List<String> new_lst = new LinkedList<>();
        while(!stk.isEmpty()){
            new_lst.add(stk.pop());
        }
        return new_lst ;
    }
}
