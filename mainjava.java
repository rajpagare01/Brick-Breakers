import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 14;
    
    private Timer timer;
    private int delay = 10;

    private int playerX = 210;
    private int ballPosX = 120, ballPosY = 350;
    private int ballDirX = -2, ballDirY = -4;

    private BrickGenerator map;
    public GamePanel() {
        map = new BrickGenerator(2, 7); 
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 800, 600);

       
        map.draw((Graphics2D) g);

        
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 800, 3);
        g.fillRect(0, 0, 3, 600);
        g.fillRect(800, 0, 3, 600);

       
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 650, 30);

        // Paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 10);

        // Ball
        g.setColor(Color.RED);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // Game Over
        if (ballPosY > 570) {
            play = false;
            timer.stop();
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over! Score: " + score, 250, 300);
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // Win Condition
        if (totalBricks == 0) {
            play = false;
            timer.stop();
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("You Won! Score: " + score, 250, 300);
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }


    public void actionPerformed(ActionEvent e) {
        if (play) {
            ballPosX += ballDirX;
            ballPosY += ballDirY;

            
            if (ballPosX < 0 || ballPosX > 770) {
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0) {
                ballDirY = -ballDirY;
            }

            // Ball collision with paddle
            if (new Rectangle(ballPosX, ballPosY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 10))) {
                ballDirY = -ballDirY;
            }

            // Ball collision with bricks
            A:  for (int i = 0; i < map.bricks.length; i++) {
                for (int j = 0; j < map.bricks[0].length; j++) {
                    if (map.bricks[i][j] > 0) {
                        int brickX = j * 100 + 50;
                        int brickY = i * 30 + 50;
                        int brickWidth = 80;
                        int brickHeight = 20;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickWidth) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }
                            break A;
                        }
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 700) {
                playerX = 700;
            } else {
                playerX += 20;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 10) {
                playerX = 10;
            } else {
                playerX -= 20;
            }
        } 
        
        

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballDirX = -2;
                ballDirY = -4;
                playerX = 210;
                score = 0;
                totalBricks = 14;
                map = new BrickGenerator(2, 7);
                repaint();
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}

class BrickGenerator {
    public int[][] bricks;
    public int brickWidth, brickHeight;

    public BrickGenerator(int row, int col) {
        bricks = new int[row][col];
        for (int[] brickRow : bricks) {
            java.util.Arrays.fill(brickRow, 1); // 1 = Brick exists
        }
        brickWidth = 80;
        brickHeight = 20;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if (bricks[i][j] > 0) {
                    g.setColor(Color.CYAN);
                    g.fillRect(j * 100 + 50, i * 30 + 50, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j * 100 + 50, i * 30 + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        bricks[row][col] = value;
    }
}

public class mainjava{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Brick Breaker Advanced");
        GamePanel gamePanel = new GamePanel();

        frame.setBounds(10, 10, 800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setVisible(true);
    }
}