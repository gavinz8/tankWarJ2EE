package xyz.gavinz.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import xyz.gavinz.GameModel;
import xyz.gavinz.NPCTank;

/**
 * netty client
 *
 * @author Gavin.Zhao
 */
public class Client {
    public static final Client INSTANCE = new Client();

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
                                    .addLast(new MsgEncoder())
                                    .addLast(new MsgDecoder())
                                    .addLast(new MyChannelInboundHandlerAdapter());
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

    public void send(TankJoinMsg msg) {
        channel.writeAndFlush(msg);
    }

    public void close() {
        channel.close();
    }

    class MyChannelInboundHandlerAdapter extends SimpleChannelInboundHandler<TankJoinMsg> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.writeAndFlush(new TankJoinMsg(GameModel.INSTANCE.getMyTank()));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) {
            if (msg.getId().equals(GameModel.INSTANCE.getMyTank().getId())) {
                return;
            }

            if (GameModel.INSTANCE.findPlayerById(msg.getId()) != null) {
                return;
            }

            System.out.println("other tank join..");
            GameModel.INSTANCE.handle(msg);
            ctx.writeAndFlush(new TankJoinMsg(GameModel.INSTANCE.getMyTank()));
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
            super.channelWritabilityChanged(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.writeAndFlush(Unpooled.copiedBuffer("test".getBytes()));
        }
    }

}
