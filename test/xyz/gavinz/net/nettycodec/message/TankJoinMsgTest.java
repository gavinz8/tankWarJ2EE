package xyz.gavinz.net.nettycodec.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import xyz.gavinz.Direction;
import xyz.gavinz.Group;
import xyz.gavinz.Player;
import xyz.gavinz.net.MsgDecoder;
import xyz.gavinz.net.MsgEncoder;
import xyz.gavinz.net.MsgType;
import xyz.gavinz.net.TankJoinMsg;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Gavin.Zhao
 */
public class TankJoinMsgTest {

    @Test
    void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgEncoder());

        Player p = new Player(100, 200, Direction.DOWN);
        TankJoinMsg msg = new TankJoinMsg(p);

        ch.writeOutbound(msg);

        ByteBuf buf = (ByteBuf) ch.readOutbound();
        MsgType type = MsgType.values()[buf.readInt()];
        int len = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction dir = Direction.values()[buf.readInt()];
        boolean moving = buf.readBoolean();
        Group group = Group.values()[buf.readInt()];
        UUID id = new UUID(buf.readLong(), buf.readLong());

        assertEquals(MsgType.TankJoin, type);
        assertEquals(33, len);
        assertEquals(x, 100);
        assertEquals(y, 200);
        assertEquals(Direction.DOWN, dir);
        assertFalse(moving);
        assertEquals(Group.GOOD, group);
        assertEquals(p.getId(), id);
    }


    @Test
    void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgDecoder());

        Player p = new Player(100, 200, Direction.DOWN);
        TankJoinMsg msg = new TankJoinMsg(p);
        byte[] msgData = msg.toBytes();

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(msg.getType().ordinal());
        buf.writeInt(msgData.length);
        buf.writeBytes(msgData);
        ch.writeInbound(buf);

        TankJoinMsg msg2 = (TankJoinMsg) ch.readInbound();

        assertEquals(MsgType.TankJoin, msg2.getType());
        assertEquals(msg.getX(), msg2.getX());
        assertEquals(msg.getY(), msg2.getY());
        assertEquals(msg.getDirection(), msg2.getDirection());
        assertFalse(msg.isMoving());
        assertEquals(msg.getGroup(), msg2.getGroup());
        assertEquals(msg.getId(), msg2.getId());
    }

}
