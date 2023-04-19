package nl.rug.ai.oop.rpg.battles.model.entities.opponents;
import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.IncrementalEffect;
import nl.rug.ai.oop.rpg.battles.model.abilities.MagicAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Ninja is a mid-difficulty opponent
 * @author Matthijs
 * @author Niclas
 */
public class Ninja extends Opponent {
    public Ninja() {
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
        setMaxHP(120);
        setHP(120);
        setMaxMP(20);
        setMP(20);
        setPhysDef(30);
        setPhysPow(30);
        setMagicPow(30);
        setMagicDef(30);
        setName("Ninja");
    }

    /**
     * Sets the abilitties a Ninja can use
     * Poison: incrementally reduces HP for 3 turns
     * Shadow slash: reduces HP and reduces physical defense for 5 turns
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setAbilities() {
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-10);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new IncrementalEffect(e.getAttributes(), "HP", "magicPow", -5, "magicDef", 3, false));
            }

        }, "Poison");
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -30, "physDef"));
                e.addEffect(new StaticEffect(e.getAttributes(), "physDef", "magicPow", -10, "magicDef", 5, true));
            }
        }, "Shadow Slash");
    }

    /**
     * Sets the default sprite, idle animation, damage animation, moving animation, action animation of Ninja (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/Ninja/ninja_sprite";
        int scale = GameView.tileSize / 2 + GameView.tileSize;
        setDefaultImage(setup(s + "0.png", scale));
        idleAnim.addFrame(setup(s + "0.png", scale));
        idleAnim.addFrame(setup(s + "1.png", scale));

        damageAnim.addFrame(setup(s + "4.png", scale));

        movingAnim.addFrame(setup(s + "0.png", scale));
        movingAnim.addFrame(setup(s + "1.png", scale));
        movingAnim.addFrame(setup(s + "2.png", scale));

        actionAnim.addFrame(setup(s + "1.png", scale));
        actionAnim.addFrame(setup(s + "3.png", scale));
    }

}
