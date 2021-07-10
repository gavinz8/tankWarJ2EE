package xyz.gavinz.net.nettycodec.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import xyz.gavinz.Bullet;
import xyz.gavinz.Direction;
import xyz.gavinz.Group;
import xyz.gavinz.net.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Gavin.Zhao
 */
public class BulletMsgTest {

    @Test
    public void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgEncoder());

        Bullet bullet = new Bullet(UUID.randomUUID(), 100, 200, Direction.DOWN, Group.BAD);
        BulletMsg msg = new BulletMsg(bullet);

        ch.writeOutbound(msg);

        ByteBuf buf = (ByteBuf) ch.readOutbound();
        MsgType type = MsgType.values()[buf.readInt()];
        int len = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction dir = Direction.values()[buf.readInt()];
        Group group = Group.values()[buf.readInt()];
        boolean live = buf.readBoolean();

        UUID id = new UUID(buf.readLong(), buf.readLong());
        UUID playerId = new UUID(buf.readLong(), buf.readLong());

        assertEquals(msg.getType(), type);
        assertEquals(49, len);
        assertEquals(x, 100);
        assertEquals(y, 200);

        assertEquals(bullet.getDirection(), dir);
        assertEquals(bullet.getId(), id);
        assertTrue(live);
        assertEquals(bullet.getGroup(), group);
        assertEquals(bullet.getPlayerId(), playerId);
    }

    @Test
    public void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgDecoder());

        Bullet bullet = new Bullet(UUID.randomUUID(), 100, 200, Direction.DOWN, Group.BAD);
        BulletMsg msg = new BulletMsg(bullet);

       byte[] msgData = msg.toBytes();

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(msg.getType().ordinal());
        buf.writeInt(msgData.length);
        buf.writeBytes(msgData);
        ch.writeInbound(buf);

        BulletMsg msg2 = (BulletMsg) ch.readInbound();

        assertEquals(msg.getX(), msg2.getX());
        assertEquals(msg.getY(), msg2.getY());
        assertEquals(msg.getDirection(), msg2.getDirection());
        assertEquals(msg.getGroup(), msg2.getGroup());
        assertEquals(msg.isLive(), msg2.isLive());
    }
}
