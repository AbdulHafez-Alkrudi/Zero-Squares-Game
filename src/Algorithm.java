import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.List;

public interface Algorithm {


    abstract public List<String> run(GameState gameState, BufferedWriter logWriter);
    public static int ManhattanDistance(int x1 , int y1 , int x2, int y2){
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    public static double ManhattanDistanceAll(GameState nextState) {
        HashMap<String, Pair<Integer , Integer>> pos = new HashMap<>();

        for (Pair<Integer, Integer> destinationCell : nextState.destinationCells) {
            int x = destinationCell.first;
            int y = destinationCell.second;
           //System.out.print(nextState.get_current_board_shallow()[x][y].getDestinationColor() + " ");
            pos.put(nextState.get_current_board_shallow()[x][y].getDestinationColor().toUpperCase(), new Pair<>(x, y));
        }
        //System.out.println();
        int cost = 0 ;
        List<Pair<Integer, Integer>> colored = nextState.getColoredCells();
        for (Pair<Integer, Integer> elm : colored) {
            int x = elm.first;
            int y = elm.second;
            //System.out.print(nextState.get_current_board_shallow()[x][y].getCellColor() + " ");
            if (pos.containsKey(nextState.get_current_board_shallow()[x][y].getCellColor().toUpperCase())) {
                int x2 = pos.get(nextState.get_current_board_shallow()[x][y].getCellColor().toUpperCase()).first;
                int y2 = pos.get(nextState.get_current_board_shallow()[x][y].getCellColor().toUpperCase()).second;
                cost += ManhattanDistance(x, y, x2, y2);
            }else{
                int x2 = pos.get("W").first;
                int y2 = pos.get("W").second;
                cost += ManhattanDistance(x, y, x2, y2);
            }
        }
        //System.out.println(cost);
        return cost;
    }
}
