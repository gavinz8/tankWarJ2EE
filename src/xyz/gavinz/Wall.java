package xyz.gavinz;

import java.awt.*;

/**
 * 障碍物墙
 *
 * @author Gavin.Zhao
 */
public class Wall extends AbstractGameObject {

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.rectangle = new Rectangle(x, y, width, height);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(x, y, width, height);
    }
}
