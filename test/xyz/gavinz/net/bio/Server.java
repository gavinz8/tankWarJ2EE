package xyz.gavinz.net.bio;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Gavin.Zhao
 */
public class Server {

    @Test
    public void testServer() throws Exception {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress("localhost", 8889));
        Socket s;

        while ((s = ss.accept()) != null) {
            Socket finalS = s;
            new Thread(() -> {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(finalS.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String msg = null;
                try {
                    if (br != null) msg = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close();
                        finalS.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println(msg);
            }).start();
        }

        ss.close();
    }

}
