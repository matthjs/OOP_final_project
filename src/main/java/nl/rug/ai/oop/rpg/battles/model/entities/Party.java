package nl.rug.ai.oop.rpg.battles.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Party is a decorator in a way for a list of battle entities.
 * It abstracts away the underlying list and provides only the necessary
 * methods.
 * 
 * @author Matthijs
 * @author Niclas
 */

public abstract class Party {
    private List<BattleEntity> party = new ArrayList<>();

    /**
     * Gets the underlying list
     * 
     * @author Matthijs
     * @author Niclas
     * @return List of BattleEntities that are in the Party
     */
    public List<BattleEntity> getMembers() {
        return party;
    }

    /**
     * Functions the same as the get() method for lists
     * 
     * @author Matthijs
     * @author Niclas
     * @param i
     * @return the i-th party member
     */
    public BattleEntity getMember(int i) {
        return getMembers().get(i);
    }

    /**
     * Adds a player to the game. Can only be performed when the game
     * is not currently being played.
     * 
     * @author Matthijs
     * @author Niclas
     * @param player party member that is to be added
     */
    public void addPartyMember(BattleEntity p) {
        party.add(p);
    }

    /**
     * Remove party member
     * 
     * @author Matthijs
     * @author Niclas
     * @param p
     */
    public void removePartyMember(BattleEntity p) {
        party.remove(p);
    }

    /**
     * Get a random member from the party.
     * 
     * @author Matthijs
     * @author Niclas
     * @return a random battle entity from the party
     */
    public BattleEntity getRandomMember() {
        return party.get(new Random().nextInt(party.size()));
    }

    /**
     * Removes all party members from the party.
     * 
     * @author Matthijs
     * @author Niclas
     */
    public void clear() {
        party.clear();
    }

    /**
     * Returns the amount of party members in the party.
     * 
     * @author Matthijs
     * @author Niclas
     */
    public int size() {
        return getMembers().size();
    }

}
