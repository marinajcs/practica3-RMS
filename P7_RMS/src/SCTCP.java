/**
 * @file SCTCP.java
 * @author marina
 */
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import javax.imageio.ImageIO;

class SCTCP extends Thread {
    
    private Socket clientSocket;

    public SCTCP(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            WebcamImg webcam = new WebcamImg();
            BufferedImage image = webcam.getScreenshot();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageData = baos.toByteArray();

            clientSocket.getOutputStream().write(imageData);
            clientSocket.getOutputStream().close();

            System.out.println("Imagen enviada al cliente");

        } catch (IOException e) {
            System.out.println("Error al manejar la conexión del cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 7123;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor escuchando en el puerto " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");
            new SCTCP(clientSocket).start();
        }
    }
}
