package nl.rug.ai.oop.rpg.battles.model.system;

import nl.rug.ai.oop.rpg.battles.model.entities.opponents.OpponentParty;
import nl.rug.ai.oop.rpg.battles.model.entities.party.MainParty;

/**
 * Builder for battle system
 * It first needs opponents and main party
 * and then consructs everything in the right order
 * @author Matthijs
 * @author Niclas
 */
public class BattleSystemBuilder {
    public OpponentParty opponentParty;
    public MainParty mainParty;
    
    /**
     * Sets opponent party that should be used when battle system is built
     * @author Mattijs
     * @param opponentParty
     * @return
     */
    public BattleSystemBuilder setOpponentParty(OpponentParty opponentParty) {
        this.opponentParty = opponentParty;
        return this;
    }

    /**
     * Sets main party that should be used when battle system is built
     * @author Matthijs
     * @param mainParty
     * @return
     */
    public BattleSystemBuilder setMainParty(MainParty mainParty) {
        this.mainParty = mainParty;
        return this;
    }

    /**
     * Checks whether builder has all components to built. Specifically opponentparty and mainparty
     * @author Matthijs
     * @return true if that is the case else false
     */
    private boolean hasAllComponents() {
        if (opponentParty == null) {
            throw new IllegalStateException("Opponent party is not assigned.");
        }
        if (mainParty == null) {
            throw new IllegalStateException("Main party is not set");
        }
        return true;
    }

    /**
     * Built battleSystem provided it has opponentparty and mainparty
     * @author Mattijs
     * @author Niclas
     * @return
     */
    public BattleSystem build() {
        if (hasAllComponents()) {
            BattleSystem buildTarget = BattleSystem.getInstance();
            buildTarget.setOpponentParty(opponentParty);
            buildTarget.setMainParty(mainParty);

            buildTarget.setCommandsManager();
            buildTarget.constructEntityList();
            return buildTarget;
        }
        return null;
    }
}
