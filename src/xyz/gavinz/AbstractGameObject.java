package xyz.gavinz;

import java.awt.*;
import java.util.UUID;

/**
 * 抽象的游戏中的对象
 *
 * @author Gavin.Zhao
 */
public abstract class AbstractGameObject {
    protected int x, y, width, height;
    protected Rectangle rectangle;
    protected boolean live = true;
    protected UUID id = UUID.randomUUID();

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
        this.rectangle.x = x;
    }

    public void setY(int y) {
        this.y = y;
        this.rectangle.y = y;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public abstract void paint(Graphics g);

}
