import java.util.*;

public class GameState {


    private int size ;
    private Cell[][] current_board;
    private Cell[][] initial_board;

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
    public GameState(){}
    public GameState(int size , String[][] initialGrid) {
        this.size = size ;
        this.current_board = new Cell[size][size] ;
        this.initial_board = new Cell[size][size] ;
        for(int i = 0 ; i < size ; i++)
        {
            for(int j = 0 ; j < size ; j++)
            {
                char cell = initialGrid[i][j].charAt(0) ;
                current_board[i][j] = new Cell() ;
                if (Character.isUpperCase(cell)) {
                    current_board[i][j].setDestinationColor(Character.toString(cell));
                }
                else if(Character.isLowerCase(cell))
                {
                    current_board[i][j].setCellColor(Character.toString(cell));
                    if(initialGrid[i][j].length() == 2)
                    {
                        current_board[i][j].setDestinationColor(Character.toString(initialGrid[i][j].charAt(1)));
                    }
                }else if(cell == '#'){
                    current_board[i][j].setCellColor("Black");
                }else{
                    current_board[i][j].setCellColor("Gray");
                }
                initial_board[i][j] = current_board[i][j];
            }
        }

    }
    public GameState(GameState other)
    {
        // Deep copy for current & Initial board
        this.current_board = new Cell[other.size][];
        this.initial_board = new Cell[other.size][];
        for (int i = 0; i < other.size; i++) {
            this.current_board[i] = new Cell[other.size];
            this.initial_board[i] = new Cell[other.size];
            for (int j = 0; j < other.size; j++) {
                this.current_board[i][j] = new Cell(other.current_board[i][j]); // Assuming Cell has a copy constructor
                this.initial_board[i][j] = new Cell(other.initial_board[i][j]); // Deep copy each cell

            }
        }
        this.size = other.size;
    }
    protected void restart_game()
    {
        for(int i = 0 ; i < size ; i++){
            for(int j = 0 ; j < size ; j++){
                current_board[i][j] = initial_board[i][j] ;
            }
        }

    }
    // This function checks whether the given coordinates are valid or not
    private boolean is_valid(int x , int y)
    {
        return x >= 0 && y >= 0 && x < size && y < size ;
    }

    // I'll give this function the current coordinates of the player, and the type of the move that I'll make and return
    // whether it's valid or not
    public boolean check_move(int x , int y , String move)
    {
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
    public Pair<Integer, Integer> get_next_move(int x , int y , String move)
    {
        return switch (move) {
            case "up"    -> new Pair<>(x + dx[UP]    , y + dy[UP]);
            case "down"  -> new Pair<>(x + dx[DOWN]  , y + dy[DOWN]);
            case "right" -> new Pair<>(x + dx[RIGHT] , y + dy[RIGHT]);
            case "left"  -> new Pair<>(x + dx[LEFT]  , y + dy[LEFT]);
            default -> throw new InputMismatchException("Invalid input!");               
        };

    }
    public boolean check_winning()
    {
        for(int i = 0 ; i < size ; i++)
        {
            for(int j = 0 ; j < size ; j++)
            {
                if(!Objects.equals(current_board[i][j].getCellColor(), "Black") && !Objects.equals(current_board[i][j].getCellColor(), "Gray"))
                {
                    return false ;
                }
            }
        }
        return true ;
    }

    public Cell[][] get_current_board_shallow() {
        return current_board;
    }
    public Cell[][] get_current_board_deep() {
        Cell[][] res = new Cell[size][size];
        for(int i = 0 ; i < size ; i++)
        {
            for(int j = 0 ; j < size ; j++)
            {
                res[i][j] = current_board[i][j] ;
            }
        }
        return res;
    }
    public void set_current_board(Cell[][] current_board) {
        int size = current_board.length;
        this.current_board = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.current_board[i][j] = new Cell(current_board[i][j]); // Create a new Cell instance for each element
            }
        }
    }

    public void set_initial_board(Cell[][] initial_board) {
        int size = initial_board.length;
        this.initial_board = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.initial_board[i][j] = new Cell(initial_board[i][j]); // Create a new Cell instance for each element
            }
        }
    }

    private List<Pair<Integer, Integer>> getColoredCells() {
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

    public GameState playMove(String move, boolean can_restart) {
        return playMove(move, can_restart, null);
    }

    public GameState playMove(String move , boolean can_restart, View view)
    {
        if (move.equals("restart")) {
            if (view instanceof ViewGUI) {
                ((ViewGUI) view).displayMessage("The Game will be restarted");
            } else if (view instanceof ViewConsole) {
                System.out.println("The Game will be restarted");
            }
            restart_game();
            return this;
        }
        GameState current_game_state = new GameState(this);
        List<Pair<Integer , Integer>> colored_cells = getColoredCells();
        while(!colored_cells.isEmpty())
        {
            List<Pair<Integer, Integer>> new_colored_cells = new ArrayList<>();
            GameState new_game_state = new GameState(current_game_state);
            //view.display(gameState.get_current_board_shallow(), gameState.get_size());
            boolean restart = false ;
            for(Pair<Integer, Integer> cell : colored_cells)
            {
                int x = cell.first , y = cell.second ;
                // Here I should check if the move is invalid (if we make this move we will go out of the grid), I should restart the game
                // else just make it
                if(current_game_state.check_move(x, y, move))
                {
                    Pair<Integer , Integer> new_cell = current_game_state.get_next_move(x, y, move);
                    int new_x = new_cell.first ;
                    int new_y = new_cell.second ;
                    // if the current color can't move, we'll ignore it the rest of the turn
                    if(!current_game_state.get_current_board_shallow()[new_x][new_y].getCellColor().equals("Gray")) continue ;

                    Cell[][] new_board = new_game_state.get_current_board_shallow();
                    new_board[new_x][new_y].setCellColor(current_game_state.get_current_board_shallow()[x][y].getCellColor());
                    new_board[x][y].setCellColor("Gray");
                    // Checking If I'm currently at the destination:
                    if(new_board[new_x][new_y].arrived_to_destination())
                    {
                        new_board[new_x][new_y].setCellColor("Gray");
                        new_board[new_x][new_y].setDestinationColor("");
                        continue ;
                    }
                    else if(new_board[new_x][new_y].getDestinationColor().equals("W")){
                        new_board[new_x][new_y].setDestinationColor(new_board[new_x][new_y].getCellColor().toUpperCase());
                    }
                    new_colored_cells.add(new Pair<>(new_x , new_y));
                }
                else
                {
                    restart = true ;
                    break;
                }
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
                    new_game_state.restart_game();
                    return new_game_state;
                }
                // else I'm using nextStates function and I only need the valid next moves
                return null ;
            }

            current_game_state = new GameState(new_game_state);
            if(new_colored_cells.isEmpty()) break;
            colored_cells = new ArrayList<>();

            colored_cells.addAll(new_colored_cells);
        }
        return current_game_state;
    }


    public List<Pair<String, GameState>> nextStates() {
        List<Pair<String, GameState>> nextStates = new ArrayList<>();
        String[] moves = {"up", "down", "left", "right"};

        for (String move : moves) {
            GameState newState = playMove(move, false);
            if (newState != null && !Arrays.deepEquals(newState.get_current_board_shallow(), get_current_board_shallow())) {
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
        return size == gameState.size &&
                Arrays.deepEquals(current_board, gameState.current_board) &&
                Arrays.deepEquals(initial_board, gameState.initial_board);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.deepHashCode(current_board);
        result = 31 * result + Arrays.deepHashCode(initial_board);
        return result;
    }

}
