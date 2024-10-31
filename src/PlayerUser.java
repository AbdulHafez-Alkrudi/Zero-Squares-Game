import java.util.Scanner;

public class PlayerUser implements Player{
    @Override
    public String get_move() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your move:");
        return in.nextLine();
    }
}
