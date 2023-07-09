package snake;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

public class Snake {
    public final int BOARD_COLS = 12;
    public final int BOARD_ROWS = 12;

    public Head head;
    public Direction direction;

    public Queue<JLabel> snake;
    private JFrame gameWindow;
    private JPanel gameMain;
    private JPanel gamePanel;

    public Snake() {
        head = new Head(6, 6);
        direction = Direction.RIGHT;
        snake = new LinkedList<>();
        gameWindow = new JFrame("Snake");
        gameMain = new JPanel(new GridBagLayout()); 
        gamePanel = new JPanel(new GridLayout(BOARD_COLS, BOARD_ROWS, -1, -1)); 

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(1);
        }
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
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
        

        /**
         * INITIALISING GRID
         */
        ArrayList<ArrayList<Integer>> apples = new ArrayList<>();
        for (int i = 0; i < BOARD_ROWS; i++) {
            apples.add(new ArrayList<>());
        }

        for (int i = 0; i < 10; i++) { // Make 10 apples
            int appleX = (int)Math.floor(Math.random() * BOARD_ROWS);
            int appleY = (int)Math.floor(Math.random() * BOARD_COLS);

            // If the indexes have already been generated, try again
            if (apples.size() != 0 && apples.get(appleY).contains(appleX)) {
                i -= 1;
                continue;
            }

            apples.get(appleY).add(appleX);
        }

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                JLabel box;

                if (apples.get(i).contains(j)) {
                    // If we're on a box with an apple
                    box = new JLabel("$");
                    box.setForeground(Color.red);
                    box.setBackground(Color.white);
                } else if (i == 6 && j == 6) {
                    // Where the snake is starting
                    box = new JLabel("o");
                    box.setBackground(Color.green);
                    // snake.add(box);
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

    public void changeDirection(Direction newDirection) {
        direction = newDirection;
    }

    public void drawInBox(JLabel box, String text, Color background, Color foreground) {
        box.setText(text);
        box.setBackground(background);
        box.setForeground(foreground);
    }

    public JLabel getBox(int[] cords) {
        // To get list index required by getComponent we take the row headPos[0] 
        // and add it to the column headPos[1] multiplied by the amount of columns.
        return (JLabel) gamePanel.getComponent(cords[0] + cords[1] * BOARD_COLS);
    }

    public void spawnApple() {
        int appleX = (int)Math.floor(Math.random() * BOARD_ROWS);
        int appleY = (int)Math.floor(Math.random() * BOARD_COLS);
        int[] cords = { appleX, appleY };

        JLabel box = getBox(cords);
        String boxText = box.getText();
        while (boxText == "o" || boxText == "+" || boxText == "$") {
            // Will go in an infinite loop if the snake gets very long
             appleX = (int)Math.floor(Math.random() * BOARD_ROWS);
             appleY = (int)Math.floor(Math.random() * BOARD_COLS);
             cords[0] = appleX;
             cords[1] = appleY;

            box = getBox(cords);
            boxText = box.getText();
        }

        drawInBox(box, "$", Color.white, Color.red);
    }

    public boolean updateGame() {
        int[] headPos = head.move(direction, BOARD_ROWS, BOARD_COLS);
        JLabel nextBox = getBox(headPos);

        if (nextBox.getText() == "+") {
            // We have eaten our tail
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

            return true;
        } 

        snake.offer(nextBox);
        for (JLabel slangeBoks : snake) {
            drawInBox(slangeBoks, "+", Color.green, Color.black);
        }

        drawInBox(nextBox, "o", Color.green, Color.black);
        drawInBox(snake.remove(), "", Color.white, Color.white);

        return true;
    }
}
