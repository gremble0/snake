package snake;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

public class Snake {
    private final int BOARD_COLS;
    private final int BOARD_ROWS;

    private Queue<JLabel> snake = new LinkedList<>();
    private Head head = new Head(6, 6);
    private Direction direction = Direction.RIGHT;
    private int score = 0;

    private JFrame gameWindow = new JFrame("Snake");
    private JPanel gameMain = new JPanel(new GridBagLayout()); 
    private JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);;
    private JPanel gamePanel;

    public Snake(int rows, int cols, int numApples) {
        BOARD_ROWS = rows;
        BOARD_COLS = cols;
        gamePanel = new JPanel(new GridLayout(BOARD_COLS, BOARD_ROWS, -1, -1)); ;

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

        JPanel header = new JPanel(new GridBagLayout());

        /** 
         * BUTTONS TO CHANGE DIRECTION
         */
        class ChangeDirection implements ActionListener {
            Direction direction;
            public ChangeDirection(Direction direction) {
                this.direction = direction;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                changeDirection(direction);
            }
        }
        
        class ChangeDirectionArrowkey implements KeyListener {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    changeDirection(Direction.UP);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    changeDirection(Direction.RIGHT);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    changeDirection(Direction.DOWN);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    changeDirection(Direction.LEFT);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        }

        JButton upButton = new JButton("Up");
        gbc.gridx = 1;
        header.add(upButton, gbc);
        upButton.addActionListener(new ChangeDirection(Direction.UP));

        JButton rightButton = new JButton("Right");
        gbc.gridy = 1; gbc.gridx = 2;
        header.add(rightButton, gbc);
        rightButton.addActionListener(new ChangeDirection(Direction.RIGHT));

        JButton leftButton = new JButton("Left");
        gbc.gridx = 0;
        header.add(leftButton, gbc);
        leftButton.addActionListener(new ChangeDirection(Direction.LEFT));

        JButton downButton = new JButton("Down");
        gbc.gridx = 1; gbc.gridy = 2;
        header.add(downButton, gbc);
        downButton.addActionListener(new ChangeDirection(Direction.DOWN));

        // Score label
        gbc.gridy = 1;
        header.add(scoreLabel, gbc);
        
        /**
         * INITIALISING GRID
         */

        int apples[][] = new int[BOARD_ROWS][BOARD_COLS];

        for (int i = 0; i < numApples; i++) {
            int appleX = (int) Math.floor(Math.random() * BOARD_ROWS);
            int appleY = (int) Math.floor(Math.random() * BOARD_COLS);

            // If the indexes have already been generated, try again
            if (apples[appleX][appleY] == 1) {
                i -= 1;
                continue;
            }

            apples[appleX][appleY] = 1;
        }

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                JLabel box;

                if (apples[i][j] == 1) {
                    // If we're on a box with an apple
                    box = new JLabel("$");
                    box.setForeground(Color.red);
                    box.setBackground(Color.white);
                } else if (i == 6 && j == 6) {
                    // Where the snake is starting
                    box = new JLabel("o");
                    box.setBackground(Color.green);
                    snake.add(box);
                } else {
                    // Empty boxes
                    box = new JLabel(" ");
                    box.setBackground(Color.white);
                }

                box.setOpaque(true);
                box.setFont(new Font("Arial", Font.BOLD, 20));
                box.setHorizontalAlignment(SwingConstants.CENTER);
                box.setBorder(BorderFactory.createLineBorder(Color.black));
                gamePanel.add(box);
            }
        }

        /**
         * DISPLAYING GUI
         */

        gbc.gridy = 0;
        header.setPreferredSize(new Dimension(500,100));
        gameMain.add(header, gbc);
        gbc.gridy = 1;
        gamePanel.setPreferredSize(new Dimension(500,500));
        gameMain.add(gamePanel, gbc);

        gameWindow.add(gameMain);
        gameWindow.pack();
        gameWindow.setVisible(true);

        gameWindow.setFocusable(true);
        gameWindow.addKeyListener(new ChangeDirectionArrowkey());
    }

    private void changeDirection(Direction newDirection) {
        direction = newDirection;
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

    public boolean continueGame() {
        int[] headPos = head.move(direction, BOARD_ROWS, BOARD_COLS);

        if (headPos == null) {
            // We have eaten our tail
            return false;
        }

        JLabel nextBox = getBox(headPos[0], headPos[1]);
        if (nextBox.getText() == "+") {
            // We have moved out of bounds
            return false;
        }
        
        if (nextBox.getText().equals("$")) {
            // We have eaten an apple
            snake.offer(nextBox);
            for (JLabel snakeBody : snake) {
                drawInBox(snakeBody, "+", Color.green, Color.black);
            }
            drawInBox(nextBox, "o", Color.green, Color.black);
            spawnApple();
            drawInBox(scoreLabel, String.format("Score: %s", ++score), Color.green, Color.black);

            return true;
        } 

        snake.offer(nextBox);
        for (JLabel snakeBody : snake) {
            drawInBox(snakeBody, "+", Color.green, Color.black);
        }

        drawInBox(nextBox, "o", Color.green, Color.black);
        drawInBox(snake.remove(), "", Color.white, Color.white);

        return true;
    }
}
