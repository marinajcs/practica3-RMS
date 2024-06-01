/**
 * @file TCP_Server.java
 * @author marina
 */
import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import javax.imageio.ImageIO;

class ServidorTCP {

    public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        int port = 7123;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");

            WebcamImg webcam = new WebcamImg();
            BufferedImage image = webcam.getScreenshot();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageData = baos.toByteArray();

            clientSocket.getOutputStream().write(imageData);
            System.out.println("Imagen enviada");
            
            clientSocket.getOutputStream().close();
            serverSocket.close();
        }

    }
    
}
