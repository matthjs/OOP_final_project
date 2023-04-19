package nl.rug.ai.oop.rpg.general.object;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import nl.rug.ai.oop.rpg.general.UtilityTool;

/**
 * Object in the world
 * 
 * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
 */
public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // whole object tile is solid
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    UtilityTool uTool = new UtilityTool();
}