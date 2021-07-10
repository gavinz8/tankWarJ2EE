package xyz.gavinz;

import xyz.gavinz.net.Client;
import xyz.gavinz.net.TankJoinMsg;
import xyz.gavinz.net.TankMovingMsg;
import xyz.gavinz.strategy.FireStrategy;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.UUID;

/**
 * @author Gavin.Zhao
 */
public class Player extends AbstractGameObject {
    public static final int SPEED = 5;
    private Direction direction;
    private boolean bL, bR, bU, bD;
    private boolean moving;
    private Group group = Group.GOOD;
    private FireStrategy fireStrategy;

    private UUID id = UUID.randomUUID();

    public Player(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        width = ResourceMgr.goodTankU.getWidth();
        height = ResourceMgr.goodTankU.getHeight();

        this.rectangle = new Rectangle(x, y, width, height);

        this.initFireStrategy();
    }

    public Group getGroup() {
        return group;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void die() {
        live = false;
    }

    public void paint(Graphics g) {
        if (!this.isLive()) return;

        Color c = g.getColor();
        g.setColor(Color.yellow);
        g.drawString(id.toString(), x, y - 10);
        g.setColor(c);

        switch (direction) {
            case UP:
                g.drawImage(ResourceMgr.goodTankU, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.goodTankD, x, y, null);
                break;
            case LEFT:
                g.drawImage(ResourceMgr.goodTankL, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.goodTankR, x, y, null);
                break;
        }


//        g.fillRect(x, y, 50, 50);
        move();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
            default:
                break;
        }

        setMainDir();
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            default:
                break;
        }

        setMainDir();
    }

    private void move() {
        if (!moving) return;

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

        Client.INSTANCE.send(new TankMovingMsg(this.id, x, y, direction, true));
    }

    private void setMainDir() {
        if (!bL && !bR && !bD && !bU) {
            moving = false;
            Client.INSTANCE.send(new TankMovingMsg(this.id, x, y, direction, false));
        } else {
            moving = true;
            if (bL && !bR && !bD && !bU) direction = Direction.LEFT;
            if (!bL && bR && !bD && !bU) direction = Direction.RIGHT;
            if (!bL && !bR && bD && !bU) direction = Direction.DOWN;
            if (!bL && !bR && !bD) direction = Direction.UP;

            Client.INSTANCE.send(new TankMovingMsg(this.id, x, y, direction, true));
        }
    }

    private void initFireStrategy() {
        String packageName = this.getClass().getPackage().getName() + ".strategy.";
        try {
            fireStrategy = (FireStrategy) Class.forName(packageName + PropertyMgr.get("tankFireStrategy"))
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void fire() {
        fireStrategy.fire(this);
    }

    public UUID getId() {
        return this.id;
    }
}
