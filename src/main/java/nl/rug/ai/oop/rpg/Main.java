package nl.rug.ai.oop.rpg;

import nl.rug.ai.oop.rpg.general.ControlManager;

/**
 * Main class. Starts the game.
 * The ControlManager object is the central object of the program.
 * @author Matthijs
 * @author Niclas
 * @author Sanad
 */
public class Main {
    public static void main(String[] args) {
        ControlManager game = new ControlManager();
        game.startGameThread();
    }
}
