package xyz.gavinz.net.nettychat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

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
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            super.channelActive(ctx);
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                            ByteBuf buf = ((ByteBuf) msg);
                                            try {
                                                byte[] bytes = new byte[buf.readableBytes()];
                                                buf.getBytes(buf.readerIndex(), bytes);
                                                String str = new String(bytes);
                                                System.out.println(str);
                                                ClientFrame.INSTANCE.updateText(str);
                                            } finally {
                                                buf.release();
                                            }

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

    public void send(String message) {
        channel.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }

    public void close() {
        send("__byte__");
        channel.close();
    }
}
