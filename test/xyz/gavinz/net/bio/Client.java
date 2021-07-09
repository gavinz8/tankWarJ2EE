package xyz.gavinz.net.bio;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Gavin.Zhao
 */
public class Client {

    @Test
    public void testClient() throws Exception {
        Socket s = new Socket();
        s.connect(new InetSocketAddress("localhost", 8889));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        bw.write("aaa");
        bw.write("bbb");
        bw.write("ccc");
        bw.write("ddd");
        bw.newLine();
        bw.flush();
        Thread.sleep(1000);
        bw.close();
        s.close();
    }

}
