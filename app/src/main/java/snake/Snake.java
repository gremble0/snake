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
    private String direction = "right";
    private int headPos = 76;
    private Queue<JLabel> snake = new LinkedList<>();
    private JFrame gameWindow = new JFrame("Snake");
    private JPanel gameMain = new JPanel(new GridBagLayout());
    private JPanel gamePanel = new JPanel(new GridLayout(12, 12, -1, -1)); 

    public Snake() {
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
            String direction;
            public ChangeDirection(String direction) {
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
                    changeDirection("up");
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    changeDirection("right");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    changeDirection("down");
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    changeDirection("left");
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
        upButton.addActionListener(new ChangeDirection("up"));

        JButton leftButton = new JButton("Left");
        gbc.gridx = 0; gbc.gridy = 1;
        header.add(leftButton, gbc);
        leftButton.addActionListener(new ChangeDirection("left"));

        JButton rightButton = new JButton("Left");
        gbc.gridx = 2;
        header.add(rightButton, gbc);
        rightButton.addActionListener(new ChangeDirection("right"));

        JButton downButton = new JButton("Down");
        gbc.gridx = 1; gbc.gridy = 2;
        header.add(downButton, gbc);
        downButton.addActionListener(new ChangeDirection("down"));
        

        /**
         * INITIALISING GRID
         */
        ArrayList<Integer> apples = new ArrayList<>(); // liste av indekser der det skal vaere apples

        for (int i = 0; i < 10; i++) { // i < 10 for aa starte med 10 apples
            int applePos = (int)Math.floor(Math.random()*(143-0+1)+0); // 144 ruter aa velge mellom siden 12*12

            if (apples.contains(applePos) || applePos == 76) {
                i -= 1;
                continue; // hvis det tilfeldige tallet allerede har blitt generert, eller tallet er der hvor snaken skal starte (boks 6,6), lag et nytt tall
            }
            apples.add(applePos);
        }

        for (int i = 0; i < 144; i++) {
            JLabel boks;

            if (apples.contains(i)) { // hvis vi er paa en boks der det er generert en skatt...
                boks = new JLabel("$");
                boks.setForeground(Color.red);
                boks.setBackground(Color.white);
            } else if (i == 76) { // der snaken skal starte
                boks = new JLabel("o");
                boks.setBackground(Color.green);
                snake.add(boks);
            } else {
                boks = new JLabel(" ");
                boks.setBackground(Color.white);
            }

            boks.setOpaque(true);
            boks.setFont(new Font("Arial", Font.BOLD, 20));
            boks.setHorizontalAlignment(SwingConstants.CENTER);
            boks.setBorder(BorderFactory.createLineBorder(Color.black)); // lager en svart ramme rundt hver boks i griden
            gamePanel.add(boks);
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

    public void changeDirection(String newDirection) {
        direction = newDirection;
    }

    public void move(JLabel head) {
        JLabel nextBox;

        if (direction == "up") {
            if (headPos <= 11) { 
                gamePanel = null; // fant ingen bedre maate aa stoppe programmet uten System.exit(), hvilket umiddelbart lukker guien
                return;
            }
            headPos -= 12;
            nextBox = (JLabel) gamePanel.getComponent(headPos);
        } else if (direction == "right") {
            if ((headPos + 1) % 12 == 0) {
                gamePanel = null;
                return;
            }
            headPos += 1;
            nextBox = (JLabel) gamePanel.getComponent(headPos);
        } else if (direction == "down") {
            if (headPos >= 132) { // hvis headPos er stoerre enn eller lik foerste rute paa nederste rad...
                gamePanel = null;
                return;
            }
            headPos += 12;
            nextBox = (JLabel) gamePanel.getComponent(headPos);
        } else {
            if (headPos % 12 == 0) {
                gamePanel = null;
                return;
            }
            headPos -= 1;
            nextBox = (JLabel) gamePanel.getComponent(headPos);
        }

        if (nextBox.getText() == "+") {
            gamePanel = null;
            return;
        }

        if (nextBox.getText().equals("$")) { // hvis neste boks har en skatt...
            snake.offer(nextBox);
            for (JLabel snakeBoks : snake) {
                drawInBox(snakeBoks, "+", Color.green, Color.black);
            }
            drawInBox(nextBox, "o", Color.green, Color.black);

            int tilfeldigInt = (int)Math.floor(Math.random()*(143-0+1)+0);
            JLabel boks = (JLabel)gamePanel.getComponent(tilfeldigInt);
            while (boks.getText() == "o" || boks.getText() == "+" || boks.getText() == "$") { // repeter til generert boks ikke er en del av snaken eller allerede en skatt
                // vil gaa i uendelig loekke hvis snaken blir veldig lang, men ikke et reelt problem
                tilfeldigInt = (int)Math.floor(Math.random()*(143-0+1)+0);
                boks = (JLabel)gamePanel.getComponent(tilfeldigInt);
            }

            drawInBox(boks, "$", Color.white, Color.red);
            return;
        } 

        snake.offer(nextBox);
        for (JLabel snakeBoks : snake) {
            drawInBox(snakeBoks, "+", Color.green, Color.black);
        }

        drawInBox(nextBox, "o", Color.green, Color.black);
        drawInBox(snake.remove(), "", Color.white, Color.white);
    }

    public void drawInBox(JLabel boks, String text, Color bakgrunn, Color forgrunn) {
        boks.setText(text);
        boks.setBackground(bakgrunn);
        boks.setForeground(forgrunn);
    }

    public JLabel getHeadRef() {
        return snake.peek();
    }
}
