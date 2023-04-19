package nl.rug.ai.oop.rpg.battles.model.entities.opponents;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.IncrementalEffect;
import nl.rug.ai.oop.rpg.battles.model.abilities.MagicAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * DarkMage is a boss-level opponent
 * Extends Opponent.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class DarkMage extends Opponent {
    public DarkMage() {
        super();
    }

    /**
     * Sets default values for a Dark Mage
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(666);
        setHP(666);
        setMaxMP(10);
        setMP(10);
        setPhysDef(70);
        setPhysPow(40);
        setMagicPow(40);
        setMagicDef(70);
        setName("DarkMage");
    }

    /**
     * Sets the abilities the dark mage can use
     * Heal: Increases HP, scales with magic power
     * Dark Matter: Decreases HP, scales with physical power and scales against physical defense
     * Primoridal burst: Decreases HP, scales with physical power and scales against physical defense
     * Drain: reduces physical defense for 5 turns, reduces magical defense for 5 turns, reduces HP for 3 turns
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setAbilities() {
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-20);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "magicPow", 100));
            }
        }, "Heal", true);
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -50, "physDef"));
                e.setStateToDamage(ATTACKANIMATIONDURATION);
            }
        }, "Dark Matter");

        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dHP(+20);
                dMP(-50);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -40, "physDef"));
                e.addEffect(new StaticEffect(e.getAttributes(), "physDef", "magicPow", -10, "magicDef", 5, true));
            }
        }, "Primordial Burst");
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-50);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "physDef", "magicPow", -10, "magicDef", 5, true));
                e.addEffect(new StaticEffect(e.getAttributes(), "magicDef", "magicPow", -10, "magicDef", 5, true));
                e.addEffect(new IncrementalEffect(e.getAttributes(), "HP", "magicPow", -10, "magicDef", 3, false));
            }
        }, "Drain");

    }

    /**
     * Sets the default sprite, idle animation, damage animation, moving animation, action animation of dark mage (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/DarkMage/dark_mage_sprite";
        int scale = GameView.tileSize * 2;
        setDefaultImage(setup(s + "00.png", scale));
        idleAnim.addFrame(setup(s + "00.png", scale));
        idleAnim.addFrame(setup(s + "01.png", scale));

        damageAnim.addFrame(setup(s + "07.png", scale));

        movingAnim.addFrame(setup(s + "00.png", scale));
        movingAnim.addFrame(setup(s + "03.png", scale));

        actionAnim.addFrame(setup(s + "04.png", scale));
        actionAnim.addFrame(setup(s + "05.png", scale));
    }

}