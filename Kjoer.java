import javax.swing.*;

class Kjoer implements Runnable {
    private boolean continueGame = true;
    private Snake snake;
    private long interval = 500;

    public Kjoer(Snake snake) {
        this.snake = snake;
    }

    @Override
    public void run() {
        try {
            while (continueGame) {
                Thread.sleep(interval);
                JLabel headRef = snake.hentHodeRef();
                snake.flytt(headRef);
                if (interval > 100) {
                    interval -= 50;
                }
            }
        } catch (InterruptedException e) {
            System.exit(1);
        } catch (IndexOutOfBoundsException e) { // hvis vi proever aa flytte oss mens vi er paa kanten av brettet
            continueGame = false;
        } catch (NullPointerException e) {
            continueGame = false;
        }
    }
}
