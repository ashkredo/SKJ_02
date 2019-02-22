/**
 *
 *  @author Shkred Artur
 *
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class Client {

    DatagramSocket sock = new DatagramSocket();
    private String ip;
    private InetAddress group;

    Client(InetAddress group) throws SocketException {
        this.group = group;
    }

    synchronized void send(String toSend) throws Exception{
        Thread client = new Thread(()-> {
            byte buf[] = toSend.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, Agent.PORT);
            try {
                sock.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        client.start();
    }

    public InetAddress getGroup() {
        return group;
    }
}