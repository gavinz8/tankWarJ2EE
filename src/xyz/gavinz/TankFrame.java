package xyz.gavinz;


import xyz.gavinz.chainofresponsibility.BulletTankCollider;
import xyz.gavinz.chainofresponsibility.BulletWallCollider;
import xyz.gavinz.chainofresponsibility.Collider;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gavin.Zhao
 */
public class TankFrame extends Frame {
    public static final Integer WINDOW_WIDTH = PropertyMgr.getInteger("windowWidth");
    public static final Integer WINDOW_HEIGHT = PropertyMgr.getInteger("windowHeight");

    public static final TankFrame INSTANCE = new TankFrame();

    private GameModel gm = GameModel.INSTANCE;

    private Image offScreenImage;

    private TankFrame() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        setResizable(false);
        setTitle("tank war");
        setVisible(true);

        addKeyListener(new MyKeyListener());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        gm.paint(g);
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
        }

        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void add(AbstractGameObject object) {
        gm.add(object);
    }

    class MyKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            gm.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gm.keyReleased(e);
        }
    }
}
