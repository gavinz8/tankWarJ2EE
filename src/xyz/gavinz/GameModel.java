package xyz.gavinz;

import xyz.gavinz.chainofresponsibility.BulletTankCollider;
import xyz.gavinz.chainofresponsibility.BulletWallCollider;
import xyz.gavinz.chainofresponsibility.Collider;
import xyz.gavinz.net.TankJoinMsg;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * model manager
 *
 * @author Gavin.Zhao
 */
public class GameModel {

    public static final GameModel INSTANCE = new GameModel();

    private Player myTank;
    private java.util.List<AbstractGameObject> objects;
    private List<Collider> colliders;

    private GameModel() {
        this.initObjects();
    }

    private void initObjects() {
        myTank = new Player(200, 200, Direction.DOWN);
        this.colliders = new ArrayList<>();
        this.colliders.add(new BulletTankCollider());
        this.colliders.add(new BulletWallCollider());

        this.objects = new ArrayList<>();
        objects.add(new Wall(20, 50, 200, 30));
        for (int i = 0; i < PropertyMgr.getInteger("initTankCount"); i++) {
            objects.add(new NPCTank(50 + 50 * i, 50, Direction.DOWN));
        }
    }

    public Player getMyTank() {
        return myTank;
    }

    public void paint(Graphics g) {
        myTank.paint(g);

        // 碰撞检测
        for (int i = 0; i < objects.size(); i++) {
            for (AbstractGameObject object : objects) {
                for (Collider collider : colliders) {
                    collider.collide(objects.get(i), object);
                }
            }

            if (objects.get(i).isLive()) {
                objects.get(i).paint(g);
            } else {
                objects.remove(i);
            }
        }
    }

    public void add(AbstractGameObject object) {
        this.objects.add(object);
    }

    public void keyPressed(KeyEvent e) {
        myTank.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        myTank.keyReleased(e);
    }

    public void handle(TankJoinMsg msg) {
        this.add(new Player(msg));
    }

    public Player findPlayerById(UUID id) {
        for (AbstractGameObject object : this.objects) {
            if (object instanceof Player) {
                if (((Player) object).getId().equals(id)) {
                    return (Player) object;
                }
            }
        }

        return null;
    }
}
