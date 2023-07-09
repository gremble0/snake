package snake;

class Run implements Runnable {
    private Snake snake;
    private int interval = 1000;

    public Run(Snake snake) {
        this.snake = snake;
    }

    @Override
    public void run() {
        try {
            while (snake.updateGame()) {
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            System.exit(1);
        } catch (IndexOutOfBoundsException e) {
            // continueGame = false;
        } catch (NullPointerException e) {
            // continueGame = false;
        }
    }
}
