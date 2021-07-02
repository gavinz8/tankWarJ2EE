package xyz.gavinz;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Gavin.Zhao
 */
public class PropertyMgr {
    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return props.get(key).toString();
    }

    public static Integer getInteger(String key) {
        return Integer.valueOf(props.get(key).toString());
    }
}
