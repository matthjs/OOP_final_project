package nl.rug.ai.oop.rpg.battles.model.abilities;

import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;

/**
 * The command interface has the method execute(BattleEntity e)
 * It encompasses the basic functionality of what a "command" should
 * do in the battles.
 * 
 * @author Matthijs
 */
public interface Command {
    public void execute(BattleEntity e);
}
