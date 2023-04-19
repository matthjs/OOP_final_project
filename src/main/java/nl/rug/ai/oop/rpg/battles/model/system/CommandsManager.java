package nl.rug.ai.oop.rpg.battles.model.system;

import java.util.ArrayList;
import java.util.List;

import nl.rug.ai.oop.rpg.battles.model.abilities.Command;

/**
 * Class that contains lists of commands
 *      0           1              N       M
 * 0 [command1, command2, ..., commandN]
 *  1 [command1, command2, ...,     commandM]
 *  ...
 * This is the class that holds the internal logic when
 * you navigate the menu in the battles.
 * @author Matthijs
 */
public class CommandsManager implements ListManager<Command> {
    private List<List<Command>> lists;
    int currentListIdx = 0;
    int currentCommandIdx = 0;

    public CommandsManager() {
        lists = new ArrayList<>();
    }

    /**
     * Gets currently selected command list
     * @author Matthijs
     */
    @Override
    public Command getCurrentlySelected() {
        return lists.get(currentListIdx).get(currentCommandIdx);
    }

    /**
     * Gets currently selected command
     * @author Matthijs
     */
    @Override
    public List<Command> getCurrentList() {
        return lists.get(currentListIdx);
    }

    /**
     * Resets list index to 0
     * @author Matthijs
     */
    public void resetListIdx() {
        currentListIdx = 0;
    }

    /**
     * Resets element index to 0
     * @author Matthijs
     */
    public void resetElementIdx() {
        currentCommandIdx = 0;
    }

    /**
     * Increases the element index by one for example if
     * currentElementIndex is j and currentListIdx is i
     *                  j                                           j+1
     * i [command1, command2, command3] -> i [command1, command2, command3]
     * then this will set currentElementIndex to j+1 (modulus the size of the list)
     * @author Matthijs
     */
    public void nextCommand() {
        currentCommandIdx = (currentCommandIdx + 1) % getCurrentList().size();
    }

    /**
     * Opposite of nextCommand
     * @author Matthijs
     */
    public void previousCommand() {
        currentCommandIdx--;
        if (currentCommandIdx < 0) {
            currentCommandIdx = currentCommandIdx + getCurrentList().size();
        }
        currentCommandIdx = currentCommandIdx % getCurrentList().size();
    }

    /**
     * Go to next list. If
     * currentElementIndex is j and currentListIdx is i then
     * j will be reset to 0 and currentListIdx set to i + 1 (modulus the amount of lists there are)
     *                  j             
     * i [command1, command2, command3]
     *          0
     * -> j [command1, command2, ...]
     *      
     * 
     * 
     * @author Matthijs
     */
    @Override
    public void nextList() {
        currentCommandIdx = 0;
        currentListIdx = (currentListIdx + 1) % lists.size();
    }

    /**
     * Opposite of nextList. If there are N lists and we are at list 0 then we get to list N
     * @author Matthijs
     */
    @Override
    public void previousList() {
        currentCommandIdx = 0;
        currentListIdx--;
        if (currentListIdx < 0) {
            currentListIdx = currentListIdx + lists.size();
        }
        currentListIdx = currentListIdx % lists.size();
        
    }

    /**
     * Adds lists of commands to internal list
     * @authro Matthijs
     */
    @Override
    public void addList(List<Command> l) {
        lists.add(l);
    }

    /**
     * Clears the internal list
     * @author Matthijs
     */
    @Override
    public void removeAll() {
        lists.clear();
    }

    /**
     * Returns true if the current list idx is at the final list
     * @author Matthijs
     * @return
     */
    public boolean atFinalCommand() {
        return currentListIdx == lists.size()-1;
    }

    /**
     * Removes the last list that was added
     * @author Matthijs
     */
    public void remove() {
        lists.remove(lists.size()-1);
    }

    /**
     *      0         ->1              N
     * ->0 [command1, command2, ..., commandN]
     *  1 [command1, command2, ..., commandN]
     * Gets current command index. In example returns 1
     * @author Matthijs
     * @return
     */
    public int getCurrentCommandIdx() {
        return currentCommandIdx;
    }
    
    /**
     *      0         ->1              N
     * ->0 [command1, command2, ..., commandN]
     *  1 [command1, command2, ..., commandN]
     * Gets current command list index. In example returns 0
     * @author Matthijs
     * @return
     */
    public int getCurrentListIdx() {
        return currentListIdx;
    }

    /**
     *      0         ->1              N
     * ->0 [command1, command2, ..., commandN]
     *  1 [command1, command2, ..., commandN]
     * Gets number of lists. In this example returns 2
     * @author Matthijs
     * @return
     */
    public int getNumberOfLists() {
        return lists.size();
    }
    
}
