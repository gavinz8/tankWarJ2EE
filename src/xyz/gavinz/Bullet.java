package xyz.gavinz;

import xyz.gavinz.net.BulletMsg;
import xyz.gavinz.net.Client;

import java.awt.*;
import java.util.UUID;

/**
 * @author Gavin.Zhao
 */
public class Bullet extends AbstractGameObject {
    private static final int SPEED = 10;

    private final Direction direction;
    private final Group group;
    private boolean live = true;
    private UUID playerId;

    public Bullet(UUID playerId, int x, int y, Direction direction, Group group) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.group = group;

        rectangle = new Rectangle(x, y, ResourceMgr.bulletU.getWidth(), ResourceMgr.bulletU.getHeight());

        //Client.INSTANCE.send(new BulletMsg(this));
    }

    public Group getGroup() {
        return group;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    @Override
    public boolean isLive() {
        return live;
    }

    @Override
    public void setLive(boolean live) {
        this.live = live;
    }

    @Override
    public void paint(Graphics g) {
        switch (direction) {
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
            case LEFT:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
        }

        move();
    }

    private void move() {
        switch (direction) {
            case LEFT:
                x -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
        }

        rectangle.x = x;
        rectangle.y = y;
        boundsCheck();
    }

    public void die() {
        this.setLive(false);
    }

    private void boundsCheck() {
        // 出界
        if (x < 0 || y < 30 || x > TankFrame.WINDOW_WIDTH || y > TankFrame.WINDOW_HEIGHT) {
            live = false;
        }

        Client.INSTANCE.send(new BulletMsg(this));
    }
}
