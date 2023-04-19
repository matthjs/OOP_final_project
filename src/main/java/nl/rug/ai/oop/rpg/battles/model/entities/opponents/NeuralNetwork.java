package nl.rug.ai.oop.rpg.battles.model.entities.opponents;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Neural Network is a mid Strong opponent.
 * Extends Opponent.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class NeuralNetwork extends Opponent {
    public NeuralNetwork() {
        super();
    }

    /**
     * See setDefaultValues() in battle entity
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(100);
        setHP(100);
        setMaxMP(100);
        setMP(100);
        setPhysDef(25);
        setPhysPow(30);
        setMagicPow(30);
        setMagicDef(30);
        setName("MultiLayer Perceptron");
    }

    /**
     * Sets abilities a Neural Network can use
     * Backprop: reduces HP (deals damage)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setAbilities() {
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -30, "physDef"));
            }
        }, "BackProp");
    }

    /**
     * Sets the default sprite, idle animation of Neural Network (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/NN/NN_sprite_";
        int scale = GameView.tileSize + GameView.tileSize;
        setDefaultImage(setup(s + "0.png", scale));
        idleAnim.addFrame(setup(s + "0.png", scale));
        idleAnim.addFrame(setup(s + "1.png", scale));
        idleAnim.addFrame(setup(s + "2.png", scale));

    }
}
