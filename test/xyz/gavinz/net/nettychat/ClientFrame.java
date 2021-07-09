package xyz.gavinz.net.nettychat;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 聊天窗口
 *
 * @author Gavin.Zhao
 */
public class ClientFrame extends Frame {

    public static final  ClientFrame INSTANCE = new ClientFrame();

    private TextArea ta = new TextArea();
    private TextField tf = new TextField();
    private Client client = null;

    public ClientFrame() {
        this.setSize(300, 400);
        this.setLocation(400, 50);
        this.setTitle("Chat");
        this.add(ta, BorderLayout.CENTER);
        this.add(tf, BorderLayout.SOUTH);

        tf.addActionListener(e -> {
            ta.setText(ta.getText() + tf.getText() + System.lineSeparator());
            client.send(tf.getText());
            tf.setText("");
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
                System.exit(0);
            }
        });

    }

    public void connectToServer() {
        client = new Client();
        client.connect();
    }

    public void updateText(String str) {
        ta.setText(ta.getText() + str + System.lineSeparator());
    }

    public static void main(String[] args) {
        ClientFrame f = ClientFrame.INSTANCE;
        f.setVisible(true);
        f.connectToServer();
    }
}
