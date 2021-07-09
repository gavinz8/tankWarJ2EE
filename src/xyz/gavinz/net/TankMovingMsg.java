package xyz.gavinz.net;

import xyz.gavinz.Direction;
import xyz.gavinz.GameModel;
import xyz.gavinz.NPCTank;
import xyz.gavinz.Player;

import java.io.*;
import java.util.UUID;

/**
 * 坦克移动的消息
 *
 * @author Gavin.Zhao
 */
public class TankMovingMsg extends Message {
    private UUID id;
    private int x, y;
    private Direction direction;
    private boolean moving;

    public TankMovingMsg() {

    }

    public TankMovingMsg(UUID id, int x, int y, Direction direction, boolean moving) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public UUID getId() {
        return id;
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

    @Override
    public byte[] toBytes() {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrOStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrOStream);
        try {
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(direction.ordinal());
            dos.writeBoolean(moving);
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());

            bytes = byteArrOStream.toByteArray();
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

    @Override
    public void parse(byte[] bytes) {
        DataInputStream buf;
        buf = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.x = buf.readInt();
            this.y = buf.readInt();
            this.direction = Direction.values()[buf.readInt()];
            this.moving = buf.readBoolean();
            this.id = new UUID(buf.readLong(), buf.readLong());

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
        //System.out.println("Tank is moving..");

        NPCTank tank = (NPCTank) GameModel.INSTANCE.findObjectById(this.id);
        if (tank != null) {
            tank.setX(this.x);
            tank.setY(this.y);
            tank.setMoving(this.moving);
            tank.setDirection(this.direction);
        }
    }

    @Override
    public MsgType getType() {
        return MsgType.TankMoving;
    }
}
