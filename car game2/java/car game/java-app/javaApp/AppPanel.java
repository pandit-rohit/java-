import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AppPanel extends JPanel {

    BufferedImage carImage;
    BufferedImage carImage1;
    BufferedImage carImage2;
    BufferedImage carImage3;
    Timer timer;
    int playerCarX = 250;
    int playerCarY = 450;
    int obstacleCarX; // Random X position for obstacle car
    int obstacleCarY = 0; // Starts from the top of the screen
    boolean isGameRunning = false;
    boolean gameOver = false;
    int score = 0; // Score variable
    Random rand = new Random(); // For random obstacle position
    // Road parameters
    int roadX = 100;  // Left side of the road
    int roadWidth = 300; // Width of the road
    int laneWidth = 20;  // Width of lane markers

    AppPanel() {
        setSize(500, 700);
        loadCarImage();
        initGameLoop();
        addKeyboardControls();
        setFocusable(true);
        resetObstacleCar(); // Set initial position for obstacle car
    }

    void loadCarImage() {
        try {
            carImage = ImageIO.read(AppPanel.class.getResource("B.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            carImage1 = ImageIO.read(AppPanel.class.getResource("A.png"));
            carImage2 = ImageIO.read(AppPanel.class.getResource("A.png"));
            carImage3 = ImageIO.read(AppPanel.class.getResource("A.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics pen) {
        super.paintComponent(pen);
        // Display start or restart message
        if (!isGameRunning) {
            pen.setColor(Color.black);
            if (gameOver) {
                pen.drawString("Collision! Press ENTER to Restart", 150, 250);
            } else {
                pen.drawString("Press ENTER to Start", 200, 250);
            }
            return;
        }
        // Draw the road
        drawRoad(pen);
        // Draw the player's car
        pen.drawImage(carImage, playerCarX, playerCarY, 150, 200, null);
        // Draw the obstacle car coming from the top
        pen.drawImage(carImage1, obstacleCarX, obstacleCarY, 150, 200, null);
        // Move the obstacle car downward
        obstacleCarY += 15;
        // Reset the obstacle car to the top when it goes off-screen
        if (obstacleCarY > getHeight()) {
            resetObstacleCar();
            score += 10; // Increment score when the obstacle car crosses the screen
        }
        // Check for collision
        if (checkCollision()) {
            pen.setColor(Color.yellow);
            pen.drawString("GAME OVER", 200, 350);
            isGameRunning = false; // Pause game on collision
            gameOver = true;
            timer.stop();
        }
        // Display the score
        pen.setColor(Color.red);
        pen.drawString("Score: " + score, 10, 20);
    }

    void drawRoad(Graphics pen) {
        // Draw road boundaries
        pen.setColor(Color.black);
        pen.fillRect(15, 0, 460, getHeight());
        // Draw the lane markers (dashed lines)
        pen.setColor(Color.white);
        for (int i = 0; i < getHeight(); i += 80) {
            pen.fillRect(15 + 460 / 2 - laneWidth / 2, i, laneWidth, 60);
        }
    }

    boolean checkCollision() {
        // Create bounding rectangles for player car and obstacle car
        Rectangle playerCarRect = new Rectangle(playerCarX, playerCarY, 60, 100);
        Rectangle obstacleCarRect = new Rectangle(obstacleCarX, obstacleCarY, 80, 130);
        return playerCarRect.intersects(obstacleCarRect);
    }

    void initGameLoop() {
        timer = new Timer(50, (a) -> {
            if (isGameRunning) {
                repaint();
            }
        });
        timer.start();
    }

    void resetGame() {
        // Reset player and obstacle car positions
        playerCarX = 200;
        playerCarY = 460;
        resetObstacleCar();
        gameOver = false;
        score = 0; // Reset score
    }

    void resetObstacleCar() {
        // Randomize X position and reset Y position of the obstacle car
        obstacleCarX = rand.nextInt(getWidth() - 100); // Prevents car from going out of bounds
        obstacleCarY = -150; // Start above the visible screen
    }

    void addKeyboardControls() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Start or restart the game on enter press
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!isGameRunning) {
                        if (gameOver) {
                            resetGame(); // Reset positions if game is over
                        }
                        isGameRunning = true;
                        timer.start();
                    }
                }
                // Car movement only if the game is running
                if (isGameRunning) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerCarX <= 500 - 160) {
                        playerCarX += 10;
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT && playerCarX >= 0) {
                        playerCarX -= 10;
                    } else if (e.getKeyCode() == KeyEvent.VK_UP && playerCarY >= 0) {
                        playerCarY -= 10;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN && playerCarY <= 700 - 250) {
                        playerCarY += 10;
                    }
                }
            }
        });
    }
}
