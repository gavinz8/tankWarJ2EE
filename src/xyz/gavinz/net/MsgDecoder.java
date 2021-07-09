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
        if (buf.readableBytes() < 8) return;
        buf.markReaderIndex();

        try {
            MsgType msgType = MsgType.values()[buf.readInt()];
            int len = buf.readInt();

            // 检查是否位完整的消息
            if (buf.readableBytes() < len) {
                buf.resetReaderIndex();
                return;
            }

            byte[] bytes = new byte[len];
            buf.readBytes(bytes);

            Message message = null;
            try {
                String className = Message.class.getPackage().getName() + "." + msgType.name() + "Msg";
                message = (Message) Class.forName(className).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return;
            }

            message.parse(bytes);

            out.add(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
