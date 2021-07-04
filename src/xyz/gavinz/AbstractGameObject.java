package xyz.gavinz;

import java.awt.*;

/**
 * 抽象的游戏中的对象
 *
 * @author Gavin.Zhao
 */
public abstract class AbstractGameObject {
    protected int x, y, width, height;
    protected Rectangle rectangle;
    protected boolean live = true;

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public abstract void paint(Graphics g);

}
