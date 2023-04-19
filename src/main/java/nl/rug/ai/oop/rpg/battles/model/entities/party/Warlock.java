package nl.rug.ai.oop.rpg.battles.model.entities.party;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.IncrementalEffect;
import nl.rug.ai.oop.rpg.battles.model.abilities.MagicAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Warlock uses specialized magic
 * @author Matthijs
 * @author Niclas
 */
public class Warlock extends PartyMember {
    public Warlock() {
        super();
    }

    public Warlock(String name) {
        super();
        setName(name);
    }

    /**
     * Sets default values for Warlock
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(100);
        setHP(100);
        setMaxMP(200);
        setMP(200);
        setPhysDef(45);
        setPhysPow(20);
        setMagicPow(50);
        setMagicDef(45);
        setName("Warlock");
    }

     /**
     * Specifies the abilities that the Warlock can use during battle
     * 
     * MP++: increases MP of selected party member and self
     * Heal++: increases HP of selected party member and self
     * Curse of weakness: reduces HP for opponent. Reduces physical defense for 5 turns for opponent
     * Swing: reduces HP of opponent
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setAbilities() {

        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(50);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "MP", "magicPow", 30));
            }
        }, "MP++", true);

        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -40, "physDef"));
            }
        }, "Swing");

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

        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-20);
                dHP(20);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "magicPow", 20));
            }
        }, "Heal++", true);

        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-50);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -40, "physDef"));
                e.addEffect(new StaticEffect(e.getAttributes(), "physDef", "magicPow", -10, "magicDef", 5, true));
            }
        }, "Curse of weakness");
    }

     /**
     * Sets the default sprite, idle animation, damage, moving animation, action animation of Warlock (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/Warlock/Warlock_sprite";
        int scale = GameView.tileSize / 2 + GameView.tileSize;
        setDefaultImage(setup(s + "00.png", scale));

        idleAnim.addFrame(setup(s + "00.png", scale));
        idleAnim.addFrame(setup(s + "01.png", scale));
        idleAnim.addFrame(setup(s + "04.png", scale));

        damageAnim.addFrame(setup(s + "07.png", scale));

        movingAnim.addFrame(setup(s + "00.png", scale));
        movingAnim.addFrame(setup(s + "03.png", scale));

        actionAnim.addFrame(setup(s + "03.png", scale));
        actionAnim.addFrame(setup(s + "04.png", scale));
        actionAnim.addFrame(setup(s + "05.png", scale));
    }
}
