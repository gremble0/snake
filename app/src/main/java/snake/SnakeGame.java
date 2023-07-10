package snake;

import java.awt.*;

import javax.swing.*;

public class SnakeGame {
    private final int BOARD_COLS;
    private final int BOARD_ROWS;

    private Snake snake;
    private int numApples;
    private int score = 0;
    private int startX;
    private int startY;
    private JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);;

    private JFrame gameWindow = new JFrame("Snake");
    private JPanel gameMain = new JPanel(new GridBagLayout()); 
    private JPanel gamePanel;

    public SnakeGame(int rows, int cols, int numApples, int startX, int startY) {
        this.BOARD_ROWS = rows;
        this.BOARD_COLS = cols;
        this.numApples = numApples;
        this.snake = new Snake(startX, startY);
        this.startX = startX;
        this.startY = startY;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel buttonPanel = snake.addEventListeners(gameWindow, gbc);

        // Score label
        gbc.gridy = 1;
        buttonPanel.add(scoreLabel, gbc);
        
        initializeBoard();

        // Displaying GUI
        gbc.gridy = 0;
        buttonPanel.setPreferredSize(new Dimension(500,100));
        gameMain.add(buttonPanel, gbc);
        gbc.gridy = 1;
        gamePanel.setPreferredSize(new Dimension(500,500));
        gameMain.add(gamePanel, gbc);

        gameWindow.add(gameMain);
        gameWindow.pack();
        gameWindow.setVisible(true);

        gameWindow.setFocusable(true);
    }

    private void drawInBox(JLabel box, String text, Color background, Color foreground) {
        box.setText(text);
        box.setBackground(background);
        box.setForeground(foreground);
    }

    private JLabel getBox(int boxX, int boxY) {
        // To get list index required by getComponent we take the row headPos[0] 
        // and add it to the column headPos[1] multiplied by the amount of columns.
        return (JLabel) gamePanel.getComponent(boxX + boxY * BOARD_COLS);
    }

    private void spawnApple() {
        int appleX = (int)Math.floor(Math.random() * BOARD_ROWS);
        int appleY = (int)Math.floor(Math.random() * BOARD_COLS);

        JLabel box = getBox(appleX, appleY);
        String boxText = box.getText();
        while (boxText == "o" || boxText == "+" || boxText == "$") {
            // Will go in an infinite loop if there are no more available boxes
            appleX = (int)Math.floor(Math.random() * BOARD_ROWS);
            appleY = (int)Math.floor(Math.random() * BOARD_COLS);

            box = getBox(appleX, appleY);
            boxText = box.getText();
        }

        drawInBox(box, "$", Color.white, Color.red);
    }

    private void initializeBoard() {
        gamePanel = new JPanel(new GridLayout(BOARD_COLS, BOARD_ROWS, -1, -1)); ;

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                JLabel box = new JLabel(" ");

                box.setBackground(Color.white);
                box.setOpaque(true);
                box.setFont(new Font("Arial", Font.BOLD, 20));
                box.setHorizontalAlignment(SwingConstants.CENTER);
                box.setBorder(BorderFactory.createLineBorder(Color.black));

                gamePanel.add(box);
            }
        }

        JLabel start = getBox(startX, startY);
        drawInBox(start, "o", Color.green, Color.black);
        snake.snake.add(start);

        for (int i = 0; i < numApples; i++) spawnApple();
    }

    public boolean continueGame() {
        int[] headPos = snake.move();
        int headX = headPos[0], headY = headPos[1];

        if (headY < 0 || headY >= BOARD_ROWS || headX < 0 || headX >= BOARD_COLS) { 
            return false;
        }

        JLabel nextBox = getBox(headX, headY);
        if (nextBox.getText() == "+") {
            // We have eaten our tail
            return false;
        }
        
        if (nextBox.getText().equals("$")) {
            // We have eaten an apple
            drawInBox(scoreLabel, String.format("Score: %s", ++score), Color.green, Color.black);
            spawnApple();
        } else {
            drawInBox(snake.snake.remove(), "", Color.white, Color.white);
        }

        for (JLabel snakeBody : snake.snake) {
            drawInBox(snakeBody, "+", Color.green, Color.black);
        }

        drawInBox(nextBox, "o", Color.green, Color.black);
        snake.snake.add(nextBox);

        return true;
    }
}
