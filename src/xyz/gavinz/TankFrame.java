package xyz.gavinz;


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
    public static final Integer WINDOW_WIDTH;
    public static final Integer WINDOW_HEIGHT;

    static {
        WINDOW_WIDTH = PropertyMgr.getInteger("windowWidth");
        WINDOW_HEIGHT = PropertyMgr.getInteger("windowHeight");
    }

    public static final TankFrame INSTANCE = new TankFrame();

    private Player myTank;
    private Image offScreenImage;
    private List<AbstractGameObject> objects;

    private TankFrame() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        setResizable(false);
        setTitle("tank war");
        setVisible(true);

        this.initObjects();

        addKeyListener(new MyKeyListener());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void initObjects() {
        myTank = new Player(200, 200, Direction.DOWN);
        this.objects = new ArrayList<>();
        objects.add(new Wall(20, 50, 200, 30));
        for (int i = 0; i < PropertyMgr.getInteger("initTankCount"); i++) {
            objects.add(new NPCTank(50 + 50 * i, 50, Direction.DOWN));
        }
    }

    @Override
    public void paint(Graphics g) {
        myTank.paint(g);

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).paint(g);
        }

        /*
        for (int i = 0; i < tanks.size(); i++) {
            if (tanks.get(i).isLive()) {
                tanks.get(i).paint(g);
            } else {
                tanks.remove(i);
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collidesWithTank(tanks.get(j));
            }

            if (!bullets.get(i).isLive()) {
                bullets.remove(i);
            } else {
                bullets.get(i).paint(g);
            }
        }

        for (int i = 0; i < this.explodes.size(); i++) {
            this.explodes.get(i).paint(g);
        }

         */
    }


    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
        }

        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, WINDOW_WIDTH, WINDOW_WIDTH);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void add(AbstractGameObject object) {
        this.objects.add(object);
    }

    class MyKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}
