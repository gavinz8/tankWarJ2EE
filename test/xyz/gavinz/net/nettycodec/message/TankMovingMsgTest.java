package xyz.gavinz.net.nettycodec.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import xyz.gavinz.Direction;
import xyz.gavinz.net.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gavin.Zhao
 */
public class TankMovingMsgTest {

    @Test
    public void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgEncoder());

        UUID oId = UUID.randomUUID();
        TankMovingMsg msg = new TankMovingMsg(oId, 100, 200, Direction.DOWN, true);

        ch.writeOutbound(msg);

        ByteBuf buf = (ByteBuf) ch.readOutbound();
        MsgType type = MsgType.values()[buf.readInt()];
        int len = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction dir = Direction.values()[buf.readInt()];
        UUID id = new UUID(buf.readLong(), buf.readLong());

        assertEquals(msg.getType(), type);
        assertEquals(28, len);
        assertEquals(x, 100);
        assertEquals(y, 200);
        assertEquals(Direction.DOWN, dir);
        assertEquals(oId, id);
        assertTrue(msg.isMoving());

    }

    @Test
    public void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgDecoder());

        UUID oId = UUID.randomUUID();
        TankMovingMsg msg = new TankMovingMsg(oId, 100, 200, Direction.DOWN, true);
        byte[] msgData = msg.toBytes();

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(msg.getType().ordinal());
        buf.writeInt(msgData.length);
        buf.writeBytes(msgData);
        ch.writeInbound(buf);

        TankMovingMsg msg2 = (TankMovingMsg) ch.readInbound();

        assertEquals(MsgType.TankMoving, msg2.getType());
        assertEquals(msg.getX(), msg2.getX());
        assertEquals(msg.getY(), msg2.getY());
        assertEquals(msg.getDirection(), msg2.getDirection());
        assertEquals(msg.getId(), msg2.getId());
        assertTrue(msg.isMoving());
    }
}
