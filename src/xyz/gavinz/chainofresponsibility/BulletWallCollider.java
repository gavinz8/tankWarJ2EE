package xyz.gavinz.chainofresponsibility;

import xyz.gavinz.*;

import java.awt.*;

public class BulletWallCollider implements Collider {

    @Override
    public boolean collide(AbstractGameObject o1, AbstractGameObject o2) {
        if (o1 instanceof Bullet && o2 instanceof Wall) {
            if (o1.getRectangle().intersects(o2.getRectangle())) {
                ((Bullet) o1).die();

                Rectangle rectangle = o1.getRectangle();
                TankFrame.INSTANCE.add(new Explode(rectangle.x + rectangle.width / 2 - Explode.WIDTH / 2, rectangle.y + rectangle.height / 2 - Explode.HEIGHT / 2));
                return true;
            } else {
                return false;
            }

        } else if (o2 instanceof Bullet && o1 instanceof Wall) {
            return collide(o2, o1);
        } else {
            return false;
        }
    }
}
