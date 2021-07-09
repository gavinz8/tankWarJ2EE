package xyz.gavinz;

import xyz.gavinz.net.Client;

public class Main {

    public static void main(String[] args) {
        TankFrame tf = TankFrame.INSTANCE;
        new Thread(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tf.repaint();
            }
        }).start();

        Client.INSTANCE.connect();
    }
}
