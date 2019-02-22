/**
 *
 *  @author Shkred Artur
 *
 */

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;


public class Controller extends Thread{

    private static Controller instance = null;
    private final static int PORT = 7979;
    private final static String INET_ADDR = "224.0.0.3";


    private Controller() {
        new Gui(this);
        run();
    }

    private static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void sendMessage(String toSend) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName(toSend.split(" ")[0]);
        Thread client = new Thread(()-> {
            byte buf[] = toSend.substring(toSend.indexOf(" ")+1).getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, PORT);
            try(DatagramSocket socket = new DatagramSocket()) {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        client.start();
    }

    public void run(){
        InetAddress address = null;
        try {
            address = InetAddress.getByName(INET_ADDR);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] buf = new byte[128];

        try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            System.setProperty("java.net.preferIPv4Stack", "true");
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.isSiteLocalAddress() && !i.isAnyLocalAddress() && !i.isLinkLocalAddress()
                            && !i.isLoopbackAddress() && !i.isMulticastAddress()) {
                        clientSocket.setNetworkInterface(NetworkInterface.getByName(n.getName()));
                    }
                }
            }
            assert address != null;
            clientSocket.joinGroup(address);

            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                String msg = new String(msgPacket.getData(), 0, msgPacket.getLength());
                if (msg.startsWith("GET")){
                    System.out.println(msg);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Controller.getInstance();
    }
}
