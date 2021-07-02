package xyz.gavinz;

import java.awt.*;

/**
 * @author Gavin.Zhao
 */
public class Bullet extends AbstractGameObject {
    private Direction direction;
    private Group group;
    private boolean live = true;
    public static final int SPEED = 10;


    public Bullet(int x, int y, Direction direction, Group group) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.group = group;
    }

    public boolean isLive() {
        return live;
    }

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

        boundsCheck();
    }

    public void die() {
        live = false;
    }

    /**
     * 碰撞检测
     * @author Gavin.Zhao
     * @throws
     */
    public void collidesWithTank(NPCTank npcTank) {
        if (!this.isLive() || !npcTank.isLive()) return;
        if (npcTank.getGroup() == this.group) return;

        Rectangle bRect = new Rectangle(x, y, ResourceMgr.bulletU.getWidth(), ResourceMgr.bulletU.getHeight());
        Rectangle tRect = new Rectangle(npcTank.getX(), npcTank.getY(),
                ResourceMgr.goodTankD.getWidth(), ResourceMgr.goodTankD.getHeight());

        if (bRect.intersects(tRect) && npcTank.isLive()) {
            this.die();
            npcTank.die();
        }
    }


    private void boundsCheck() {
        // 出界
        if (x < 0 || y < 30 || x > TankFrame.INSTANCE.WINDOW_WIDTH || y > TankFrame.INSTANCE.WINDOW_HEIGHT) {
            live = false;
        }
    }
}
