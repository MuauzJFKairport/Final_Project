import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private final double MOVE_AMT = 3; // Increase movement amount
    private final double ROTATE_AMT = 0.027; // Reduce rotation amount
    private BufferedImage norm;
    private double xCoord, yCoord;
    private double angle = 0; // Rotation angle in radians
    private int score;

    public Player(String Img) {
        xCoord = 50; // starting position is (50, 435), right on top of ground
        yCoord = 435;
        score = 0;
        try {
            norm = ImageIO.read(new File(Img));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getxCoord() {
        return (int) xCoord;
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    public int getScore() {
        return score;
    }

    public void collectCoin() {
        score++;
    }

    public void rotateLeft() {
        angle -= ROTATE_AMT;
    }

    public void rotateRight() {
        angle += ROTATE_AMT;
    }

    public void moveForward() {
        xCoord += MOVE_AMT * Math.sin(angle);
        yCoord -= MOVE_AMT * Math.cos(angle);
    }

    public void moveBackward() {
        xCoord -= MOVE_AMT * Math.sin(angle);
        yCoord += MOVE_AMT * Math.cos(angle);
    }

    public BufferedImage getPlayerImage() {
        return norm;
    }

    public double getAngle() {
        return angle;
    }

    // We use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        return new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
    }
}