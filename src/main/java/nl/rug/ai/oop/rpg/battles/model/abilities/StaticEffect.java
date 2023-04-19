package nl.rug.ai.oop.rpg.battles.model.abilities;

import java.util.Map;

/**
 * Statics effects have a constant effect for a certain duration.
 * For example: a debuff that lowers physDef for 5 turns.
 * 
 * @author Matthijs
 * @author Niclas
 */
public class StaticEffect extends AbstractEffect {
    private Boolean applied = false;

    /**
     * Constructor for creating a StaticEffect. If duration and resetAfterDuration
     * is not specified then
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
    public StaticEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount) {
        super(attributes, attribute, amount, attributeScaleWith);
        this.amountToReset = -amount;
    }

    /**
     * Constructor for creating a StaticEffect. If no scale against attribute is
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
    public StaticEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount,
            int duration, Boolean resetAfterDuration) {
        super(attributes, attribute, amount, attributeScaleWith, duration, resetAfterDuration);
        this.amountToReset = -amount;
    }

    /**
     * Constructor for creating a StaticEffect. If duration and resetAfterDuration
     * is not
     * specified then
     * by default they are set to 0 and false respectively.
     * 
     * @author Matthijs
     * @author Niclas
     * @param attributes
     * @param attribute
     * @param amount
     * @param attributeScaleWith
     */
    public StaticEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount,
            String attributeScaleAgainst) {
        super(attributes, attribute, amount, attributeScaleWith, attributeScaleAgainst);
        this.amountToReset = -amount;
    }

    /**
     * Constructor for creating a StaticEffect. attribute: attribute that the effect
     * should have an effect on. attributeScaleWith and
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
    public StaticEffect(Map<String, Integer> attributes, String attribute, String attributeScaleWith, int amount,
            String attributeScaleAgainst, int duration, Boolean resetAfterDuration) {
        super(attributes, attribute, amount, attributeScaleWith, attributeScaleAgainst, duration, resetAfterDuration);
        this.amountToReset = -amount;
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
                resetEffect();
            }
            return true;
        }

        if (!applied) {
            changeAttributeValue();
        } else {
            applied = true;
        }
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
        if (obj instanceof StaticEffect) {
            return getattributeScaleWith().equals(((AbstractEffect) obj).getattributeScaleWith()) &&
                    getAmount() == ((AbstractEffect) obj).getAmount()
                    && getDuration() == ((AbstractEffect) obj).getDuration() &&
                    hasResetAfterDuration() == ((AbstractEffect) obj).hasResetAfterDuration();
        } else
            return false;
    }

}
