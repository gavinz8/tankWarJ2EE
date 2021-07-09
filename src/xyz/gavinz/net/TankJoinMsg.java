package xyz.gavinz.net;

import xyz.gavinz.*;

import java.io.*;
import java.util.UUID;

/**
 * tank join message
 *
 * @author Gavin.Zhao
 */
public class TankJoinMsg extends Message {
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

    public TankJoinMsg(Player t) {
        this.x = t.getX();
        this.y = t.getY();
        this.direction = t.getDirection();
        this.moving = t.isMoving();
        this.group = t.getGroup();
        this.id = t.getId();
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

    public void handle() {
        if (GameModel.INSTANCE.findObjectById(this.id) != null) return;

        System.out.println("other tank join..");

        GameModel.INSTANCE.add(new NPCTank(this.id, x, y, this.direction));

        Client.INSTANCE.send(new TankJoinMsg(GameModel.INSTANCE.getMyTank()));
    }

    @Override
    public MsgType getType() {
        return MsgType.TankJoin;
    }
}
