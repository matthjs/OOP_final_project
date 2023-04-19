package nl.rug.ai.oop.rpg.battles.model.system;

import java.util.List;

/**
 * Interface represents the functionality that is needed to
 * manage a list of lists with some abstraction.
 * @author Matthijs
 */
public interface ListManager<T> {
    
    public T getCurrentlySelected();

    public List<T> getCurrentList();

    public void addList(List<T> l);

    public void nextList();

    public void previousList();

    public void removeAll();
}
