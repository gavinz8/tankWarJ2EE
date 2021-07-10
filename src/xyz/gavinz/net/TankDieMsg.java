package xyz.gavinz.net;

import xyz.gavinz.*;

import java.awt.*;
import java.io.*;
import java.util.UUID;

public class TankDieMsg extends Message {
    private UUID id;
    private UUID bulletId;

    public TankDieMsg() {
    }

    public TankDieMsg(UUID id, UUID bulletId) {
        this.id = id;
        this.bulletId = bulletId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBulletId() {
        return bulletId;
    }

    public void setBulletId(UUID bulletId) {
        this.bulletId = bulletId;
    }

    @Override
    public byte[] toBytes() {

        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());

            dos.writeLong(bulletId.getMostSignificantBits());
            dos.writeLong(bulletId.getLeastSignificantBits());

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
            id = new UUID(buf.readLong(), buf.readLong());
            bulletId = new UUID(buf.readLong(), buf.readLong());
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
        Bullet bullet = ((Bullet) GameModel.INSTANCE.findObjectById(this.bulletId));
        if (bullet != null) {
            System.out.println("bullet will die");
            bullet.die();
        }

        if (this.id.equals(GameModel.INSTANCE.getMyTank().getId())) {
            Player myTank = GameModel.INSTANCE.getMyTank();
            myTank.die();

            Rectangle rectangle = myTank.getRectangle();
            System.out.println("new explode, rect is " + rectangle);

            TankFrame.INSTANCE.add(
                    new Explode(
                            rectangle.x + (int)rectangle.getWidth() / 2 - Explode.WIDTH / 2,
                            rectangle.y + (int)rectangle.getHeight() / 2 - Explode.HEIGHT / 2)
            );
        } else {
            NPCTank tank = ((NPCTank) GameModel.INSTANCE.findObjectById(id));
            if (tank != null) {
                Rectangle rectangle = tank.getRectangle();
                tank.die();

                TankFrame.INSTANCE.add(
                        new Explode(
                                rectangle.x + tank.getWidth() / 2 - Explode.WIDTH / 2,
                                rectangle.y + tank.getHeight() / 2 - Explode.HEIGHT / 2)
                );
            }

        }
    }

    @Override
    public MsgType getType() {
        return MsgType.TankDie;
    }
}
