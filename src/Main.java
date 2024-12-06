import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        GameController game = new GameController();
        try {
            game.startGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}