import java.util.HashSet;
import java.util.Set;

public final class Heuristic {
    public static double get(GameState gameState)
    {
        /*double cost = every_player_has_goal(gameState) ;
        if(cost == 1_000_000_000) return cost ;
*/
        double cost = hasRepeatedGoals(gameState);
        if(cost == 1_000_000_000) return cost ;

        return manhattan(gameState) + min_no_players_for_a_player_to_reach_a_goal(gameState);
    }

    public static int hasRepeatedGoals(GameState gameState) {
        // Use a HashSet to track seen goal types
        Set<String> seenGoals = new HashSet<>();

        for (Pair<Integer , Integer> pos : gameState.destinationCells) {
            int x = pos.first ;
            int y = pos.second;
            if (seenGoals.contains(gameState.currentBoard[x][y].getDestinationColor())) {
                // If the goal type has already been seen, it's repeated
                return 1_000_000_000;
            }
            // Add the goal type to the set
            seenGoals.add(gameState.currentBoard[x][y].getDestinationColor());
        }
        // No repeated goals found
        return 0;
    }
    public static double manhattan(GameState gameState)
    {
        return Algorithm.ManhattanDistanceAll(gameState) ;
    }
    /*public static double every_player_has_goal(GameState state){
        Set<String> goals = new HashSet<>();
        int white = 0 ;
        for(Pair<Integer, Integer> goal : state.destinationCells){
            int x = goal.first;
            int y = goal.second;

            if(state.currentBoard[x][y].getDestinationColor().equals("W")){
                white++;
            }else{
                goals.add((state.currentBoard[x][y].getDestinationColor().toLowerCase()));
            }
        }
        for(Pair<Integer, Integer> player : state.coloredCells)
        {
            int x = player.first;
            int y = player.second;
            if(!goals.contains(state.currentBoard[x][y].getCellColor())){
                white--;
            }
        }
        // if the game is unsolvable, give it a very high cost, else 0
        return white >= 0 ? 0 : 1_000_000_000;
    }*/
    public static double min_no_players_for_a_player_to_reach_a_goal(GameState gameState)
    {

        // Each player going to reach the answer from either of the 4 directions, so for each direction, I'll calculate the minimum
        // number of player that I depend on to reach it:
        int cost = 0 ;
        int max_cost = 0 ;

        Cell[][] currBoard = gameState.currentBoard;
        // if the max one is greater than the number of players, then it's unsolvable
        for (Pair<Integer, Integer> destinationCell : gameState.destinationCells) {
            int x = destinationCell.first;
            int y = destinationCell.second;
            int mn = 1_000_000_000 ;
            // testing up direction:
            while(x >= 0 && !currBoard[x][y].getCellColor().equals("Black")) x--;
            if(x >= 0)
            {
                // Left:
                while(y >= 0 && !currBoard[x][y].getCellColor().equals("Black")) y--;
                if(y >= 0) mn = Math.min(mn , Math.abs(destinationCell.second - y) - 1);
                y = destinationCell.second;
                // right:
                while(y < gameState.get_size() && !currBoard[x][y].getCellColor().equals("Black")) y++;
                if(y < gameState.get_size()) mn = Math.min(mn, Math.abs(y - destinationCell.second) - 1);
            }
            x = destinationCell.first;
            y = destinationCell.second;
            // testing down direction :
            while(x < gameState.get_size() && !currBoard[x][y].getCellColor().equals("Black")) x++;
            if(x < gameState.get_size() )
            {
                // Left:
                while(y >= 0 && !currBoard[x][y].getCellColor().equals("Black")) y--;
                if(y >= 0) mn = Math.min(mn , Math.abs(destinationCell.second - y) - 1);
                y = destinationCell.second;
                // right:
                while(y < gameState.get_size() && !currBoard[x][y].getCellColor().equals("Black")) y++;
                if(y < gameState.get_size()) mn = Math.min(mn, Math.abs(y - destinationCell.second) - 1);
            }

            // testing left direction:
            x = destinationCell.first;
            y = destinationCell.second;
            while(y >= 0 && !currBoard[x][y].getCellColor().equals("Black")) y--;

            if(y >= 0)
            {
                // up:
                while(x >= 0 && !currBoard[x][y].getCellColor().equals("Black")) x--;
                if(x >= 0) mn = Math.min(mn , Math.abs(destinationCell.first - x) - 1);
                x = destinationCell.first;
                // down:
                while(y < gameState.get_size() && !currBoard[x][y].getCellColor().equals("Black")) x++;
                if(y < gameState.get_size()) mn = Math.min(mn, Math.abs(x - destinationCell.first) - 1);
            }
            x = destinationCell.first;
            y = destinationCell.second;
            // testing right direction :
            while(y < gameState.get_size() && !currBoard[x][y].getCellColor().equals("Black")) y++;

            if(y< gameState.get_size())
            {
                // up:
                while(x >= 0 && !currBoard[x][y].getCellColor().equals("Black")) x--;
                if(x >= 0) mn = Math.min(mn , Math.abs(destinationCell.first - x) - 1);
                x = destinationCell.first;
                // down:
                while(y < gameState.get_size() && !currBoard[x][y].getCellColor().equals("Black")) x++;
                if(y < gameState.get_size()) mn = Math.min(mn, Math.abs(x - destinationCell.first) - 1);
            }

            cost += mn ;
            max_cost = Math.max(max_cost, mn);
        }
        if(max_cost > gameState.coloredCells.size()) cost = 1_000_000_000 ;
        return cost ;
    }
    
}
