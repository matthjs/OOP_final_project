package nl.rug.ai.oop.rpg.battles.view;

import java.awt.Graphics2D;
import java.util.List;

import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.Drawable;

/**
 * Keeps track of entities from battleSystem
 * And runs the draw() method of each entity, causing them
 * to be displayed with possibly updated information.
 * 
 * @author Matthijs,
 * @author Niclas
 */
public class BattleEntityView implements Drawable {
    private List<BattleEntity> entityList;

    public BattleEntityView() {

    }

    public BattleEntityView(List<BattleEntity> entityList) {
        this.entityList = entityList;
    }

    public void setEntityList(List<BattleEntity> entityList) {
        this.entityList = entityList;
    }

    /**
     * Draws all the entities in turn.
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void draw(Graphics2D g2) {
        try {
            for (BattleEntity e : entityList) {
                e.draw(g2);
            }
        } catch (NullPointerException e) {
            System.out.println("entity list empty");
        }
    }
}
