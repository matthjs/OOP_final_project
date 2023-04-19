package nl.rug.ai.oop.rpg.battles.model.entities.opponents;
import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.MagicAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Mog is an mid strong opponent
 * @author Matthijs
 * @author Niclas
 */
public class Mog extends Opponent {
    public Mog() {
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
        setMaxHP(150);
        setHP(150);
        setMaxMP(20);
        setMP(20);
        setPhysDef(50);
        setPhysPow(30);
        setMagicPow(30);
        setMagicDef(55);
        setName("Mog");
    }

    /**
     * Sets the abilities a Mog can use
     * Dance: deals damage
     * Singing Reduces physical defense for 5 turns
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setAbilities() {
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -20, "physDef"));
            }
        }, "Dance");
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-20);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "physDef", "magicPow", -20, "magicDef", 5, true));
            }
        }, "Singing");
    }

    /**
     * Sets the default sprite, idle animation, damage animation, moving animation, action animation of Mog (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/Mog/mog_sprite_";
        int scale = GameView.tileSize / 2 + GameView.tileSize;
        setDefaultImage(setup(s + "7.png", scale));
        idleAnim.addFrame(setup(s + "0.png", scale));
        idleAnim.addFrame(setup(s + "1.png", scale));
        idleAnim.addFrame(setup(s + "2.png", scale));
        idleAnim.addFrame(setup(s + "4.png", scale));

        damageAnim.addFrame(setup(s + "3.png", scale));

        movingAnim.addFrame(setup(s + "7.png", scale));
        movingAnim.addFrame(setup(s + "5.png", scale));

        actionAnim.addFrame(setup(s + "1.png", scale));
        actionAnim.addFrame(setup(s + "5.png", scale));
    }

}
