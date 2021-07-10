package xyz.gavinz.net;

import xyz.gavinz.*;

import java.io.*;
import java.util.UUID;

/**
 * 子弹消息
 *
 * @author Gavin.Zhao
 */
public class BulletMsg extends Message {
    private UUID id = UUID.randomUUID();
    private int x, y;
    private Direction direction;
    private boolean live;
    private Group group;
    private UUID playerId;

    public BulletMsg() {
    }

    public BulletMsg(Bullet bullet) {
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.id = bullet.getId();
        this.direction = bullet.getDirection();
        this.live = bullet.isLive();
        this.group = Group.BAD;
        this.playerId = bullet.getPlayerId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(direction.ordinal());
            dos.writeInt(Group.BAD.ordinal());
            dos.writeBoolean(live);

            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());

            dos.writeLong(playerId.getMostSignificantBits());
            dos.writeLong(playerId.getLeastSignificantBits());

            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream buf;
        buf = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.x = buf.readInt();
            this.y = buf.readInt();
            this.direction = Direction.values()[buf.readInt()];
            this.group = Group.values()[buf.readInt()];
            this.live = buf.readBoolean();
            this.id = new UUID(buf.readLong(), buf.readLong());
            this.playerId = new UUID(buf.readLong(), buf.readLong());
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

    @Override
    public void handle() {
        Bullet bullet = (Bullet) GameModel.INSTANCE.findObjectById(this.id);
        if (bullet == null) {
            bullet = new Bullet(this.playerId, this.x, this.y, this.direction, this.group);
            bullet.setLive(this.live);
            bullet.setId(this.id);

            GameModel.INSTANCE.add(bullet);
        } else {
            bullet.setX(this.x);
            bullet.setY(this.y);
            bullet.setLive(this.isLive());
        }
    }

    @Override
    public MsgType getType() {
        return MsgType.Bullet;
    }
}
