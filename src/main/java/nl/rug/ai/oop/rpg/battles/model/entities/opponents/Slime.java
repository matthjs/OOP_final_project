package nl.rug.ai.oop.rpg.battles.model.entities.opponents;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;

/**
 * Slime is a weak opponent.
 * Extends Opponent.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class Slime extends Opponent {
    public Slime() {
        super();
    }

    /**
     * Sets the default values for a Slime
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(50);
        setHP(50);
        setMaxMP(10);
        setMP(10);
        setPhysDef(20);
        setPhysPow(20);
        setMagicPow(20);
        setMagicDef(20);
        setName("green slime");
    }

    /**
     * Sets the abilities a slime can use
     * Vortex: reduces HP. Scales with physical power, Scales against physical defense
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setAbilities() {
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -20, "physDef"));
                e.setStateToDamage(ATTACKANIMATIONDURATION);
            }
        }, "Vortex");
    }

    /**
     * Sets the default sprite, idle animation, action animation of slime (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setEntityImage() {
        String s = "/BBA/Monster/greenslime";
        setDefaultImage(setup(s + "_down_1.png", true));
        idleAnim.addFrame(setup(s + "_down_1.png", true));
        idleAnim.addFrame(setup(s + "_down_2.png", true));

        actionAnim.addFrame(setup(s + "_atk0.png", true));
        actionAnim.addFrame(setup(s + "_atk1.png", true));
    }

}
