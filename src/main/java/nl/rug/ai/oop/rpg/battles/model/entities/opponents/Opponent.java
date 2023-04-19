package nl.rug.ai.oop.rpg.battles.model.entities.opponents;

import java.util.List;
import java.util.Random;

import nl.rug.ai.oop.rpg.battles.model.abilities.Ability;
import nl.rug.ai.oop.rpg.battles.model.abilities.Command;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Opponents are the entities that your party fights
 * 
 * @author Matthijs
 * @author Niclas
 */
public abstract class Opponent extends BattleEntity {

    public Opponent() {
        super();
    }

    /**
     * See setDefaultValues() in BattleEntity.
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setDefaultValues() {
        state = State.IDLE;
        setWorldX(GameView.tileSize * 15); // right boundary around 20
        setWorldY(GameView.tileSize * 5); // was 3
        setDirection("left");
    }

    /**
     * Runs the update method of battle entity
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void update() {
        super.update();
    }

    /**
     * Returns a random ability from the list of abilities the battle entity has.
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public Ability chooseRandomAbility() {
        Ability a = null;
        List<Command> magicAb = getMagicAbilities();
        List<Command> attackAb = getAttackAbilities();
        if (magicAb.isEmpty() && attackAb.isEmpty()) {
            throw new IllegalStateException("Cannot choose ability: has no magic nor attack abilities.");
        }
        if (magicAb.isEmpty()) {
            a = (Ability) attackAb.get(new Random().nextInt(attackAb.size()));
        } else if (attackAb.isEmpty()) {
            a = (Ability) magicAb.get(new Random().nextInt(magicAb.size()));
        } else {
            int num = new Random().nextInt(2);
            if (num == 1) {
                a = (Ability) attackAb.get(new Random().nextInt(attackAb.size()));
            } else {
                a = (Ability) magicAb.get(new Random().nextInt(magicAb.size()));
            }
        }
        return a;
    }
}
