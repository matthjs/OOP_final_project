package nl.rug.ai.oop.rpg.battles.model.entities.party;

import nl.rug.ai.oop.rpg.battles.model.abilities.AttackAbility;
import nl.rug.ai.oop.rpg.battles.model.abilities.StaticEffect;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * Fighter is a combat focused class (battles)
 * @author Matthijs
 * @author Niclas
 */
public class Fighter extends PartyMember {
    public Fighter() {
        super();
    }

    public Fighter(String name) {
        super();
        setName(name);
    }

    /**
     * Sets default values for Fighter
     * 
     * @author Matthijs
     * @author Niclas
     */

    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        setMaxHP(150);
        setHP(150);
        setMaxMP(100);
        setMP(100);
        setPhysDef(40);
        setPhysPow(20);
        setMagicPow(20);
        setMagicDef(40);
        setName("Fighter");
    }

     /**
     * Specifies the abilities that the Fighter can use during battle
     * 
     * Kick!: decreases HP
     * Kamikaze: kills self but decreases HP of opponent massively
     * Berserk: increases physical power of party member
     * MP+: increases MP of party member
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
                e.addEffect(new StaticEffect(e.getAttributes(), "MP", "magicPow", 20));
            }
        }, "MP+", true);

        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -40, "physDef"));
            }
        }, "Kick!");

        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                dHP(-10);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "physPow", "physPow", 40, 3, true));
            }
        }, "Berserk", true);

        addAbility(new AttackAbility() {
            @Override
            public void execute(BattleEntity e) {
                dHP(-99999999);
                setStateToAbilityActivate(ATTACKANIMATIONDURATION);
                e.setStateToDamage(ATTACKANIMATIONDURATION);
                e.addEffect(new StaticEffect(e.getAttributes(), "HP", "physPow", -300));
            }
        }, "Kamikaze");
    }

     /**
     * Sets the default sprite, idle animation, damage, moving animation, action animation of Fighter (with scaling)
     * @author Matthijs
     * @author Niclas
     */
    @Override
    protected void setEntityImage() {
        String s = "/Sprites/Fighter/fighter_sprite";
        int scale = GameView.tileSize / 2 + GameView.tileSize;
        setDefaultImage(setup(s + "00.png", scale));

        idleAnim.addFrame(setup(s + "00.png", scale));
        idleAnim.addFrame(setup(s + "01.png", scale));
        idleAnim.addFrame(setup(s + "04.png", scale));

        damageAnim.addFrame(setup(s + "07.png", scale));

        movingAnim.addFrame(setup(s + "00.png", scale));
        movingAnim.addFrame(setup(s + "03.png", scale));

        actionAnim.addFrame(setup(s + "04.png", scale));
        actionAnim.addFrame(setup(s + "05.png", scale));
        actionAnim.addFrame(setup(s + "06.png", scale));
    }
}
