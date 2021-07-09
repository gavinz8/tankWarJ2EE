package xyz.gavinz.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author Gavin.Zhao
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        if (buf.readableBytes() < 37) return;

        int len = buf.readInt();

        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        TankJoinMsg tankJoinMsg = new TankJoinMsg();
        tankJoinMsg.parse(bytes);

        out.add(tankJoinMsg);
    }
}
