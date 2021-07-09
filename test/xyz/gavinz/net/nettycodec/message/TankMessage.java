package xyz.gavinz.net.nettycodec.message;

/**
 * tank message
 *
 * @author Gavin.Zhao
 */
public class TankMessage {
    private int x, y;

    public TankMessage() {
    }

    public TankMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "TankMessage{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
