package snake;

public class App {
    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame(12, 12, 10, 6, 6);
        Thread run = new Thread(new Run(snakeGame));
        run.start();
    }
}
