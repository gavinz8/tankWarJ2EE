package xyz.gavinz;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

/**
 * @author Gavin.Zhao
 */
public class NPCTank extends AbstractGameObject {
    public static final int SPEED = 5;
    private Direction direction;
    private boolean moving = false;
    private final Group group = Group.BAD;
    private int oldX, oldY;

    public NPCTank(UUID id, int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;

        oldX = x;
        oldY = y;

        rectangle = new Rectangle(x, y, ResourceMgr.badTankD.getWidth(), ResourceMgr.badTankD.getHeight());
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


    public Group getGroup() {
        return group;
    }

    public void die() {
        this.setLive(false);
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

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    private final Random random = new Random();

    private void randomDir() {
        if (random.nextInt(100) > 95)
            this.direction = Direction.random();
    }

    @Override
    public void paint(Graphics g) {
        if (!this.isLive()) return;

        switch (direction) {
            case UP:
                g.drawImage(ResourceMgr.badTankU, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.badTankD, x, y, null);
                break;
            case LEFT:
                g.drawImage(ResourceMgr.badTankL, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.badTankR, x, y, null);
                break;
        }

//        g.fillRect(x, y, 50, 50);
        move();
    }

    private void move() {
        if (!moving) return;

        oldX = x;
        oldY = y;

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
            default:
                break;
        }

        rectangle.x = x;
        rectangle.y = y;

        boundsCheck();

        randomDir();
        /*
        if (random.nextInt(100) > 90)
            fire();
         */
    }

    public int getWidth() {
        return ResourceMgr.badTankD.getWidth();
    }

    public int getHeight() {
        return ResourceMgr.badTankD.getHeight();
    }

    private void boundsCheck() {
        // 出界
        if (x < 0 || y < 30
                || x > (TankFrame.WINDOW_WIDTH - this.getWidth())
                || y > (TankFrame.WINDOW_HEIGHT - this.getHeight())) {
            this.back();
        }
    }

    private void back() {
        this.x = oldX;
        this.y = oldY;
    }

    private void fire() {
        int bX = x + ResourceMgr.goodTankD.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = y + ResourceMgr.goodTankD.getHeight() / 2 - ResourceMgr.bulletU.getHeight() / 2;
        TankFrame.INSTANCE.add(new Bullet(this.id, bX, bY, direction, Group.BAD));
    }

}
