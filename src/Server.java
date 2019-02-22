/**
 *
 *  @author Shkred Artur
 *
 */

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

class Server{
    private MulticastSocket sock;
    private byte buf[];
    private Agent agent;

    Server(Agent agent, MulticastSocket s) {
        this.agent = agent;
        this.sock = s;
        buf = new byte[128];

    }

    synchronized void run() {
        Thread server = new Thread(() -> {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    sock.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    if (received.startsWith("SYN")) {
                        agent.getClient().send("ASYN " + String.valueOf(agent.getLicznik()));
                    } else if (received.startsWith("ASYN")) {
                        agent.counter += Integer.valueOf(received.split(" ")[1]);
                        agent.agentCounter += 1;
                    } else if (received.startsWith("set")) {
                        if (received.split(" ")[1].equals("counter")) {
                            this.agent.setLicznik(Long.parseLong(received.split(" ")[2]));
                            JOptionPane.showMessageDialog(null, "counter changed to: " + received.split(" ")[2]);
                        } else {
                            this.agent.setInterval(Integer.parseInt(received.split(" ")[2]) * 1000);
                            JOptionPane.showMessageDialog(null, "interval changed to: " + received.split(" ")[2]);
                        }
                    } else if (received.startsWith("get")) {
                        if (received.split(" ")[1].equals("counter")) {
                            this.agent.getClient().send("GET Counter = " + agent.getLicznik());
                        } else {
                            this.agent.getClient().send("GET Interval = " + agent.getInterval() / 1000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        server.start();
    }
}