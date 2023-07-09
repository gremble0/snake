package snake;

public class App {
    public static void main(String[] args) {
        Snake snake = new Snake();
        Thread run = new Thread(new Run(snake));
        run.start();
    }
}
