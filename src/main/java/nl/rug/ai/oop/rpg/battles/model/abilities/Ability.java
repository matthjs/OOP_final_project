package nl.rug.ai.oop.rpg.battles.model.abilities;

import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;

/**
 * Abilities are the actions battle entities can perform.
 * The idea here is that classes that implement Ability need to give
 * their implementation of execute(). In practice are used as inner classes
 * to allow an battle entity to influence another battle entity.
 * 
 * @author Matthijs
 */
public abstract class Ability implements Command {
    String name;
    private Boolean targetOwn;

    public Ability() {
        setName("Generic Ability");
    }

    /**
     * targetOwn is used to determine whether the ability
     * should target the opponent or the party members
     * 
     * @author Matthijs
     * @param targetOwn
     */
    public void setTargetOwn(boolean targetOwn) {
        this.targetOwn = targetOwn;
    }

    /**
     * Getter for targetOwn
     * 
     * @author Matthijs
     * @return {True, False} indicating whether the ability should be used for own
     *         party members
     */
    public boolean targetOwn() {
        return targetOwn;
    }

    /**
     * e is the entity that the ability will have an effect on.
     * 
     * @author Matthijs
     * @param e
     */
    public abstract void execute(BattleEntity e);

    /**
     * Gets name of ability
     * @author Matthijs
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of ability
     * @author Matthijs
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
