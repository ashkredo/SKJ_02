/**
 *
 *  @author Shkred Artur
 *
 */

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

public class Agent {

    final static int PORT = 7979;
    private volatile AtomicLong licznik;
    private int interval;
    private Server server;
    private Client client;
    private MulticastSocket multicastSocket;
    volatile long counter;
    volatile int agentCounter;

    public Agent(long counter, int interval) throws IOException {
        this.counter = 0;
        this.agentCounter = 0;
        this.licznik = new AtomicLong(counter);
        this.interval = interval*1000;
        licznik();
        System.setProperty("java.net.preferIPv4Stack", "true");
        multicastSocket = new MulticastSocket(PORT);
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.isSiteLocalAddress() && !i.isAnyLocalAddress() && !i.isLinkLocalAddress()
                        && !i.isLoopbackAddress() && !i.isMulticastAddress()) {
                    multicastSocket.setNetworkInterface(NetworkInterface.getByName(n.getName()));
                }
            }
        }
        InetAddress group = InetAddress.getByName("224.0.0.3");
        multicastSocket.joinGroup(group);
        server = new Server(this, multicastSocket);
        server.run();
        client = new Client(group);
        interval();
        new Gui(this);
    }

    synchronized void licznik(){
        Thread licznikThread = new Thread(() -> {
            while (true) {
                this.licznik.incrementAndGet();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        licznikThread.start();
    }

    synchronized void interval(){
        Thread intervalThread = new Thread(() -> {
            while (true) {
                try {
                    this.client.send("SYN");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long newcounter = count();
                if (newcounter != -1) {
                    this.licznik.set(newcounter);
                }
                try {
                    Thread.sleep(interval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        intervalThread.start();
    }

     long count(){
        long newCounter = -1;
        if (agentCounter > 1){
            try {
                newCounter = counter / agentCounter;
            }catch (ArithmeticException e){}
        }
        counter = 0;
        agentCounter = 0;
        return newCounter;
    }

    void stop() throws IOException {
        multicastSocket.leaveGroup(client.getGroup());
        System.exit(0);
    }


    public Client getClient() {
        return client;
    }

    public long getLicznik() {
        return licznik.get();
    }

    public void setLicznik(long licznik) {
        this.licznik.set(licznik);
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                new Agent(0, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}