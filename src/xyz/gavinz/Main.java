package xyz.gavinz;

public class Main {

    public static void main(String[] args) {
        TankFrame tf = TankFrame.INSTANCE;
        for (; ; ) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tf.repaint();
        }
    }
}
