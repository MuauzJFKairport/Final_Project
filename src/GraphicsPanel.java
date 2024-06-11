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
    private ArrayList<Coin> coins;
    private Rectangle five;
    private Rectangle six;
    private Rectangle seven;
    private Rectangle eight;
    private Rectangle nine;
    private Rectangle ten;
    private Rectangle eleven;
    private Rectangle twelve;
    Rectangle parkingSpot = new Rectangle(220, 410, 92, 20);

    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/parking-lot.png"));
            area = ImageIO.read(new File("src/yellow.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player("src/whitecar.png");
        coins = new ArrayList<>();
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
    }

    private void handleCollisions() {
        Rectangle playerRect = player.playerRect();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // just do this
        g.drawImage(background, 0, 0, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(area, 220,268,null);
        g.drawRect(parkingSpot.x, parkingSpot.y, parkingSpot.width, parkingSpot.height);
        // Rotate and draw player
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(player.getxCoord(), player.getyCoord());
        transform.rotate(player.getAngle(), player.getPlayerImage().getWidth() / 2.0, player.getPlayerImage().getHeight() / 2.0);
        g2d.setTransform(transform);
        g2d.drawImage(player.getPlayerImage(), 0, 0, null);
        g2d.setTransform(originalTransform);
        g.setColor(Color.red);
        g.drawRect(parkingSpot.x, parkingSpot.y, parkingSpot.width, parkingSpot.height);
        five = new Rectangle(81 , 482 , 140, 190);
        six = new Rectangle(239 , 541 , 140, 59);
        seven = new Rectangle(344 , 514 , 168, 134);
        eight= new Rectangle(86 , 765 , 433, 92);
        nine = new Rectangle(771 , 378 , 145, 40);
        ten = new Rectangle(921 , 55 , 70, 89 );
        eleven = new Rectangle(1051 , 173 , 145, 568);
        twelve = new Rectangle(925 , 758 , 72, 95);

        // This loop does two things: it draws each Coin that gets placed with mouse clicks,
        // and it also checks if the player has "intersected" (collided with) the Coin, and if so,
        // the score goes up and the Coin is removed from the arraylist
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            g.drawImage(coin.getImage(), coin.getxCoord(), coin.getyCoord(), null); // draw Coin
            if (player.playerRect().intersects(coin.coinRect())) { // check for collision
                player.collectCoin();
                coins.remove(i);
                i--;
            }
        }
        if (
                player.playerRect().intersects(five) ||
                player.playerRect().intersects(six) ||
                player.playerRect().intersects(seven) ||
                player.playerRect().intersects(eight) ||
                player.playerRect().intersects(nine) ||
                player.playerRect().intersects(ten) ||
                player.playerRect().intersects(eleven) ||
                player.playerRect().intersects(twelve)) {
            player.subtractScore();

            // Check if the player is moving in the direction of collision and prevent movement
            if (pressedKeys[87]) { // W
                pressedKeys[87] = false;
                player.moveBackward();// Stop moving forward
            }
            if (pressedKeys[83]) { // S
                pressedKeys[83] = false;
                player.moveForward();// Stop moving backward
            }
        }

        if (player.playerRect().intersects(parkingSpot)) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("You Win!", 700, 300);
        }


        // Draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Score: " + player.getScore(), 20, 40);
        



        // Handle key presses for movement and rotation
        if (pressedKeys[65]) {
            if (pressedKeys[87] || pressedKeys[83]) {// A
                player.rotateLeft();
            }
        }
        if (pressedKeys[68]) {
            if (pressedKeys[87] || pressedKeys[83]) {// D
                player.rotateRight();
            }
        }
        if (pressedKeys[87]) { // W
            player.moveForward();
        }
        if (pressedKeys[83]) { // S
            player.moveBackward();
        }
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }  // unimplemented; if you move your mouse while clicking,
    // this method isn't called, so mouseReleased is best

    public void mousePressed(MouseEvent e) { } // unimplemented

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
            Coin coin = new Coin(mouseClickLocation.x, mouseClickLocation.y);
            coins.add(coin);
        }
    }

    public void mouseEntered(MouseEvent e) { } // unimplemented

    public void mouseExited(MouseEvent e) { } // unimplemented
}

    public void mouseExited(MouseEvent e) { } // unimplemented
}
