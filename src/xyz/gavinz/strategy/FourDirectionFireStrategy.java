package xyz.gavinz.strategy;

import xyz.gavinz.*;

/**
 * @author Gavin.Zhao
 */
public class FourDirectionFireStrategy implements FireStrategy {
    @Override
    public void fire(Player p) {
        int bX = p.getX() + ResourceMgr.goodTankD.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = p.getY() + ResourceMgr.goodTankD.getHeight() / 2 - ResourceMgr.bulletU.getHeight() / 2;
        for (Direction direction : Direction.values()) {
            TankFrame.INSTANCE.add(new Bullet(p.getId(), bX, bY, direction, Group.GOOD));
        }
    }
}
