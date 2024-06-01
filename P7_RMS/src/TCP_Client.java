/**
 * @file TCP_Client.java
 * @author marina
 */
import com.github.sarxos.webcam.Webcam;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

class ClienteTCP {

    public static void main(String args[]) throws UnknownHostException, IOException {
        String dir = "localhost";
        int port = 7123;
        try (Socket socket = new Socket(dir, port);
            InputStream inputStream = socket.getInputStream()) {

            byte[] imageData = new byte[921600];
            int bytesRead = inputStream.read(imageData);
            System.out.println("Imagen recibida");

            ByteArrayInputStream bais = new ByteArrayInputStream(imageData, 0, bytesRead);
            BufferedImage image = ImageIO.read(bais);

            WebcamImg webcam = new WebcamImg();
            webcam.saveAndDisplayImg(image, "test_TCP");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
 
}
