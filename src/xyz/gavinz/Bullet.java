package xyz.gavinz;

import java.awt.*;

/**
 * @author Gavin.Zhao
 */
public class Bullet extends AbstractGameObject {
    private static final int SPEED = 10;

    private final Direction direction;
    private final Group group;
    private boolean live = true;

    public Bullet(int x, int y, Direction direction, Group group) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.group = group;

        rectangle = new Rectangle(x, y, ResourceMgr.bulletU.getWidth(), ResourceMgr.bulletU.getHeight());
    }

    public Group getGroup() {
        return group;
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
    }
}
