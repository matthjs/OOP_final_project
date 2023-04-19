package nl.rug.ai.oop.rpg.battles.model.entities.party;

import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Generic party member class
 * 
 * @author Matthijs
 * @author Niclas
 */
public abstract class PartyMember extends BattleEntity {

    public PartyMember() {
        super();
    }

    @Override
    protected void configureAttributes() {
        super.configureAttributes();
    }

    /**
     * Sets the default values for a PartyMember
     * 
     * @author Matthijs
     */
    @Override
    public void setDefaultValues() {
        state = State.IDLE;
        setWorldX(GameView.tileSize * 5); // right boundary around 20
        setWorldY(GameView.tileSize * 5); // was 3
        setDirection("right");
    }

    @Override
    public void update() {
        super.update();
    }
}
