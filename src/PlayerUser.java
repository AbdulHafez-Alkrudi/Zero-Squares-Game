import java.util.InputMismatchException;
import java.util.Scanner;

public class PlayerUser implements Player{
    @Override
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
    private boolean checkMove(String move)
    {
        return switch (move) {
            case "up" -> true;
            case "down" -> true;
            case "left" -> true;
            case "right" -> true;
            case "restart" -> true;
            default -> throw new InputMismatchException();
        };
    }

}
