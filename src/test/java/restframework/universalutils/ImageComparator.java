package restframework.universalutils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Slf4j
public class ImageComparator {
    public static boolean isImagesEqual(String expectedImage, String actualImage) {
        boolean result = false;
        try {
            BufferedImage actualImg = ImageIO.read(new URL(actualImage));
            BufferedImage expectedImg = ImageIO.read(new File(expectedImage));
            ImageDiff dif = new ImageDiffer().makeDiff(expectedImg, actualImg);
            if(!dif.hasDiff()){
                result = true;
            }
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
