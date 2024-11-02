import java.util.*;

public class GameState {


    private int size ;



    /*
    *  Cells Representation in the Grid:
    *  1- Blocked Cells: #
    *  2- Colored Cells: any small-letter alphabet a-z
    *  3- Destination Cells: These cells
    */
    private Cell[][] current_board;
    private Cell[][] initial_board;

    // This map stores all The Goals positions

    private static final int[] dx = {-1 , 1 , 0 , 0} ;
    private static final int[] dy = {0 , 0 , 1 , -1} ;

    // These parameters represent the indices of each move
    private static final int UP = 0 ;
    private static final int DOWN = 1 ;
    private static final int RIGHT = 2 ;
    private static final int LEFT = 3 ;

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
        // Deep copy for current & Initial board board
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
    private boolean is_valid(int x , int y)
    {
        return x >= 0 && y >= 0 && x < size && y < size ;
    }
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
        return current_board.clone();
    }
    public void set_current_board(Cell[][] current_board) {
        this.current_board = current_board.clone();
    }
    public int get_size() {
        return size;
    }
}
