package xyz.gavinz.net.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.jupiter.api.Test;

/**
 * netty
 *
 * @author Gavin.Zhao
 */
public class NettyServer {
    @Test
    public void testServer() throws Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup(4);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        ServerBootstrap server = new ServerBootstrap();
        server.group(mainGroup, workerGroup);
        server.channel(NioServerSocketChannel.class);

        server.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                System.out.println("Client has connected.");
                socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                        ByteBuf buf = ((ByteBuf) msg);
                        byte[] bytes = new byte[buf.readableBytes()];
                        buf.getBytes(buf.readerIndex(), bytes);
                        String str = new String(bytes);
                        System.out.println(str);
                        buf.release();
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        System.out.println(cause.getMessage());
                    }
                });
            }
        });

        ChannelFuture future = server.bind(8889).sync();
        future.channel().closeFuture().sync();
    }
}
