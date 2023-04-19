package nl.rug.ai.oop.rpg.general.tile;

import java.awt.image.BufferedImage;

/**
 * Tiles are the squares the player walks on
 * 
 * @author Matthijs
 * @author Niclas
 */
public class Tile {
    private BufferedImage image;
    private boolean collision = false;

    /**
     * Gets image of tile
     * @author Matthijs
 	 * @author Niclas
     * @return
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Sets image of tile
     * @author Matthijs
 	 * @author Niclas
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    /**
     * The player cannot walk over tiles
     * that have collision.
     * @author Matthijs
 	 * @author Niclas
     * @return
     */
    public boolean hasCollision() {
        return collision;
    }

    /**
     * @author Matthijs
 	 * @author Niclas
     * @param collision
     */
    public void setCollision(boolean collision) {
        this.collision = collision;
    }

}
