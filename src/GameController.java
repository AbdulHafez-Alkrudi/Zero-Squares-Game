import java.util.*;

public class GameController {
    private GameState gameState ;
    private Player player ;
    private View view ;

    public void start_game()
    {
        Scanner in = new Scanner(System.in);
        int size = take_board_size(in);
        String[][] board = take_board(in , size);
        gameState = new GameState(size, board);

        // Choosing the type of the Player, User of AI :
        System.out.println("Wanna Play or try the Search Algo :)\n1) Play\n2)Search Algo");
        int type = in.nextInt();
        if(type == 1)
            player = new PlayerUser();
        else
            player = new PlayerAI() ;

        System.out.println("What kind of view do you prefer:\n1)Console\n2)Console");
        type = in.nextInt();
        if(type == 1)
            view = new ViewConsole();
        else
            view = new ViewGUI();
        run_the_game();
    }
    private List<Pair<Integer, Integer>> getColoredCells() {
        List<Pair<Integer, Integer>> cells = new ArrayList<>();
        Cell[][] board = gameState.get_current_board();
        int size = gameState.get_size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!board[i][j].getCellColor().equals("Gray") && !board[i][j].getCellColor().equals("Black")) {
                    cells.add(new Pair<>(i, j));
                }
            }
        }
        return cells;
    }
    public void run_the_game()
    {


        while(!gameState.check_winning())
        {
            boolean valid_move = false ;
            String move = "";
            while(!valid_move)
            {
                try{
                    valid_move = true ;
                    move = player.get_move();
                    // If this function didn't throw an exception, the user entered a valid move
                    gameState.check_move(0 , 0 , move);
                } catch(InputMismatchException e) {
                    valid_move = false;
                    System.out.print("An error occurred: " + e.getMessage());
                }
            }
            List<Pair<Integer , Integer>> colored_cells = getColoredCells();
            while(!colored_cells.isEmpty())
            {

                Cell[][] current_board = new Cell[gameState.get_size()][gameState.get_size()] ;
                Cell[][] new_board = new Cell[gameState.get_size()][gameState.get_size()];

                Cell[][] temp = gameState.get_current_board();
                for (int i = 0; i < gameState.get_size(); i++) {
                    for (int j = 0; j < gameState.get_size(); j++) {
                        // Assuming Cell has a copy constructor like `public Cell(Cell other)`
                        current_board[i][j] = new Cell(temp[i][j]); // Create a new instance for deep copy
                        new_board[i][j] = new Cell(temp[i][j]);
                    }
                }
                List<Pair<Integer, Integer>> new_colored_cells = new ArrayList<>();
                view.display(current_board, gameState.get_size());
                boolean restart = false ;
                for(Pair<Integer, Integer> cell : colored_cells)
                {
                    int x = cell.first , y = cell.second ;
                    // Here I should check if the move is invalid (if we make this move we will go out of the grid), I should restart the game
                    // else just make it
                    if(gameState.check_move(x, y, move))
                    {
                        Pair<Integer , Integer> new_cell = gameState.get_next_move(x, y, move);
                        int new_x = new_cell.first , new_y = new_cell.second ;
                        if(!current_board[new_x][new_y].getCellColor().equals("Gray")) continue ;
                        System.out.println("x :" + x + " y:" + y);
                        System.out.println("new x :" + new_x + " new y:" + new_y);
                        System.out.println(current_board[new_x][new_y].getCellColor());
                        new_board[new_x][new_y].setCellColor(current_board[x][y].getCellColor());
                        new_board[x][y].setCellColor("Gray");

                        // Checking If I'm currently at the destination:
                        if(new_board[new_x][new_y].arrived_to_destination())
                        {
                            new_board[new_x][new_y].setCellColor("Gray");
                            new_board[new_x][new_y].setDestinationColor("");
                            continue ;
                        }
                        else if(new_board[new_x][new_y].getDestinationColor().equals("W")){
                            new_board[new_x][new_y].setDestinationColor(new_board[new_x][new_y].getCellColor());
                        }
                        new_colored_cells.add(new Pair<>(new_x , new_y));
                    }
                    else
                    {
                        restart = true ;
                        break;
                    }
                }
                if(restart)
                {
                    System.out.println("Ooops, You've made an invalid move, I'll give you another chance sweety :)");
                    gameState.restart_game();
                    break;
                }
                gameState.set_current_board(new_board);
                if(new_colored_cells.isEmpty()) break;
                colored_cells = new ArrayList<>();

                colored_cells.addAll(new_colored_cells);
            }
        }
        view.display(gameState.get_current_board(), gameState.get_size());
        System.out.print("Winner Winner Chicken Dinner !!!!\n Congrats for solving this puzzle");
    }

    private int take_board_size(Scanner in)
    {
        int gridSize = 0;
        while (gridSize <= 0) {
            try {
                System.out.print("Enter grid size (n for an n x n grid): ");
                gridSize = in.nextInt();
                if (gridSize <= 0) {
                    System.out.println("Grid size must be a positive integer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                in.next();
            }
        }
        in.nextLine();
        return gridSize;
    }
    private String[][] take_board(Scanner in, int size)
    {
        String[][] grid = new String[size][size];

        boolean valid = false ;
        while(!valid)
        {
            System.out.println("Please Enter The Board");
            try{
                valid = true ;
                for(int i = 0 ; i < size ; i++)
                {
                    String input = in.nextLine();
                    String[] line = input.split(" ");
                    for(int j = 0 ; j < size ; j++)
                    {
                        grid[i][j] = line[j];
                        if(grid[i][j].length() == 1)
                        {
                            char cell = grid[i][j].charAt(0);
                            if(!Character.isAlphabetic(cell) && cell != '.' && cell != '#'){
                                throw new InputMismatchException("Invalid input. Please enter Alphabetical chars, dots or hashes only.");
                            }
                        }else{
                            char color = grid[i][j].charAt(0), destination = grid[i][j].charAt(1);
                            if(!Character.isLowerCase(color) || !Character.isUpperCase(destination)){
                                throw new InputMismatchException("Invalid input. Please enter Alphabetical chars, dots or hashes only.");
                            }
                        }

                    }
                }
            } catch (InputMismatchException e) {
                valid = false ;
                System.out.println("An error occurred : " + e.getMessage());
                in.nextLine();
            }
        }
        return grid ;
    }
}
