package xyz.gavinz.net;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * server frame
 *
 * @author Gavin.Zhao
 */
public class ServerFrame extends Frame {
    public final static ServerFrame INSTANCE = new ServerFrame();
    public Server server = new Server();

    private TextArea taServer = new TextArea();
    private TextArea taClient = new TextArea();

    public ServerFrame() {
        this.setSize(800, 300);
        this.setLocation(200, 100);
        this.setTitle("Tank Server");
        Panel panel = new Panel(new GridLayout(1, 2));
        this.add(panel);
        panel.add(taServer);
        panel.add(taClient);

        taServer.setText("Server:" + System.lineSeparator());
        taClient.setText("Client:" + System.lineSeparator());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void updateServerMsg(String msg) {
        taClient.setText(taClient.getText() + msg + System.lineSeparator());
    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.connect();
    }

}
