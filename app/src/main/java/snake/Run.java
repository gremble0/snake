package snake;

class Run implements Runnable {
    private SnakeGame snakeGame;
    private int interval = 750;

    public Run(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
    }

    @Override
    public void run() {
        try {
            while (snakeGame.continueGame()) {
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            System.exit(1);
        } 
    }
}
