package nl.rug.ai.oop.rpg.battles.view;

import java.awt.Graphics2D;

import nl.rug.ai.oop.rpg.general.Drawable;
import nl.rug.ai.oop.rpg.general.GameView;
import nl.rug.ai.oop.rpg.general.UtilityTool;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Draws the background
 * 
 * @author Matthijs
 * @author Niclas
 */
public class BattleBackgroundManager implements Drawable {
    private BufferedImage background;
    private boolean backgroundOn = false;

    public BattleBackgroundManager() {
        super();
    }

    /**
     * draws background image if backGroundOn == true
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void draw(Graphics2D g2) {
        if (backgroundOn) {
            g2.drawImage(background, 0, 0, null);
        }
    }

    /**
     * Loads the background from filePath
     * 
     * @author Matthijs
     * @author Niclas
     * @param filePath
     */
    public void loadBackground(String filePath) {
        System.out.println("loading background");
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        String s = "/Backgrounds/";
        try {
            image = ImageIO.read(getClass().getResourceAsStream(s + filePath));
            background = uTool.scaleImage(image, GameView.screenWidth, GameView.screenHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("loading background ended");
    }

    public void enableBackground() {
        backgroundOn = true;
    }

    public void disableBackground() {
        backgroundOn = false;
    }
}
