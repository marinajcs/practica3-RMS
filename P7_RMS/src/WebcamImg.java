/**
 * @file WebcamImg.java
 * @author marina
 */
import com.github.sarxos.webcam.Webcam;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WebcamImg {

    public BufferedImage getScreenshot() {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage image = webcam.getImage();
        webcam.close();

        return image;
    }

    public void saveAndDisplayImg(BufferedImage image, String filename) throws IOException {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }
        };

        JFrame frame = new JFrame("Imagen Recibida");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(image.getHeight(), image.getWidth());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        String img_path = "./imgs/" + filename + ".jpg";
        File imgFile = new File(img_path);
        int count = 1;

        while (imgFile.exists()) {
            img_path = "./imgs/" + filename + "_" + count + ".jpg";
            imgFile = new File(img_path);
            count++;
        }

        ImageIO.write(image, "jpg", imgFile);
        System.out.println("Imagen guardada como: " + imgFile.getName());
    }

}
