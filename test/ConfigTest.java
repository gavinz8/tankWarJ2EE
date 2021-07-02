import org.junit.jupiter.api.Test;
import xyz.gavinz.PropertyMgr;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Gavin.Zhao
 */
public class ConfigTest {
    @Test
    public void testConfig() {
        Integer initTankCount = PropertyMgr.getInteger("initTankCount");
        assertNotNull(initTankCount);
    }
}
