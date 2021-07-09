package xyz.gavinz.net.nettycodec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.gavinz.net.nettycodec.message.TankMessage;
import xyz.gavinz.net.nettycodec.message.TankMsgDecoder;
import xyz.gavinz.net.nettycodec.message.TankMsgEncoder;

/**
 * test codec
 *
 * @author Gavin.Zhao
 */
public class CodecTest {

    @Test
    void decode() {
        int oX = 5;
        int oY = 10;
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new TankMsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(oX);
        buf.writeInt(oY);

        ch.writeInbound(buf);

        TankMessage message = ch.readInbound();

        Assertions.assertEquals(message.getX(), oX);
        Assertions.assertEquals(message.getY(), oY);
    }

    @Test
    void encode() {
        int oX = 8;
        int oY = 10;

        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new TankMsgEncoder());

        ch.writeOutbound(new TankMessage(oX, oY));

        ByteBuf buf = ch.readOutbound();
        int x = buf.readInt();
        int y = buf.readInt();

        Assertions.assertEquals(x, oX);
        Assertions.assertEquals(y, oY);
    }

}
