package nl.rug.ai.oop.rpg.general;

import java.util.LinkedList;
import java.awt.image.BufferedImage;

/**
 * Decorator pattern: Animation holds a list of BufferedImages
 * and adds methods that allows for one to abstract away the underlying list
 * @author Matthijs
 */
public class Animation extends LinkedList<BufferedImage> {

    private LinkedList<BufferedImage> anim;

    public Animation() {
        this.anim = new LinkedList<>();
    }
    /**
     * You can either provide an empty list and then use the methods
     * or provide a list already containing elements.
     * @author Matthijs
     * @param anim
     */
    public Animation(LinkedList<BufferedImage> anim) {
        this.anim = anim;
    }

    /**
     * Add BufferedImage to underlying list
     * @author Matthijs
     * @param img
     */
    public void addFrame(BufferedImage img) {
        anim.add(img);
    }

    /**
     * Returns newest frame
     * @author Matthijs
     * @return
     */
    public BufferedImage getFrame() {
        return anim.get(0);
    }

    /**
     * Returns true if underlying list is empty
     * @author Matthijs
     * @return
     */
    public boolean hasNoAnimation() {
        return anim.isEmpty();
    }

    /**
     * Move to next frame in the list, the frame
     * that was previously at position 0 (front) is now
     * placed at the back
	 * [frame1, frame2, ..., frameN] -> [frame2, ..., frameN, frame1]
     * @author Matthijs
     */
    public void modeToNextFrame() {
        BufferedImage temp = anim.get(0);
        anim.remove(0);
        anim.add(temp);
    }
    
}
