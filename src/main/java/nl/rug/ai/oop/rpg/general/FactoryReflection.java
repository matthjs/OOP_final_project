package nl.rug.ai.oop.rpg.general;

import java.util.Collection;
import java.util.HashMap;

/**
 * Factory that uses refecction to create objects
 * @author Matthijs
 * @author Nilcas
 */
public class FactoryReflection {
	private HashMap<String, Class> register = new HashMap<>();

	/**
	 * Registers an AbstractProduct to be producable by the factory
	 * @author Mathijs
	 * @author Niclas
	 * @param identifier identifier to use for this AbstractProduct
	 * 					automatically set to string .getClass().getSimpleName()
	 * @param prototype prototypical AbstractProduct
	 */
	public void registerProduct(Class productClass) {
		String identifier = productClass.getSimpleName();
		register.put(identifier, productClass);
	}

	/**
	 * Creates one of several possible Producables
	 * @author Matthijs
	 * @author Nilcas
	 * @param identifier identifier of the Producable to create
	 * @return newly cloned Producables corresponding to the given identifier
	 */
	public Producable createProduct(String identifier) {
		try {
			Class productClass = register.get(identifier);
			return (Producable)productClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Register product class
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public Collection<Class> getRegisteredProducts() {
		return register.values();
	}
}

