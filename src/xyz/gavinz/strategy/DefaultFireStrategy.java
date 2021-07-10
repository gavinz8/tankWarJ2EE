package xyz.gavinz.strategy;

import xyz.gavinz.*;
import xyz.gavinz.net.BulletMsg;
import xyz.gavinz.net.Client;

/**
 * @author Gavin.Zhao
 */
public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Player p) {
        int bX = p.getX() + ResourceMgr.goodTankD.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = p.getY() + ResourceMgr.goodTankD.getHeight() / 2 - ResourceMgr.bulletU.getHeight() / 2;
        Bullet bullet = new Bullet(p.getId(), bX, bY, p.getDirection(), Group.GOOD);
        TankFrame.INSTANCE.add(bullet);

        Client.INSTANCE.send(new BulletMsg(bullet));
    }
}
