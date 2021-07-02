import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Gavin.Zhao
 */
public class ImageTest {

    @Test
    public void testLoadImage() {
        assertImage("0.gif");
    }

    @Test
    public void testRotateImage() {
        assertImage("GoodTank1.png");
    }

    private void assertImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ImageTest.class.getClassLoader().getResourceAsStream("images/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(image);
    }
}
