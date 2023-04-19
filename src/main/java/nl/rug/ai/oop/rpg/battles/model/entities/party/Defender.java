package nl.rug.ai.oop.rpg.battles.model.entities.party;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.MagicAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Defender has lots of health and defence
 * 
 * @author Matthijs
 * @author Niclas
 */

public class Defender extends PartyMember {

    public Defender() {
        super();
    }

    public Defender(String name) {
        super();
        setName(name);
    }

    /**
     * Sets default values for Defender
     * 
     * @author Matthijs
     * @author Niclas
     */

    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(300);
        setHP(300);
        setMaxMP(50);
        setMP(50);
        setPhysDef(50);
        setPhysPow(20);
        setMagicPow(20);
        setMagicDef(50);
        setName("Defender");
    }

     /**
     * Specifies the abilities that the Defender can use during battle
     * Smash: Reduces oppponent HP
     * Defend: Magic abilit which increases the PhysDef and MagicDef of an Ally for 3 turns, Costs 20 MP
     * MP+: increases MP of party member
     *
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
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -30, "physDef"));
            }
        }, "Smash");
        addAbility(new MagicAbility() {
            @Override
            public void execute(BattleEntity e) {
                dMP(-20);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "physDef", "physPow", 20, 3, true));
                e.addEffect(new StaticEffect(e.getAttributes(), "magicDef", "magicPow", 20, 3, true));
            }
        }, "Defend", true);

        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "MP", "magicPow", 20));
            }
        }, "MP+", true);
    }

     /**
     * Sets the default sprite, idle animation, damage, moving animation, action animation of Defender (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/FF/sprite_";
        int scale = GameView.tileSize / 2 + GameView.tileSize;
        setDefaultImage(setup(s + "10.png", scale));

        idleAnim.addFrame(setup(s + "10.png", scale));
        idleAnim.addFrame(setup(s + "11.png", scale));

        damageAnim.addFrame(setup(s + "17.png", scale));

        movingAnim.addFrame(setup(s + "10.png", scale));
        movingAnim.addFrame(setup(s + "13.png", scale));

        actionAnim.addFrame(setup(s + "14.png", scale));
        actionAnim.addFrame(setup(s + "15.png", scale));

    }

}
