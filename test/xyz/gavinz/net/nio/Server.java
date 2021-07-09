package xyz.gavinz.net.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * nio server
 *
 * @author Gavin.Zhao
 */
public class Server {

    @Test
    public void testServer() {
        ServerSocketChannel server = null;
        Selector selector = null;

        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            server.socket().bind(new InetSocketAddress("localhost", 8889));

            while (true) {
                selector.select();
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isAcceptable()) {
                        ServerSocketChannel  server2 = ((ServerSocketChannel ) key.channel());
                        SocketChannel channel = server2.accept();
                        channel.configureBlocking(false);

                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                        }

                        channel.register(selector, SelectionKey.OP_READ);
                        System.out.println("accept客户端连接："
                                + channel.socket().getInetAddress().getHostName()
                                + channel.socket().getLocalPort());
                    } else if (key.isReadable()) {
                        //读取数据事件
                        //读取数据事件
                        SocketChannel channel = (SocketChannel)key.channel();

                        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

                        //读取数据
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int count = channel.read(buffer);
                        if (count == -1) {
                            continue;
                        }
                        buffer.flip();
                        String msg = decoder.decode(buffer).toString();

                        System.out.println(count + ":" + msg);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    selector.close();
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
