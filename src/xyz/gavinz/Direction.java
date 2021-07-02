package xyz.gavinz;

import java.util.Random;

/**
 * @author Gavin.Zhao
 */
public enum Direction {
    LEFT, RIGHT, UP, DOWN;

    private static Random random = new Random();

    /**
     * 随机生成方向
     * @author Gavin.Zhao
     * @return: xyz.gavinz.Dir
     * @throws
     */

    public static Direction random() {
        return values()[random.nextInt(Direction.values().length)];
    }
}
