package nl.rug.ai.oop.rpg.battles.model.entities.opponents;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Big Slime is a low strong opponent.
 * Extends Opponent.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class BigSlime extends Opponent {
    public BigSlime() {
        super();
    }

    /**
     * Sets default values for BigSlime
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(100);
        setHP(100);
        setMaxMP(10);
        setMP(10);
        setPhysDef(20);
        setPhysPow(20);
        setMagicPow(20);
        setMagicDef(20);
        setName("green BigSlime");
    }

    /**
     * Sets the abilities for Bigslime
     * Vortex: reduces HP. Scales with physical power, Scales against physical defense
     * @author Matthijs
     * @auhor Niclas
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
     * Sets the default sprite, idle animation, action animation of Bigslime (with scaling)
     * @author Matthijs
     * @auhtor Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/BBA/Monster/greenslime";
        int scale = GameView.tileSize * 2;
        setDefaultImage(setup(s + "_down_1.png", scale));
        idleAnim.addFrame(setup(s + "_down_1.png", scale));
        idleAnim.addFrame(setup(s + "_down_2.png", scale));

        actionAnim.addFrame(setup(s + "_atk0.png", scale));
        actionAnim.addFrame(setup(s + "_atk1.png", scale));
    }

}
