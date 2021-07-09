package xyz.gavinz.net.nettychat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Iterator;

/**
 * netty
 *
 * @author Gavin.Zhao
 */
public class Server {
    public final static Server INSTANCE = new Server();

    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void main(String[] args) {
        Server.INSTANCE.connect();
    }

    public void connect() {
        EventLoopGroup mainGroup = new NioEventLoopGroup(4);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            ServerBootstrap server = new ServerBootstrap();
            ChannelFuture future = server.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            System.out.println("Client has connected.");
                            socketChannel.pipeline()
                                    .addLast(new MyChannelInboundHandlerAdapter());
                        }
                    })
                    .bind(8889)
                    .sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    class MyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            clients.add(ctx.channel());
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = ((ByteBuf) msg);
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String str = new String(bytes);
            System.out.println(str);
            if ("__byte__".equals(str)) {
                System.out.println("One client will close. Count:" + clients.size());
                clients.remove(ctx.channel());
                ctx.channel().close();
                System.out.println("One client closed. Count" + clients.size());
                return;
            }

            for (Channel channel : clients) {
                if (channel != ctx.channel()) {
                    channel.writeAndFlush(msg);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            clients.remove(ctx.channel());
        }
    }
}
