package nl.rug.ai.oop.rpg.battles.model.abilities;

import java.util.Map;

/**
 * IncrementalEffects are effects who have at each iteration step a certain
 * effect.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class IncrementalEffect extends AbstractEffect {

    /**
     * Constructor for creating an IncrementalEffect. If duration and
     * resetAfterDuration is not specified then
     * by default they arte set to 0 and false respectively. If no scale against
     * attribute is specified
     * then the effect will not negatively scale with any attribute.
     * 
     * @author Matthijs
     * @author Niclas
     * @param attributes
     * @param attribute
     * @param amount
     * @param attributeScaleWith
     */
    public IncrementalEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount) {
        super(attributes, attribute, amount, attributeScaleWith);
        this.amountToReset = -(amount * duration);
    }

    /**
     * Constructor for creating an IncrementalEffect. If no scale against attribute
     * is
     * specified
     * then the effect will not negatively scale with any attribute.
     * 
     * @author Matthijs
     * @author Niclas
     * @param attributes
     * @param attribute
     * @param amount
     * @param attributeScaleWith
     */
    public IncrementalEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount,
            int duration, Boolean resetAfterDuration) {
        super(attributes, attribute, amount, attributeScaleWith, duration, resetAfterDuration);
        this.amountToReset = -(amount * duration);
    }

    /**
     * Constructor for creating an IncrementalEffect. If duration and
     * resetAfterDuration is not specified then
     * by default they arte set to 0 and false respectively.
     * 
     * @author Matthijs
     * @author Niclas
     * @param attributes
     * @param attribute
     * @param amount
     * @param attributeScaleWith
     */
    public IncrementalEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount,
            String attributeScaleAgainst) {
        super(attributes, attribute, amount, attributeScaleWith, attributeScaleAgainst);
        this.amountToReset = -(amount * duration);
    }

    /**
     * Constructor for creating an IncrementalEffect. attribute: attribute that the
     * effect should have an effect on. attributeScaleWith and
     * attributeScaleAgainst influence how much the effect scales with amount.
     * Duration and resetAfterDuration is what
     * you expect. In practice a duration is a full turn for a specific battle
     * entity.
     * 
     * @author Matthijs
     * @author Niclas
     * @param attributes
     * @param attribute
     * @param amount
     * @param attributeScaleWith
     */
    public IncrementalEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount,
            String attributeScaleAgainst, int duration, Boolean resetAfterDuration) {
        super(attributes, attribute, amount, attributeScaleWith, attributeScaleAgainst, duration, resetAfterDuration);
        this.amountToReset = -(amount * duration);
    }

    /**
     * True when duration is over.
     * 
     * @author Matthijs
     */
    @Override
    public boolean applyEffect() {
        if (timer > duration) {
            if (resetAfterDuration) {
                System.out.println("resetting effect");
                resetEffect();
            }
            return true;
        }
        changeAttributeValue();
        return false;
    }

    /**
     * Equals when the Effects specifies the same thing.
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IncrementalEffect) {
            return getattributeScaleWith().equals(((AbstractEffect) obj).getattributeScaleWith()) &&
                    getAttributeScaleAgainst().equals(((AbstractEffect) obj).getAttributeScaleAgainst()) &&
                    getAmount().equals(((AbstractEffect) obj).getAmount())
                    && getDuration().equals(((AbstractEffect) obj).getDuration()) &&
                    hasResetAfterDuration().equals(((AbstractEffect) obj).hasResetAfterDuration());
        } else
            return false;
    }
}
