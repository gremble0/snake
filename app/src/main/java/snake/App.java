package snake;

public class App {
    public static void main(String[] args) {
        Snake snake = new Snake(12, 12, 10);
        Thread run = new Thread(new Run(snake));
        run.start();
    }
}
