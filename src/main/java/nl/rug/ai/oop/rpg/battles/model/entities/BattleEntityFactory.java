package nl.rug.ai.oop.rpg.battles.model.entities;

import nl.rug.ai.oop.rpg.general.FactoryReflection;
import nl.rug.ai.oop.rpg.general.Producable;
import nl.rug.ai.oop.rpg.battles.model.entities.party.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.rug.ai.oop.rpg.battles.model.entities.opponents.*;

/**
 * Factory for battle entities. Uses reflection.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class BattleEntityFactory extends FactoryReflection {
    private List<Class> extraPartyMembers = new ArrayList<>();

    /**
     * This constructor preloads all the battle entities.
     * extraPartyMembers are used to add party members as you progress in the game.
     * 
     * @author Matthijs
     * @author Niclas
     */
    public BattleEntityFactory() {
        registerProduct(Attacker.class);
        registerProduct(Defender.class);
        registerProduct(Mage.class);
        registerProduct(Warlock.class);
        registerProduct(Fighter.class);
        registerProduct(Slime.class);
        registerProduct(BigSlime.class);
        registerProduct(DarkMage.class);
        registerProduct(NeuralNetwork.class);
        registerProduct(Ninja.class);
        registerProduct(Mog.class);

        extraPartyMembers.add(Defender.class);
        extraPartyMembers.add(Warlock.class);
        extraPartyMembers.add(Fighter.class);
    }

    /**
     * Gets a random element from extraPartyMembers
     * 
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public Producable getNewRandomPartMember() {
        Random rand = new Random();
        int i = rand.nextInt(extraPartyMembers.size());
        Class c = extraPartyMembers.get(i);
        extraPartyMembers.remove(i);
        return createProduct(c.getSimpleName());
    }
}
