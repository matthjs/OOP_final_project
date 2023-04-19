package nl.rug.ai.oop.rpg.battles.model.entities.party;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.IncrementalEffect;
import nl.rug.ai.oop.rpg.battles.model.abilities.MagicAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Attackers are the main damage dealers of the party.
 * They don't have as much health or defense as a defender.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class Attacker extends PartyMember {

    public Attacker() {
        super();
    }

    /**
     * Default stats
     */
    public Attacker(String name) {
        super();
        setName(name);
    }

    /**
     * Sets default values for Attacker
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
        setPhysDef(40);
        setPhysPow(20);
        setMagicPow(20);
        setMagicDef(40);
        setName("Attacker");
    }

    /**
     * Specifies the abilities that the Attacker can use during battle
     * Spinning Edge: Reduces  HP of opponent and incrementally reduces HP for 3 turns 
     * Slash: Reduces HP of opponent 
     * Blood Thirst: Reduces HP of opponent and reduces HP of opponent for 2 turns, It also costs 30 MP and heals for 15HP 
     * @author Matthijs
     * @author Niclas
     */
    protected void setAbilities() {
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -35, "physDef"));
                e.addEffect(new IncrementalEffect(e.getAttributes(), "HP", "magicPow", -5, "magicDef",3, false));
            }
        }, "Spinning Edge");
        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -40, "physDef"));
            }
        }, "Slash");
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-30);
                dHP(+15);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -40, "physDef"));
                e.addEffect(new IncrementalEffect(e.getAttributes(), "HP", "magicPow", -5, "magicDef", 2, false));
            }
        }, "Blood Thirst");
    }

     /**
     * Sets the default sprite, idle animation, damage, moving animation, action animation of Attacker (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/FF/sprite_";
        int scale = GameView.tileSize / 2 + GameView.tileSize;
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
