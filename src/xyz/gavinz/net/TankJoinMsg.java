package xyz.gavinz.net;

import xyz.gavinz.Direction;
import xyz.gavinz.Group;
import xyz.gavinz.NPCTank;
import xyz.gavinz.Player;

import java.io.*;
import java.util.UUID;

/**
 * tank join message
 *
 * @author Gavin.Zhao
 */
public class TankJoinMsg {
    private int x, y;
    private Direction direction;
    private boolean moving;
    private Group group;

    private UUID id;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TankJoinMsg() {
    }

    public TankJoinMsg(Player player) {
        this.x = player.getX();
        this.y = player.getY();
        this.direction = player.getDirection();
        this.moving = player.isMoving();
        this.group = player.getGroup();
        this.id = player.getId();
    }

    public byte[] toBytes() {
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(direction.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bytes;
        }
    }

    public void parse(byte[] bytes) {
        DataInputStream buf;
        buf = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            x = buf.readInt();
            y = buf.readInt();
            direction = Direction.values()[buf.readInt()];
            moving = buf.readBoolean();
            group = Group.values()[buf.readInt()];
            id = new UUID(buf.readLong(), buf.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
