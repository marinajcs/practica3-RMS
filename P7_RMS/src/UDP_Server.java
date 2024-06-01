/**
 * @file UDP_Server.java
 * @author marina
 */
import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class UDP_Server {

    public static void main(String[] args) {
        int port = 8767;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new UDP_Server(port);
    }

    private UDP_Server(int port) {
        /*  1. Creamos el socket
            2. Esperamos un mensaje
            3. Almacenamos dir. IP y puerto en el datagrama a enviar 4. Abrimos la cámara y capturamos una imagen
            5. Copiamos la imagen a un stream (ByteArrayOutputStream)
            Para ello, añadimos primero el número de filas (getHeight), luego el número de columnas (getWidth) y por último los pixeles RGB (getRGB)
            6. Almacenamos en un array de bytes la información a transmitir
            7. Transmitimos el número de datagramas que se van a enviar 8. Transmitimos los datagramas que contienen los datos de la
            imagen (se recomienda usar la función sleep entre cada envío) 
         */
        try {
            // 1. Creamos el socket
            DatagramSocket socket = new DatagramSocket(port);

            // 2. Esperamos un mensaje
            System.out.println("Servidor escuchando en el puerto " + port);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            System.out.println("Mensaje de cliente recibido");

            // 3. Almacenamos dir. IP y puerto en el datagrama a enviar
            InetAddress IPAddress = receivePacket.getAddress();
            int portClient = receivePacket.getPort();

            // 4. Abrimos la cámara y capturamos una imagen
            WebcamImg webcam = new WebcamImg();
            BufferedImage image = webcam.getScreenshot();
            System.out.println("Captura de webcam realizada");

            /* 5. Copiamos la imagen a un stream (ByteArrayOutputStream)
             * Para ello, añadimos primero el número de filas (getHeight),
             * luego el número de columnas (getWidth) y por último 
             * los pixeles RGB (getRGB)
             */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(image.getHeight());
            dos.writeInt(image.getWidth());

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    dos.writeInt(image.getRGB(x, y));
                }
            }
            dos.flush();

            // 6. Almacenamos en un array de bytes la información a transmitir
            byte[] imageData = baos.toByteArray();

            // 7. Transmitimos el número de datagramas que se van a enviar
            int numPackets = (int) Math.ceil(imageData.length / (double) 1024);
            byte[] numPacketsData = Integer.toString(numPackets).getBytes();
            DatagramPacket packet = new DatagramPacket(numPacketsData, numPacketsData.length, IPAddress, portClient);
            socket.send(packet);

            // 8. Transmitimos los datagramas que contienen los datos de la imagen
            System.out.println("Se van a enviar " + numPackets + " paquetes al cliente");
            for (int i = 0; i < numPackets; ++i) {
                int start = i * 1024;
                int length = Math.min(imageData.length - start, 1024);
                DatagramPacket imagePacket = new DatagramPacket(imageData, start, length, IPAddress, portClient);
                socket.send(imagePacket);
                Thread.sleep(10);
            }
            System.out.println("Envío de la imagen finalizado");
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
