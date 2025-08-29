package tools;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.commons.lang.SerializationUtils;

/**
 *
 * @author Home
 */
public class ClientUDP {
    public static void send(String message) {
        InetAddress adresse;
        DatagramSocket socket;
        int serverPort = 54321;
        try{
            adresse = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
            byte[] data = message.getBytes();
            DatagramPacket request = new DatagramPacket(data, data.length,adresse,serverPort);            
            socket.send(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void send(MyData myData) {
        InetAddress adresse;
        DatagramSocket socket;
        int serverPort = 54321;
        try{
            adresse = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
            byte[] data = SerializationUtils.serialize(myData);
            DatagramPacket request = new DatagramPacket(data, data.length,adresse,serverPort);            
            socket.send(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}