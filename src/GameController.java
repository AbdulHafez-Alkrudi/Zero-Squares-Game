import java.io.*;
import java.util.*;

public class GameController {
    private GameState gameState ;
    private Player player ;
    private Algorithm algorithm;
    private View view ;
    private final Scanner in = new Scanner(System.in);

    private Cell[][] initialBoard;
    public void startGame() throws IOException {
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
    public void InitializeBoard(int size , String[][] initialGrid) {

        initialBoard = new Cell[size][size] ;
        for(int i = 0 ; i < size ; i++)
        {
            for(int j = 0 ; j < size ; j++)
            {
                char cell = initialGrid[i][j].charAt(0) ;
                initialBoard[i][j] = new Cell() ;
                if (Character.isUpperCase(cell)) {
                    initialBoard[i][j].setDestinationColor(Character.toString(cell));
                }
                else if(Character.isLowerCase(cell))
                {
                    initialBoard[i][j].setCellColor(Character.toString(cell));
                    if(initialGrid[i][j].length() == 2)
                    {
                        initialBoard[i][j].setDestinationColor(Character.toString(initialGrid[i][j].charAt(1)));
                    }
                }else if(cell == '#'){
                    initialBoard[i][j].setCellColor("Black");
                }else{
                    initialBoard[i][j].setCellColor("Gray");
                }
                initialBoard[i][j] = initialBoard[i][j];
            }
        }

    }
    public void runGameAsAI() throws IOException {
        boolean success = false ;
        String grid[][] = null ;
        System.out.println("Do you want to log the results of all the levels ???");
        System.out.println("1) Yes");
        System.out.println("2) No");
        int all = 0;
        all = in.nextInt();
        in.nextLine();
        String levelNumber = null ;
        int level = 0 ;
        if(all != 1){
            while (!success) {
                try {
                    System.out.println("Enter the level number (e.g., 2 for Level2): ");
                    levelNumber = in.nextLine();
                    level = Integer.parseInt(levelNumber);

                    grid = this.getLevel(level);
                    success = true ;
                } catch (FileNotFoundException e) {
                    System.out.println("Level file not found. Please try again.");
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the file. Please try again.");
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid file format. Ensure the first line contains the size.");
                }
            }
            System.out.println("Level successfully loaded.");
        }else{
            level = -1 ;
        }

        System.out.println("Which Algorithm you would like to use ?");
        System.out.println("0) All Algorithms");
        System.out.println("1) BFS");
        System.out.println("2) Parallel BFS");
        System.out.println("3) DFS");
        System.out.println("4) DFS Recursive");
        System.out.println("5) UCS");
        System.out.println("6) A*");
        System.out.println("7) Hill Climbing Simple");
        System.out.println("8) Hill Climbing Steepest");
        int choice = in.nextInt();
        in.nextLine(); // Consume the remaining newline




        if (choice == 0 || choice == 1) {
            algorithm = new Algo_BFS();
            run_ai_algorithm("BFS" , level);
        }
        if (choice == 0 || choice == 2) {
            algorithm = new Algo_BFS_Parallel();
            run_ai_algorithm("Parallel BFS", level);
        }
        if (choice == 0 || choice == 3) {
            algorithm = new Algo_DFS();
            run_ai_algorithm("DFS", level);
        }
        if (choice == 0 || choice == 4) {
            algorithm = new Algo_DFS_Recursive();
            run_ai_algorithm("DFS Recursive" , level);
        }
        if (choice == 0 || choice == 5) {
            algorithm = new Algo_UCS();
            run_ai_algorithm("UCS" , level );
        }
        if (choice == 0 || choice == 6) {
            algorithm = new Algo_A_Star();
            run_ai_algorithm("A*" , level);
        }
        if (choice == 0 || choice == 7) {
            algorithm = new Algo_Hill_Climbing_Simple();
            run_ai_algorithm("Hill Climbing Simple" , level);
        }
        if (choice == 0 || choice == 8) {
            algorithm = new Algo_Hill_Climbing_Steepest();
            run_ai_algorithm("Hill Climbing Steepest" , level);
        }


    }
    public void runGameAsUser() {
        boolean success = false ;
        String grid[][] = null ;
        while (!success) {
            try {
                success = true ;
                System.out.println("Enter the level number (e.g., 2 for Level2): ");
                String levelNumber = in.nextLine();
                grid = this.getLevel(Integer.parseInt(levelNumber));
            }  catch (FileNotFoundException e) {
                System.out.println("Level file not found. Please try again.");
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file. Please try again.");
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Invalid file format. Ensure the first line contains the size.");
            }
        }
        System.out.println("Level successfully loaded.");

        if(view instanceof ViewConsole)
        {
            while(!gameState.check_winning())
            {
                System.out.println(gameState.coloredCells);
                view.display(gameState.get_current_board_shallow(), gameState.get_size());
                String move = player.get_move();
                gameState = gameState.playMove(move , true , initialBoard,  view);
                System.out.println(gameState.cost);
            }
            System.out.print("Winner Winner Chicken Dinner !!!!\nCongrats for solving this puzzle");

        }else{
            view.display(gameState.get_current_board_shallow(), gameState.get_size());

            ((ViewGUI) view).setMoveListener(move -> {
                System.out.println("move = " + move);
                gameState = gameState.playMove(move, true, initialBoard, view);
                view.display(gameState.get_current_board_shallow(), gameState.get_size());
                System.out.println(gameState.cost);

                if (gameState.check_winning()) {
                    // Game over - display winning message
                    ((ViewGUI) view).displayMessage("Winner Winner Chicken Dinner !!!!\n Congrats for solving this puzzle");
                    ((ViewGUI) view).close();
                }
            });

        }

    }
    private String[][] getLevel(int levelNumber) throws IOException {
        String fileName = "src/Levels/Level" + levelNumber + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read the size of the board
            String firstLine = reader.readLine();
            int size = Integer.parseInt(firstLine);

            // Initialize the grid
            String[][] grid = new String[size][size];
            // Read and validate the board
            grid = takeBoard(reader, size);
            InitializeBoard(size , grid);
            this.gameState = new GameState(size, initialBoard);
            return grid ;
        }
    }
    int index = 0 ;
    public void run_ai_algorithm(String algorithmName, int levelNumber ) throws IOException {
        for(int i = 1 ; i <= 30 ; i++) {
            if (levelNumber != -1 && i != levelNumber) continue;

            try {
                // Just checking if the file exists or not:
                getLevel(i);
            } catch (IOException e) {
                System.out.println("This level doesn't exist");
                continue;
            }
            // Log file setup
            File logFile = getFile(i);
            // Create a BufferedWriter for appending to the log file
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true))) {
                try {
                    logWriter.write("Level " + i + " is running:\n");
                    System.out.println("Running " + algorithmName + "...");

                    // Log the name of the algorithm:
                    logWriter.write("Algorithm: " + algorithmName + "\n");

                    // Track memory usage before execution
                    Runtime runtime = Runtime.getRuntime();
                    long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();;
                    long startTime = System.nanoTime();
                    List<String> path = algorithm.run(gameState, logWriter);
                    long endTime = System.nanoTime();
                    long duration = endTime - startTime; // in nanoseconds
                    double durationInSeconds = duration / 1_000_000_000.0;


                    long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    long memoryUsed = memoryAfter - memoryBefore;
                    // Log execution details
                    logWriter.write("Execution Time: " + durationInSeconds + " seconds\n");
                    logWriter.write("Number of Moves: " + path.size() + "\n");
                    logWriter.write("Path: " + path + "\n");
                    logWriter.write("Memory Usage: " + (memoryUsed / 1024.0) + " KB\n");
                    logWriter.write("--------------------------------------------------------------------------------------------\n");

                    logWriter.close();
                    // Display results
                    System.out.println("Execution Time: " + durationInSeconds + " seconds");
                    System.out.println("Number of moves: " + path.size());
                    System.out.println("Memory Usage: " + (memoryUsed / 1024.0) + " KB ");
                    System.out.println(path);
                    System.out.println("--------------------------------------------------------------------------------------------\n");

                    List<GameState> solution = new LinkedList<>();
                    solution.add(gameState);
                    for (String move : path) {
                        gameState = gameState.playMove(move, true, initialBoard, view);
                        solution.add(gameState);
                    }

                    /*// Display initial board
                    view.display(solution.get(index).get_current_board_shallow(), gameState.get_size());
                    // Display:
                    ((ViewGUI) view).setMoveListener(move -> {
                        System.out.println("move = " + move);
                        if(move.equalsIgnoreCase("forward") && index + 1 < solution.size()) index++;
                        if(move.equalsIgnoreCase("backward") && index - 1 >= 0) index-- ;
                        System.out.println(index);
                        view.display(solution.get(index).get_current_board_shallow(), gameState.get_size());

                    });*/

                } catch (Exception e) {
                    logWriter.write("Algorithm: " + algorithmName + " failed: " + e.getMessage() + "\n");
                    logWriter.write("----------\n");
                    System.out.println("Algorithm: " + algorithmName + " failed: " + e.getMessage());
                }
            }
        }
    }

    private static File getFile(int i) throws IOException {
        String logFilePath = "Log/level" + i + ".txt";
        File logFile = new File(logFilePath);

        // Ensure the parent directory exists
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Create parent directories if they don't exist
        }

        // Ensure the log file exists
        if (!logFile.exists()) {
            logFile.createNewFile(); // Create the file if it doesn't exist
        }
        return logFile;
    }

    private static String[][] takeBoard(BufferedReader reader, int size) throws IOException {
        String[][] grid = new String[size][size];

        for (int i = 0; i < size; i++) {
            String input = reader.readLine();
            if (input == null) {
                throw new InputMismatchException("Unexpected end of file. Ensure the file contains " + size + " rows.");
            }
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

        return grid;
    }
}
