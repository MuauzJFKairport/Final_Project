import javax.swing.*;

public class MainFrame implements Runnable {

    private GraphicsPanel panel;

    public MainFrame() {
        JFrame frame = new JFrame("Rotating Sprite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 950); // Adjusted size
        frame.setLocationRelativeTo(null); // Auto-centers frame on screen

        // Create and add panel
        panel = new GraphicsPanel();
        frame.add(panel);

        // Display the frame
        frame.setVisible(true);

        // Start thread, required for animation
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            panel.repaint(); // We don't ever call "paintComponent" directly, but call this to refresh the panel
            try {
                Thread.sleep(16); // Adjusted for ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainFrame());
    }
}