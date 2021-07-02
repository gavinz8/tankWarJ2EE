package xyz.gavinz;

import java.awt.*;

/**
 * 抽象的游戏中的对象
 *
 * @author Gavin.Zhao
 */
public abstract class AbstractGameObject {
    public static final int HEIGHT = 0;
    protected int x, y, width, height;

    public abstract void paint(Graphics g);
}
