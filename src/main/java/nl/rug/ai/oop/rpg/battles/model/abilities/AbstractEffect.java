package nl.rug.ai.oop.rpg.battles.model.abilities;

import java.util.Map;

/**
 * Abstract effect is a template for creating effects amd implements the Effects
 * interface.
 * In practice effects are placed on a battle entity when usign an ability.
 * Think
 * of effects as for ex: increase HP by x amount or decrease physDef by x for n
 * number of turns.
 * 
 * @author Matthijs
 * @author Niclas
 */
public abstract class AbstractEffect implements Effect {
    private final double BASESCALEFACTOR = 0.2;
    private Map<String, Integer> attributes;

    private String attribute;
    private String attributeScaleWith;
    private String attributeScaleAgainst;
    private Integer amount;
    protected Integer amountToReset;
    protected int timer = 0;
    protected Integer duration;
    protected Boolean resetAfterDuration;

    /**
     * Constructor for creating an effect. If duration and resetAfterDuration is not
     * specified then
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
    public AbstractEffect(Map<String, Integer> attributes, String attribute, int amount, String attributeScaleWith) {
        this(attributes, attribute, amount, attributeScaleWith, 0, false);
    }

    /**
     * Constructor for creating an effect. If no scale against attribute is
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
    public AbstractEffect(Map<String, Integer> attributes, String attribute, int amount, String attributeScaleWith,
            int duration, Boolean resetAfterDuration) {
        if (attributes.get(attributeScaleWith) != null && attributes.get(attribute) != null) {
            this.attribute = attribute;
            this.attributes = attributes;
            this.attributeScaleWith = attributeScaleWith;
            if (amount < 0) {
                this.amount = (int) (amount - (BASESCALEFACTOR * attributes.get(attributeScaleWith)));
                if (this.amount > 0) {
                    this.amount = 0;
                }
            } else {
                this.amount = (int) (amount + (BASESCALEFACTOR * attributes.get(attributeScaleWith)));
                if (this.amount < 0) {
                    this.amount = 0;
                }
            }
            this.duration = duration;
            this.resetAfterDuration = resetAfterDuration;
        } else
            throw new IllegalArgumentException("attributeScaleWith to have effect on is not in attributes of entity");
    }

    /**
     * Constructor for creating an effect. If duration and resetAfterDuration is not
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
    public AbstractEffect(Map<String, Integer> attributes, String attribute, int amount, String attributeScaleWith,
            String attributeScaleAgainst) {
        this(attributes, attribute, amount, attributeScaleWith, attributeScaleAgainst, 0, false);
    }

    /**
     * Constructor for creating an effect. attribute: attribute that the effect
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
    public AbstractEffect(Map<String, Integer> attributes, String attribute, int amount, String attributeScaleWith,
            String attributeScaleAgainst, int duration, Boolean resetAfterDuration) {
        if (attributes.get(attributeScaleWith) != null && attributes.get(attributeScaleAgainst) != null
                && attributes.get(attribute) != null) {
            this.attribute = attribute;
            this.attributeScaleAgainst = attributeScaleAgainst;
            this.attributes = attributes;
            this.attributeScaleWith = attributeScaleWith;
            if (amount < 0) {
                this.amount = (int) (amount + (BASESCALEFACTOR * attributes.get(attributeScaleAgainst))
                        - (BASESCALEFACTOR * attributes.get(attributeScaleWith)));
                if (this.amount > 0) {
                    this.amount = 0;
                }
            } else {
                this.amount = (int) (amount - (BASESCALEFACTOR * attributes.get(attributeScaleAgainst))
                        + (BASESCALEFACTOR * attributes.get(attributeScaleWith)));
                if (this.amount < 0) {
                    this.amount = 0;
                }
            }
            this.duration = duration;
            this.resetAfterDuration = resetAfterDuration;
        } else
            throw new IllegalArgumentException("attributeScaleWith to have effect on is not in attributes of entity");
    }

    /**
     * Get the attribute that the effect scales with
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public String getattributeScaleWith() {
        return attributeScaleWith;
    }

    /**
     * Get the attribute that the effect scales against
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public String getAttributeScaleAgainst() {
        return attributeScaleAgainst;
    }

    /**
     * Get the base amount of the effect
     * @auhor Matthijs
     * @author Niclas
     * @return
     */
    public Integer getAmount() {
        return amount;
    }

    public Integer getDuration() {
        return duration;
    }

    /**
     * Returns true if the effect should reset after the duration is over
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public Boolean hasResetAfterDuration() {
        return resetAfterDuration;
    }

    /**
     * Changes attribute of entity on which the effect is applied.
     * With HP and MP the maximum is considered.
     * 
     * @author Matthijs
     * @author Niclas
     */
    protected void changeAttributeValue() {
        if (attribute.equals("HP") || attribute.equals("MP")) {
            if (attributes.get(attribute) + amount > attributes.get("max"+attribute)) {
                attributes.computeIfPresent(attribute, (k, v) -> attributes.get("max"+attribute));
            } else {
                attributes.computeIfPresent(attribute, (k, v) -> v + amount);
            }
        } else {
            attributes.computeIfPresent(attribute, (k, v) -> v + amount);
        }
    }


    /**
     * If an effect is specified to cancel out after a certain amount of time
     * then this method is used to do so.
     * 
     * @author Matthijs
     * @author Niclas
     */
    protected void resetEffect() {
        attributes.computeIfPresent(attribute, (k, v) -> v + amountToReset);
    }

    /**
     * Increments the timer. Timer is used to check the duration of an effect.
     * @author Matthijs
     * @author Niclas
     */
    public void incrementTimer() {
        timer++;
    }
}
