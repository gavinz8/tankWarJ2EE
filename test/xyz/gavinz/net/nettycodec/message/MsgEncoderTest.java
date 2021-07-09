package xyz.gavinz.net.nettycodec.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import xyz.gavinz.Direction;
import xyz.gavinz.Group;
import xyz.gavinz.Player;
import xyz.gavinz.net.MsgEncoder;
import xyz.gavinz.net.TankJoinMsg;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gavin.Zhao
 */
public class MsgEncoderTest {

    @Test
    void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MsgEncoder());

        Player p = new Player(100, 200, Direction.DOWN);
        TankJoinMsg msg = new TankJoinMsg(p);

        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        int len = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction dir = Direction.values()[buf.readInt()];
        boolean moving = buf.readBoolean();
        Group group = Group.values()[buf.readInt()];
        UUID id = new UUID(buf.readLong(), buf.readLong());

        assertEquals(33, len);
        assertEquals(x, 100);
        assertEquals(y, 200);
        assertEquals(Direction.DOWN, dir);
        assertFalse(moving);
        assertEquals(Group.GOOD, group);
        assertEquals(p.getId(), id);
    }

}
