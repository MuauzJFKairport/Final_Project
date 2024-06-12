import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private final double MOVE_AMT = .25; // Reduced movement amount
    private final double ROTATE_AMT = 0.002; // Reduced rotation amount
    private BufferedImage norm;
    private double xCoord, yCoord;
    private double angle = 0; // Rotation angle in radians
    public int score;

    public Player(String Img) {
        xCoord = 770; // starting position
        yCoord = 710;
        score = 1000;
        try {
            norm = ImageIO.read(new File(Img));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Getter and setter methods for xCoord and yCoord
    public double getxCoord() {
        return xCoord;
    }

    public void setxCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public void setyCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    public int getScore() {
        return score;
    }

    public void subtractScore() {
        score = score - 1;
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
    public Shape playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();

        // Reduce the width and height of the bounding rectangle
        double reducedWidthFactor = .373;
        double reducedHeightFactor = 0.8;
        int reducedWidth = (int) (imageWidth * reducedWidthFactor);
        int reducedHeight = (int) (imageHeight * reducedHeightFactor);

        // Center the smaller rectangle within the player image
        int offsetX = (imageWidth - reducedWidth) / 2;
        int offsetY = (imageHeight - reducedHeight) / 2;

        Rectangle rect = new Rectangle(offsetX, offsetY, reducedWidth, reducedHeight);
        AffineTransform transform = new AffineTransform();
        transform.translate(xCoord, yCoord);
        transform.rotate(angle, imageWidth / 2.0, imageHeight / 2.0);
        return transform.createTransformedShape(rect);
    }
}
