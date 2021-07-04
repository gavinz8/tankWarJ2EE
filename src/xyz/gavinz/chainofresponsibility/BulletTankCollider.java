package xyz.gavinz.chainofresponsibility;

import xyz.gavinz.*;

import java.awt.*;

public class BulletTankCollider implements Collider {

    @Override
    public boolean collide(AbstractGameObject o1, AbstractGameObject o2) {
        if (o1 instanceof Bullet && o2 instanceof NPCTank) {
            Bullet bullet = (Bullet) o1;
            NPCTank tank = (NPCTank) o2;

            if (!bullet.isLive() || !tank.isLive()) return false;
            if (bullet.getGroup() == tank.getGroup()) return false;

            Rectangle bRect = bullet.getRectangle();
            Rectangle tRect = tank.getRectangle();

            if (bRect.intersects(tRect) && tank.isLive()) {
                bullet.die();
                tank.die();
                Rectangle rectangle = o2.getRectangle();
                TankFrame.INSTANCE.add(new Explode(rectangle.x + tank.getWidth() / 2 - Explode.WIDTH / 2, rectangle.y + tank.getHeight() / 2 - Explode.HEIGHT / 2));
            }

            return true;
        } else if (o1 instanceof NPCTank && o2 instanceof Bullet) {
            return collide(o2, o1);
        }

        return false;
    }
}
