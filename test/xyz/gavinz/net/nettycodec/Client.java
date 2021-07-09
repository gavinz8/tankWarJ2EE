package xyz.gavinz.net.nettycodec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import xyz.gavinz.net.nettycodec.message.TankMessage;
import xyz.gavinz.net.nettycodec.message.TankMsgEncoder;

/**
 * netty client
 *
 * @author Gavin.Zhao
 */
public class Client {

    private Channel channel = null;

    public void connect() {
        NioEventLoopGroup eventLoop = new NioEventLoopGroup(1);
        Bootstrap client = new Bootstrap();

        try {
            ChannelFuture future = client.group(eventLoop)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            channel = socketChannel;
                            socketChannel
                                    .pipeline()
                                    .addLast(new TankMsgEncoder())
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            super.channelActive(ctx);
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                            TankMessage tankMsg = (TankMessage) msg;
                                            System.out.println(tankMsg);
                                        }



                                        @Override
                                        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
                                            super.channelWritabilityChanged(ctx);
                                        }

                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                            ctx.writeAndFlush(Unpooled.copiedBuffer("test".getBytes()));
                                        }
                                    });
                        }
                    })
                    .connect("localhost", 8889)
                    .sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoop.shutdownGracefully();
        }
    }

    public void send(TankMessage msg) {
        channel.writeAndFlush(msg);
    }

    public void close() {
        channel.close();
    }
}
