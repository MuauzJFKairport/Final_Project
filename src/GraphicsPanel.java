import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private BufferedImage area;
    private Player player;
    private boolean[] pressedKeys;
    private ArrayList<Rectangle> rectangles;
    private JButton restartButton;
    private Rectangle parkingSpot = new Rectangle(220, 410, 92, 20);
    private Rectangle parkingSpot_two = new Rectangle(220, 290, 92, 20);
    private Rectangle parkingSpot_three = new Rectangle(255, 330, 20, 75);
    private boolean parkingSpotVisible = false;
    private boolean parkingSpotTwoVisible = false;
    private boolean parkingSpotThreeVisible = false;

    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/parking-lot.png"));
            area = ImageIO.read(new File("src/yellow.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player("src/whitecar.png");
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above

        // Initialize rectangles with proper dimensions
        rectangles = new ArrayList<>();
        rectangles.add(new Rectangle(86, 71, 139, 49));
        rectangles.add(new Rectangle(334, 63, 176, 94));
        rectangles.add(new Rectangle(83, 796, 136, 53));
        rectangles.add(new Rectangle(339, 297, 174, 93));
        rectangles.add(new Rectangle(81, 482, 140, 190));
        rectangles.add(new Rectangle(239, 541, 140, 59));
        rectangles.add(new Rectangle(344, 514, 168, 134));
        rectangles.add(new Rectangle(86, 765, 433, 92));
        rectangles.add(new Rectangle(771, 378, 145, 40));
        rectangles.add(new Rectangle(921, 55, 70, 89));
        rectangles.add(new Rectangle(1051, 173, 145, 568));
        rectangles.add(new Rectangle(925, 758, 72, 95));

        // Initialize restart button
        restartButton = new JButton("Restart");
        restartButton.setBounds(20, 60, 100, 30);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
                requestFocusInWindow(); // Request focus back to the panel
            }
        });
        setLayout(null); // Use absolute positioning for button
        add(restartButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.drawImage(area, 220, 268, null);

        // Draw parking spots if they are visible
        if (parkingSpotVisible) {
            g.drawRect(parkingSpot.x, parkingSpot.y, parkingSpot.width, parkingSpot.height);
        }
        if (parkingSpotTwoVisible) {
            g.drawRect(parkingSpot_two.x, parkingSpot_two.y, parkingSpot_two.width, parkingSpot_two.height);
        }
        if (parkingSpotThreeVisible) {
            g.drawRect(parkingSpot_three.x, parkingSpot_three.y, parkingSpot_three.width, parkingSpot_three.height);
        }

        // Rotate and draw player
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(player.getxCoord(), player.getyCoord());
        transform.rotate(player.getAngle(), player.getPlayerImage().getWidth() / 2.0, player.getPlayerImage().getHeight() / 2.0);
        g2d.setTransform(transform);
        g2d.drawImage(player.getPlayerImage(), 0, 0, null);

        // Restore the original transform for drawing other components
        g2d.setTransform(originalTransform);

        // Draw and check for collisions with rectangles
        for (Rectangle rect : rectangles) {
            if (player.playerRect().intersects(rect)) {
                player.subtractScore();
                restrictPlayerMovement(rect);
            }
        }
        if (player.playerRect().intersects(parkingSpot) && player.playerRect().intersects(parkingSpot_two) && player.playerRect().intersects(parkingSpot_three)) {
            g.setColor(Color.RED);
            g.setFont(new Font("Courier New", Font.BOLD, 50));
            g.drawString("You Win!", 700, 300);
            g.setFont(new Font("Courier New", Font.BOLD, 35));
            g.drawString("Score: " + player.getScore(), 710, 340);


        }

        // Draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Score: " + player.getScore(), 30, 40);

        // Handle key presses for movement and rotation
        if (pressedKeys[65]) {
            if (pressedKeys[87] || pressedKeys[83]) {
                player.rotateLeft();
            }
        }
        if (pressedKeys[68]) {
            if (pressedKeys[87] || pressedKeys[83]) {
                player.rotateRight();
            }
        }
        if (pressedKeys[87]) {
            player.moveForward();
        }
        if (pressedKeys[83]) {
            player.moveBackward();
        }
        repaint();
    }

    // Method to adjust player's position upon collision with a rectangle
    private void restrictPlayerMovement(Rectangle rect) {
        Shape playerShape = player.playerRect();
        Rectangle playerRect = playerShape.getBounds();

        // Calculate overlap
        double dx = 0, dy = 0;

        // Determine the direction of overlap
        if (playerRect.x < rect.x) {
            dx = playerRect.x + playerRect.width - rect.x;
        } else {
            dx = rect.x + rect.width - playerRect.x;
        }

        if (playerRect.y < rect.y) {
            dy = playerRect.y + playerRect.height - rect.y;
        } else {
            dy = rect.y + rect.height - playerRect.y;
        }

        // Adjust player position to prevent overlap
        if (Math.abs(dx) < Math.abs(dy)) {
            // Adjust player position horizontally
            if (playerRect.x < rect.x) {
                player.setxCoord(player.getxCoord() - dx);
            } else {
                player.setxCoord(player.getxCoord() + dx);
            }
        } else {
            // Adjust player position vertically
            if (playerRect.y < rect.y) {
                player.setyCoord(player.getyCoord() - dy);
            } else {
                player.setyCoord(player.getyCoord() + dy);
            }
        }
    }

    // Method to restart the game
    private void restartGame() {
        player = new Player("src/whitecar.png"); // Reset the player
        pressedKeys = new boolean[128]; // Reset key presses
        repaint(); // Repaint the panel
        requestFocusInWindow(); // Request focus back to the panel
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) {
    } // unimplemented

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
}
