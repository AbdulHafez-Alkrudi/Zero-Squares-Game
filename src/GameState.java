import java.util.*;
import java.util.List;

@SuppressWarnings("CopyConstructorMissesField")
public class GameState {


    private int size ;
    private Integer hash;
    public Cell[][] currentBoard;
    public List<Pair<Integer , Integer>> coloredCells , destinationCells;

    // this attribute used for algorithms like UCS, A* to know its cost when I want to visit it
    public int cost = 0 ;

    /*
    *  Cells Representation in the Grid:
    *  1- Blocked Cells: #
    *  2- Colored Cells: any small-letter alphabet a-z
    *  3- Destination Cells: capital-letter alphabet A-Z
    */

    // This map stores all The Goals positions

    private static final int[] dx = {-1 , 1 , 0 , 0} ;
    private static final int[] dy = {0 , 0 , 1 , -1} ;

    // These parameters represent the indices of each move
    private static final int UP = 0 ;
    private static final int DOWN = 1 ;
    private static final int RIGHT = 2 ;
    private static final int LEFT = 3 ;

    public static int numberOfCreatedObjects = 0;
    public GameState(){
        numberOfCreatedObjects++;
    }
    public GameState(int size , Cell[][] initialBoard) {
        numberOfCreatedObjects++;
        this.size = size ;
        currentBoard = new Cell[size][size] ;
        for(int i = 0 ; i < size ; i++)
        {
            //System.arraycopy(initialBoard[i], 0, currentBoard[i], 0, size);
            System.arraycopy(initialBoard[i], 0, currentBoard[i], 0, size);
        }
        this.coloredCells = this.getColoredCells();
        this.destinationCells = this.getDestinationCells();
        this.cost = 0 ;
    }
    public GameState(GameState other)
    {
        numberOfCreatedObjects++;
        // Deep copy for current & Initial board
        this.currentBoard = new Cell[other.size][];
        for (int i = 0; i < other.size; i++) {
            this.currentBoard[i] = new Cell[other.size];
            for (int j = 0; j < other.size; j++) {
                this.currentBoard[i][j] = new Cell(other.currentBoard[i][j]);
            }
        }
        coloredCells = new ArrayList<>(other.coloredCells);
        destinationCells = new ArrayList<>(other.destinationCells);
        this.size = other.size;
        this.cost = other.cost;
    }
    protected void restart_game(Cell[][] initial_board)  {
        for(int i = 0 ; i < size ; i++){
            for (int j = 0; j < size; j++) {
                this.currentBoard[i][j] = new Cell(initial_board[i][j]);
            }
        }
        coloredCells = getColoredCells();
        destinationCells = getDestinationCells();
    }
    // This function checks whether the given coordinates are valid or not
    private boolean is_valid(int x , int y)
    {
        return x >= 0 && y >= 0 && x < size && y < size ;
    }

    // I'll give this function the current coordinates of the player, and the type of the move that I'll make and return
    // whether it's valid or not
    public boolean check_move(int x , int y , String move)  {
        // here when the move isn't valid (I went out of the grid), I should restart the game:
        return switch (move) {
            case "up"    -> is_valid(x + dx[UP], y + dy[UP]);
            case "down"  -> is_valid(x + dx[DOWN], y + dy[DOWN]);
            case "right" -> is_valid(x + dx[RIGHT], y + dy[RIGHT]);
            case "left"  -> is_valid(x + dx[LEFT], y + dy[LEFT]);
            default      -> throw new InputMismatchException("Invalid input!");
        };

    }

    // This function returns the next coordinates according to a specific move:
    public Pair<Integer, Integer> get_next_move(int x , int y , String move)  {
        return switch (move) {
            case "up"    -> new Pair<>(x + dx[UP]    , y + dy[UP]);
            case "down"  -> new Pair<>(x + dx[DOWN]  , y + dy[DOWN]);
            case "right" -> new Pair<>(x + dx[RIGHT] , y + dy[RIGHT]);
            case "left"  -> new Pair<>(x + dx[LEFT]  , y + dy[LEFT]);
            default -> throw new InputMismatchException("Invalid input!");               
        };

    }
    public boolean check_winning()  {
        for(int i = 0 ; i < size ; i++)
        {
            for(int j = 0 ; j < size ; j++)
            {
                if(!Objects.equals(currentBoard[i][j].getCellColor(), "Black") && !Objects.equals(currentBoard[i][j].getCellColor(), "Gray"))
                {
                    return false ;
                }
            }
        }
        return true ;
    }

    public Cell[][] get_current_board_shallow() {
        return currentBoard;
    }
    public Cell[][] get_current_board_deep() {
        Cell[][] res = new Cell[size][size];
        for(int i = 0 ; i < size ; i++)
        {
            for(int j = 0 ; j < size ; j++)
            {
                res[i][j] = new Cell(currentBoard[i][j]) ;
            }
        }
        return res;
    }
    public void set_current_board(Cell[][] current_board) {
        int size = current_board.length;
        this.currentBoard = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.currentBoard[i][j] = new Cell(current_board[i][j]); // Create a new Cell instance for each element
            }
        }
    }

    public List<Pair<Integer, Integer>> getColoredCells() {
        List<Pair<Integer, Integer>> cells = new ArrayList<>();
        Cell[][] board = get_current_board_shallow();
        int size = get_size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!board[i][j].getCellColor().equals("Gray") && !board[i][j].getCellColor().equals("Black")) {
                    cells.add(new Pair<>(i, j));
                }
            }
        }
        return cells;
    }
    public List<Pair<Integer, Integer>> getDestinationCells() {
        List<Pair<Integer, Integer>> cells = new ArrayList<>();
        Cell[][] board = get_current_board_shallow();
        int size = get_size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!board[i][j].getDestinationColor().isBlank()) {
                    cells.add(new Pair<>(i, j));
                }
            }
        }
        return cells;
    }
    public GameState playMove(String move, boolean can_restart) {
        return playMove(move, can_restart,null,null);
    }
    public GameState playMove(String move , boolean can_restart, Cell[][] initialBoard, View view)  {
        if (move.equals("restart")) {
            if (view instanceof ViewGUI) {
                ((ViewGUI) view).displayMessage("The Game will be restarted");
            } else if (view instanceof ViewConsole) {
                System.out.println("The Game will be restarted");
            }
            restart_game(initialBoard);
            return this;
        }
        else if(move.equals("all"))
        {
            List<Pair<String, GameState>> allPossibleBoards = nextStates();
            allPossibleBoards.forEach(state -> {
                System.out.print(state.first + " : \n");
                view.display(state.second.get_current_board_shallow(), state.second.get_size());
            });
            return this;
        }
        GameState newGameState = new GameState(this);
        Cell[][] newBoard = newGameState.currentBoard;
        Queue <Pair<Integer, Integer>> ColoredCells = new LinkedList<>();
        for(Pair<Integer, Integer> cell : coloredCells)  {
            int x = cell.first ;
            int y = cell.second;
            if(!check_move(x , y , move)){
                if (can_restart) {
                    if(view instanceof ViewGUI)
                    {
                        ((ViewGUI) view).displayMessage("Oops!! You've made an invalid move, don't worry sweety, I'll give you another chance :) ");
                    }else{
                        System.out.println("Oops!! You've made an invalid move, don't worry sweety, I'll give you another chance :) ") ;
                    }
                    newGameState.restart_game(initialBoard);
                    return newGameState;
                }
                // else I'm using nextStates function and I only need the valid next moves
                return null ;
            }
            Pair<Integer , Integer> new_cell = get_next_move(x , y , move);
            int new_x = new_cell.first ;
            int new_y = new_cell.second;
            if(newBoard[new_x][new_y].getCellColor().equals("Gray")){
                ColoredCells.add(new Pair<>(x , y));
            }
        }
        int cost = 0 ;
        while(!ColoredCells.isEmpty())
        {

            boolean restart = false ;
            Pair<Integer, Integer> cell  = ColoredCells.poll();
            int x = cell.first , y = cell.second ;
            // Here I should check if the move is invalid (if we make this move we will go out of the grid), I should restart the game
            // else just make it
            if(newGameState.check_move(x , y , move)){


                Pair<Integer, Integer> new_cell = get_next_move(x , y , move);
                int new_x = new_cell.first ;
                int new_y = new_cell.second ;
                // if the current color can't move, we'll ignore it the rest of the turn
                if(!newBoard[new_x][new_y].getCellColor().equals("Gray")) continue ;
                cost++;
                newBoard[new_x][new_y].setCellColor(newBoard[x][y].getCellColor());
                newBoard[x][y].setCellColor("Gray");
                // Checking If I'm currently at the destination:
                if(newBoard[new_x][new_y].arrived_to_destination())
                {
                    newBoard[new_x][new_y].setCellColor("Gray");
                    newBoard[new_x][new_y].setDestinationColor("");
                    continue ;
                }
                else if(newBoard[new_x][new_y].getDestinationColor().equals("W")){
                    newBoard[new_x][new_y].setDestinationColor(newBoard[new_x][new_y].getCellColor().toUpperCase());
                }
                ColoredCells.add(new Pair<>(new_x , new_y));
            }
            else{
                restart = true ;
            }
            if(restart) {
                // Here the user is playing the game, and I'll restart the game for him/her
                if (can_restart) {
                    if(view instanceof ViewGUI)
                    {
                        ((ViewGUI) view).displayMessage("Oops!! You've made an invalid move, don't worry sweety, I'll give you another chance :) ");
                    }else{
                        System.out.println("Oops!! You've made an invalid move, don't worry sweety, I'll give you another chance :) ") ;
                    }
                    newGameState.restart_game(initialBoard);
                    return newGameState;
                }
                // else I'm using nextStates function and I only need the valid next moves
                return null ;
            }
        }
        newGameState.coloredCells = newGameState.getColoredCells();
        newGameState.destinationCells = newGameState.getDestinationCells();
        newGameState.cost = cost ;
//        newGameState.cost = 1 ;

        return newGameState ;
    }
    public List<Pair<String, GameState>> nextStates() {
        List<Pair<String, GameState>> nextStates = new ArrayList<>();
        String[] moves = {"up", "down", "left", "right"};

        for (String move : moves) {
            GameState newState = playMove(move, false);
            if (newState != null && !Arrays.deepEquals(newState.get_current_board_shallow(), get_current_board_shallow())/* && Heuristic.every_player_has_goal(newState) != 1_000_000_000*/) {
                nextStates.add(new Pair<>(move, newState));
            }
        }
        return nextStates;
    }
    public int get_size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return Objects.deepEquals(currentBoard, gameState.currentBoard);
    }

    @Override
    public int hashCode() {
        if(hash!=null) return hash;
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<size;i++){
            for(int j = 0; j<size;j++){
                if(!currentBoard[i][j].getCellColor().equals("Gray") && !currentBoard[i][j].getCellColor().equals("Black")){
                    sb.append(i);sb.append(j);sb.append(currentBoard[i][j].getCellColor());
                }
                if(!currentBoard[i][j].getDestinationColor().isBlank()){
                    sb.append(i);sb.append(j);sb.append(currentBoard[i][j].getDestinationColor());
                }
            }
        }
        hash = sb.toString().hashCode();
        return hash;
    }

    /*@Override
    public int hashCode() {
        return Objects.hash(coloredCells, destinationCells);
    }*/
}
