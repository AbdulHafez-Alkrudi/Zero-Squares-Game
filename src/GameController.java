import java.util.*;

public class GameController {
    private GameState gameState ;
    private Player player ;
    private View view ;

    public void startGame() {
        Scanner in = new Scanner(System.in);
        int size = takeBoardSize(in);
        String[][] board = takeBoard(in, size);
        gameState = new GameState(size, board);

        // Choose the type of the player: User or AI
        System.out.println("Do you want to Play or try the Search Algorithm?");
        System.out.println("1) Play");
        System.out.println("2) Search Algorithm");
        int choice = in.nextInt();
        in.nextLine(); // Consume the remaining newline
        switch (choice) {
            case 1:
                player = new PlayerUser();
                break;
            case 2:
                player = new PlayerAI();
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Play.");
                player = new PlayerUser();
                break;
        }

        // Choose the type of view: Console or GUI
        System.out.println("What kind of view do you prefer?");
        System.out.println("1) Console");
        System.out.println("2) GUI");
        choice = in.nextInt();
        in.nextLine(); // Consume the remaining newline
        switch (choice) {
            case 1:
                view = new ViewConsole();
                break;
            case 2:
                view = new ViewGUI();
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Console view.");
                view = new ViewConsole();
                break;
        }

        runGame();
    }
    private List<Pair<Integer, Integer>> getColoredCells() {
        List<Pair<Integer, Integer>> cells = new ArrayList<>();
        Cell[][] board = gameState.get_current_board_shallow();
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
    public void runGame()
    {
        if(view instanceof ViewConsole)
        {
            while(!gameState.check_winning())
            {
                view.display(gameState.get_current_board_shallow(), gameState.get_size());
                String move = player.get_move();
                gameState = playMove(move , true);
                view.display(gameState.get_current_board_shallow(), gameState.get_size());
                System.out.print("Winner Winner Chicken Dinner !!!!\n Congrats for solving this puzzle");
            }
        }else{
            view.display(gameState.get_current_board_shallow(), gameState.get_size());
            ((ViewGUI) view).setMoveListener(move -> {
                view.display(gameState.get_current_board_shallow(), gameState.get_size());

                gameState = playMove(move, true);


                if (gameState.check_winning()) {
                    // Game over - display winning message
                    ((ViewGUI) view).displayMessage("Winner Winner Chicken Dinner !!!!\n Congrats for solving this puzzle");
                    ((ViewGUI) view).close();
                }
            });
        }

    }

    private int takeBoardSize(Scanner in) {
        int gridSize = 0;
        while (gridSize <= 0) {
            try {
                System.out.print("Enter grid size (n for an n x n grid): ");
                gridSize = in.nextInt();
                if (gridSize <= 0) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                in.next(); // Clear invalid input
            }
        }
        in.nextLine(); // Consume the remaining newline
        return gridSize;
    }
    private String[][] takeBoard(Scanner in, int size) {
        String[][] grid = new String[size][size];
        boolean valid = false;

        while (!valid) {
            System.out.println("Please enter the board:");
            try {
                valid = true;
                for (int i = 0; i < size; i++) {
                    String input = in.nextLine();
                    String[] line = input.trim().split("\\s+");
                    if (line.length != size) {
                        throw new InputMismatchException("Each line must contain exactly " + size + " elements.");
                    }
                    for (int j = 0; j < size; j++) {
                        grid[i][j] = line[j];
                        if (grid[i][j].length() == 1) {
                            char cell = grid[i][j].charAt(0);
                            if (!Character.isAlphabetic(cell) && cell != '.' && cell != '#') {
                                throw new InputMismatchException("Invalid input. Use alphabetical chars, dots, or hashes only.");
                            }
                        } else if (grid[i][j].length() == 2) {
                            char color = grid[i][j].charAt(0);
                            char destination = grid[i][j].charAt(1);
                            if (!Character.isLowerCase(color) || !Character.isUpperCase(destination)) {
                                throw new InputMismatchException("Invalid input. Use lowercase for color and uppercase for destination.");
                            }
                        } else {
                            throw new InputMismatchException("Invalid cell format.");
                        }
                    }
                }
            } catch (InputMismatchException e) {
                valid = false;
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        return grid;
    }
    private GameState playMove(String move , boolean can_restart)
    {
        if(move.equals("restart"))
        {
            if(view instanceof ViewGUI)
            {
                ((ViewGUI) view).displayMessage("The Game will be restarted");
            }else{
                System.out.println("The Game will be restarted ") ;
            }
            gameState.restart_game();
            return gameState;
        }
        GameState old_game_state = new GameState(gameState);
        GameState new_game_state = new GameState(gameState);
        List<Pair<Integer , Integer>> colored_cells = getColoredCells();
        while(!colored_cells.isEmpty())
        {
            List<Pair<Integer, Integer>> new_colored_cells = new ArrayList<>();
            view.display(gameState.get_current_board_shallow(), gameState.get_size());
            boolean restart = false ;
            for(Pair<Integer, Integer> cell : colored_cells)
            {
                int x = cell.first , y = cell.second ;
                // Here I should check if the move is invalid (if we make this move we will go out of the grid), I should restart the game
                // else just make it
                if(gameState.check_move(x, y, move))
                {
                    Pair<Integer , Integer> new_cell = gameState.get_next_move(x, y, move);
                    int new_x = new_cell.first ;
                    int new_y = new_cell.second ;
                    // if the current color can't move, we'll ignore it the rest of the turn
                    if(!gameState.get_current_board_shallow()[new_x][new_y].getCellColor().equals("Gray")) continue ;

                    Cell[][] new_board = new_game_state.get_current_board_shallow();
                    new_board[new_x][new_y].setCellColor(gameState.get_current_board_shallow()[x][y].getCellColor());
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
            gameState = new GameState(new_game_state);
            if(new_colored_cells.isEmpty()) break;
            colored_cells = new ArrayList<>();

            colored_cells.addAll(new_colored_cells);
        }
        // I'll just return the game state to the initial state before I've done anymove
        gameState = new GameState(old_game_state);
        return new_game_state;
    }
    public List<Pair<String, GameState>> nextStates() {
        List<Pair<String, GameState>> nextStates = new ArrayList<>();
        String[] moves = {"up", "down", "left", "right"};

        for (String move : moves) {
            GameState newState = playMove(move, false);
            if (newState != null) {
                nextStates.add(new Pair<>(move, newState));
            }
        }
        return nextStates;
    }
}
