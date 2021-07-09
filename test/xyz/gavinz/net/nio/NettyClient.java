package xyz.gavinz.net.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

/**
 * netty client
 *
 * @author Gavin.Zhao
 */
public class NettyClient {
    @Test
    public void testClient() throws Exception {
        NioEventLoopGroup eventLoop = new NioEventLoopGroup(1);
        Bootstrap client = new Bootstrap();
        client.group(eventLoop);
        client.channel(NioSocketChannel.class);
        client.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        super.channelActive(ctx);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                        System.out.println(msg);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        ctx.writeAndFlush(Unpooled.copiedBuffer("test".getBytes()));
                    }
                });
            }
        });
        ChannelFuture future = client.connect("localhost", 8889).sync();
        future.channel().closeFuture().sync();
    }
}
