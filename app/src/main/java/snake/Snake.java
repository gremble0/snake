package snake;

import java.util.LinkedList;
import java.util.Queue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import javax.swing.*;

public class Snake {
    public Queue<JLabel> snake = new LinkedList<JLabel>();
    private Direction direction = Direction.RIGHT;

    private int headX;
    private int headY;

    private enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    };

    public Snake(int headX, int headY) {
        // Start positions for snakes head
        this.headX = headX;
        this.headY = headY;
    }


    public JPanel addEventListeners(JFrame gameWindow, GridBagConstraints gbc) {
        // Buttons to change direction
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
        
        // Use arrow keys to change direction
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

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        JButton upButton = new JButton("Up");
        gbc.gridx = 1;
        buttonPanel.add(upButton, gbc);
        upButton.addActionListener(new ChangeDirection(Direction.UP));

        JButton rightButton = new JButton("Right");
        gbc.gridy = 1; gbc.gridx = 2;
        buttonPanel.add(rightButton, gbc);
        rightButton.addActionListener(new ChangeDirection(Direction.RIGHT));

        JButton leftButton = new JButton("Left");
        gbc.gridx = 0;
        buttonPanel.add(leftButton, gbc);
        leftButton.addActionListener(new ChangeDirection(Direction.LEFT));

        JButton downButton = new JButton("Down");
        gbc.gridx = 1; gbc.gridy = 2;
        buttonPanel.add(downButton, gbc);
        downButton.addActionListener(new ChangeDirection(Direction.DOWN));

        gameWindow.addKeyListener(new ChangeDirectionArrowkey());

        return buttonPanel;
    }

    private void changeDirection(Direction newDirection) {
        direction = newDirection;
    }

    public int[] move() {
        if (direction == Direction.UP) --headY;
        else if (direction == Direction.RIGHT) ++headX;
        else if (direction == Direction.DOWN) ++headY;
        else --headX;

        int[] headPos = { headX, headY };
        return headPos;
    }
}
