package nl.rug.ai.oop.rpg.general;

import java.awt.*;

/**
 * Use to identify elements that can be drawn
 * on the screen (JPanel)
 * @author Matthijs
 * @author Niclas
 */
public interface Drawable {
    public void draw(Graphics2D g2);
}
