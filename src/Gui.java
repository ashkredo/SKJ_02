/**
 *
 *  @author Shkred Artur
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;


public class Gui extends JFrame{

    private Agent agent;
    private Controller controller;
    private JLabel count;
    private JLabel inter;
    private JTextArea textArea;

    public Gui(Object object) throws HeadlessException {
        if (object instanceof Agent) {
            setTitle("Agent");
            this.agent = (Agent) object;
            setMainPanel(agent.getLicznik(), agent.getInterval());
        }
        else{
            setTitle("Controller");
            this.controller = (Controller) object;
            setMainPanel();
        }
    }

    void setMainPanel(long counter, int interval){
        JPanel jp = new JPanel(new GridLayout(1,2,2,2));
        count = new JLabel(String.valueOf(counter));
        inter = new JLabel(String.valueOf(interval/1000));
        jp.add(count);
        jp.add(inter);

        JPanel jp1 = new JPanel(new GridLayout(1,2,2,2));
        Button disconnect = new Button("Disconnect");
        Button reload = new Button("Reload");
        disconnect.addActionListener(new disconnectButton());
        reload.addActionListener(new refreshButton());
        jp1.add(disconnect);
        jp1.add(reload);

        add(jp, BorderLayout.NORTH);
        add(jp1, BorderLayout.SOUTH);
        setSize(200, 100);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void setMainPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        JButton send = new JButton("Send");
        send.addActionListener(new SendButton());
        textArea = new JTextArea();

        panel.add(textArea, BorderLayout.NORTH);
        panel.add(send, BorderLayout.SOUTH);
        add(panel);
        setSize(300, 100);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class SendButton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                controller.sendMessage(textArea.getText());
                textArea.setText("");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    class refreshButton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            count.setText(String.valueOf(agent.getLicznik()));
            inter.setText(String.valueOf(agent.getInterval()/1000));
        }
    }

    class disconnectButton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                agent.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}