package nl.rug.ai.oop.rpg.general;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Toolbox class: used to scale images
 * @author (https://www.youtube.com/c/RyiSnow)
 */
public class UtilityTool {
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }
}
