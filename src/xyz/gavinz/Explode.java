package xyz.gavinz;

import java.awt.*;

/**
 * @author Gavin.Zhao
 */
public class Explode extends AbstractGameObject {
    public static final int WIDTH = ResourceMgr.explodes[0].getWidth();
    public static final int HEIGHT = ResourceMgr.explodes[0].getHeight();

    private int step = 0;

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {
        if (!this.isLive()) return;
        g.drawImage(ResourceMgr.explodes[step++], x, y, null);
        if (step >= ResourceMgr.explodes.length) {
            step = 0;
            this.setLive(false);
        }
    }


}
