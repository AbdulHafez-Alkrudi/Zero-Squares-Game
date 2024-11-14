import java.util.List;

public interface Algorithm {
    abstract public List<String> run(GameState gameState , View view);
}
