package xyz.gavinz.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * encoder
 *
 * @author Gavin.Zhao
 */
public class MsgEncoder extends MessageToByteEncoder<TankJoinMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TankJoinMsg msg, ByteBuf out) {
        byte[] bytes = msg.toBytes();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
