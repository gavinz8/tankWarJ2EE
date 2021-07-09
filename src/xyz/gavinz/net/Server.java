package xyz.gavinz.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * netty
 *
 * @author Gavin.Zhao
 */
public class Server {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private List<TankJoinMsg> tankMessages = new ArrayList<>();

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
                                    .addLast(new MsgEncoder())
                                    .addLast(new MsgDecoder())
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

    class MyChannelInboundHandlerAdapter extends SimpleChannelInboundHandler<TankJoinMsg> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            clients.add(ctx.channel());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) {
            ServerFrame.INSTANCE.updateServerMsg(msg.toString());

            for (Channel client : clients) {
                if (client != ctx.channel()) {
                    client.writeAndFlush(msg);
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
