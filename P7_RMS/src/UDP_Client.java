import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Cliente UDP
 * 
 * @author Marina J.
 */

public class UDP_Client {

    public static void main(String[] args) {
        int port = 8767;
        String dir = "127.0.0.1";

        if (args.length == 2) {
            dir = args[0];
            port = Integer.parseInt(args[1]);
        }

        new UDP_Client(dir, port);
    }

    private UDP_Client(String dir, int port) {
        try {
            // 1. Creamos el socket
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(dir);

            // 2. Enviamos un mensaje al servidor para pedir la imagen
            byte[] sendData = "Send image".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);
            System.out.println("Solicitud a servidor enviada");

            // 3. Recibimos el número de datagramas que se va a recibir
            byte[] numPacketData = new byte[1024];
            DatagramPacket packetCountPacket = new DatagramPacket(numPacketData, numPacketData.length);
            socket.receive(packetCountPacket);
            int numPackets = Integer.parseInt(new String(numPacketData, 0, packetCountPacket.getLength()).trim());
            System.out.println("Se van a recibir " + numPackets + " paquetes del servidor");
            
            // 4. Recibimos y almacenamos la información del servidor
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = 0; i < numPackets; i++) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                baos.write(receiveData, 0, receivePacket.getLength());
            }
            System.out.println("Imagen recibida, se procede a su lectura");

            // 5. Creamos un stream de bytes ByteArrayInputStream para ir leyendo la información recibida
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            DataInputStream dis = new DataInputStream(bais);

            // 6. Leemos el primer dato que es el número de filas
            int rows = dis.readInt();

            // 7. Leemos el segundo dato que es el número de columnas
            int cols = dis.readInt();

            // 8. Copiamos los pixeles recibidos a un objeto imagen
            BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
            int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = dis.readInt();
            }

            // 9. Representamos la imagen
            WebcamImg webcam = new WebcamImg();
            webcam.saveAndDisplayImg(image, "test_UDP");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
