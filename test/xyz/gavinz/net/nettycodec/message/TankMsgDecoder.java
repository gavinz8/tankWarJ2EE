package xyz.gavinz.net.nettycodec.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * tank message decoder
 *
 * @author Gavin.Zhao
 */
public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        if (buf.readableBytes() < 8) return;

        int x = buf.readInt();
        int y = buf.readInt();

        out.add(new TankMessage(x, y));
    }
}
