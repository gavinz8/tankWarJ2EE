package xyz.gavinz.strategy;

import xyz.gavinz.*;

/**
 * @author Gavin.Zhao
 */
public class LeftRightFireStrategy implements FireStrategy {
    @Override
    public void fire(Player p) {
        int bX = p.getX() + ResourceMgr.goodTankD.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = p.getY() + ResourceMgr.goodTankD.getHeight() / 2 - ResourceMgr.bulletU.getHeight() / 2;
        TankFrame.INSTANCE.add(new Bullet(bX, bY, Direction.RIGHT, Group.GOOD));
        TankFrame.INSTANCE.add(new Bullet(bX, bY, Direction.LEFT, Group.GOOD));
    }
}
