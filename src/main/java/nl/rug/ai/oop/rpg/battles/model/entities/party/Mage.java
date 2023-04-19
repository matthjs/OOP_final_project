package nl.rug.ai.oop.rpg.battles.model.entities.party;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.IncrementalEffect;
import nl.rug.ai.oop.rpg.battles.model.abilities.MagicAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Healer heals. Low health and defense.
 * setDefaultValues() and setEntityImage() are run within the
 * constructor of the parent class (BattleEntity)
 * 
 * @author Matthijs
 * @author Niclas
 */

public class Mage extends PartyMember {

    public Mage() {
        super();
    }

    public Mage(String name) {
        super();
        setName(name);
    }

    /**
     * Sets default values for Mage
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(80);
        setHP(80);
        setMaxMP(300);
        setMP(300);
        setPhysDef(30);
        setPhysPow(20);
        setMagicPow(50);
        setMagicDef(30);
        setName("Mage");
    }

     /**
     * Specifies the abilities that the Mage can use during battle
     * 
     * Hit: reduces HP of opponent
     * Heal: increases HP of party member
     * Regenerate: heals HP for 5 turns for party member
     * Poison: decreases HP for 5 turns for opponent
     * MP++: inceases MP of party member and increases MP of self
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
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -10, "physDef"));
            }
        }, "Hit");

        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-20);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "magicPow", 20));
            }
        }, "Heal", true);
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-50);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new IncrementalEffect(e.getAttributes(), "HP", "magicPow", 10, 5, false));

            }
        }, "Regenerate", true);
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-50);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new IncrementalEffect(e.getAttributes(), "HP", "magicPow", -10, "magicDef", 5, false));
            }
        }, "Poison");
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(50);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "MP", "magicPow", 30));
            }
        }, "MP++", true);
    }

     /**
     * Sets the default sprite, idle animation, damage, moving animation, action animation of Mage (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/FF/sprite_";
        int scale = GameView.tileSize / 2 + GameView.tileSize;
        setDefaultImage(setup(s + "30.png", scale));

        idleAnim.addFrame(setup(s + "30.png", scale));
        idleAnim.addFrame(setup(s + "31.png", scale));
        idleAnim.addFrame(setup(s + "35.png", scale));

        damageAnim.addFrame(setup(s + "37.png", scale));

        movingAnim.addFrame(setup(s + "31.png", scale));
        movingAnim.addFrame(setup(s + "33.png", scale));

        actionAnim.addFrame(setup(s + "34.png", scale));
        actionAnim.addFrame(setup(s + "35.png", scale));
        actionAnim.addFrame(setup(s + "36.png", scale));
    }

}
