import java.util.*;

public class GameController {
    private GameState gameState ;
    private Player player ;
    private Algorithm algorithm;
    private View view ;
    private final Scanner in = new Scanner(System.in);

    public void startGame() {
        int size = takeBoardSize(in);
        String[][] board = takeBoard(in, size);
        gameState = new GameState(size, board);
        System.out.println("What kind of view do you prefer?");
        System.out.println("1) Console");
        System.out.println("2) GUI");
        int choice = in.nextInt();
        in.nextLine(); // Consume the remaining newline
        switch (choice) {
            case 1:
                view = new ViewConsole();
                break;
            case 2:
                view = new ViewGUI();
                break;
            default:
                System.out.println("Invalid choice. Defaulting to GUI view.");
                view = new ViewGUI();
                break;
        }

        // Choose the type of the player: User or AI
        System.out.println("Do you want to Play or try the Search Algorithm?");
        System.out.println("1) Play");
        System.out.println("2) Search Algorithm");
        choice = in.nextInt();
        in.nextLine(); // Consume the remaining newline
        switch (choice) {
            case 1:
                player = new Player();
                runGameAsUser();
                return ;
            case 2:
                runGameAsAI();
                return ;
            default:
                System.out.println("Invalid choice. Defaulting to Play.");
                player = new Player();
                runGameAsUser();
                break;
        }
    }
    public void runGameAsAI(){
        System.out.println("Which Algorithm you would like to use ?");
        System.out.println("1) BFS");
        System.out.println("2) Parallel BFS");
        System.out.println("3) DFS");

        int choice = in.nextInt();
        switch (choice){
            case 1:
                algorithm = new Algo_BFS();
                break;
            case 2:
                algorithm = new Algo_BFS_Parallel();
                break;
            case 3:
                algorithm = new Algo_DFS();
            default:
                System.out.println("Invalid input, Defaulting the BFS");
                algorithm = new Algo_BFS();
                break;
        }
        long startTime = System.nanoTime();
        for(int i = 0 ; i < 10 ; i++) algorithm.run(gameState, view);

        List<String> path = algorithm.run(gameState, view);
        long endTime = System.nanoTime();
        long duration = endTime - startTime; // in nanoseconds
        double durationInSeconds = duration / 1_000_000_000.0;
        System.out.println("Execution Time: " + durationInSeconds + " seconds");
        path.forEach(move -> {
            try {
                System.out.println(move);
                gameState = gameState.playMove(move , true , view);
                view.display(gameState.get_current_board_shallow(), gameState.get_size());
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });




    }
    public void runGameAsUser()
    {
        if(view instanceof ViewConsole)
        {
            while(!gameState.check_winning())
            {
                view.display(gameState.get_current_board_shallow(), gameState.get_size());
                String move = player.get_move();
                gameState = gameState.playMove(move , true , view);
                view.display(gameState.get_current_board_shallow(), gameState.get_size());
            }
            System.out.print("Winner Winner Chicken Dinner !!!!\nCongrats for solving this puzzle");

        }else{
            view.display(gameState.get_current_board_shallow(), gameState.get_size());

            ((ViewGUI) view).setMoveListener(move -> {
                System.out.println("move = " + move);
                gameState = gameState.playMove(move, true, view);
                view.display(gameState.get_current_board_shallow(), gameState.get_size());


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

}
