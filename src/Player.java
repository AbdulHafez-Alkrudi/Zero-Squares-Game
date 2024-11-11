import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Player {
    public String get_move() {
        Scanner in = new Scanner(System.in);
        String move = "" ;
        boolean valid_move = false;
        while(!valid_move)
        {
            try{
                valid_move = true ;
                System.out.println("Enter your move:");
                move = in.nextLine();
                checkMove(move);
            } catch(InputMismatchException e) {
                valid_move = false;
                System.out.print("An error occurred: " + e.getMessage());
            }
        }
        return move;
    }
    private static final Set<String> VALID_MOVES = Set.of("up", "down", "left", "right", "restart", "all");

    private void checkMove(String move) {
        if (!VALID_MOVES.contains(move.toLowerCase())) {
            throw new InputMismatchException("Invalid move: " + move);
        }
    }


}
