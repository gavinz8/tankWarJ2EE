package xyz.gavinz.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * encoder
 *
 * @author Gavin.Zhao
 */
public class MsgEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) {
        byte[] bytes = msg.toBytes();

        out.writeInt(msg.getType().ordinal());
        out.writeInt(bytes.length);

        out.writeBytes(bytes);
    }
}
