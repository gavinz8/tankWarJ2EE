package xyz.gavinz.net.nettycodec.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * tank message encoder
 *
 * @author Gavin.Zhao
 */
public class TankMsgEncoder extends MessageToByteEncoder<TankMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TankMessage msg, ByteBuf out) {
        out.writeInt(msg.getX());
        out.writeInt(msg.getY());
    }
}
