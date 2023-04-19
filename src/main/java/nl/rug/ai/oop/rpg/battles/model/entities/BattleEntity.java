package nl.rug.ai.oop.rpg.battles.model.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.*;

import nl.rug.ai.oop.rpg.battles.model.abilities.*;
import nl.rug.ai.oop.rpg.general.*;
import java.util.List;
import java.util.Set;

/**
 * Entity that takes part in the battle
 * 
 * @author Matthijs
 * @author Niclas
 */
public abstract class BattleEntity extends Entity implements Drawable, Updatable, Producable {

	/**
	 * Sprite animation system
	 * Moves through the list at certain update speed
	 * 
	 * @author Matthijs
	 */
	private BufferedImage defaultImage; // what is used when any of the animation lists are empty
	private BufferedImage currentFrame; // what should the battle entity currently look like?

	protected List<Command> AttackAbilities = new ArrayList<>();
	protected List<Command> MagicAbilities = new ArrayList<>();

	protected Animation idleAnim = new Animation(); // animation when not moving
	protected Animation movingAnim = new Animation(); // animation when moving
	protected Animation damageAnim = new Animation(); // animation when taking damage
	protected Animation actionAnim = new Animation(); // animation when attacking

	private Set<Effect> effects = new LinkedHashSet<>(); // what effects are on the entity right now?

	protected final int SPRITEUPDATERATE = 12; // determines after how many frames the sprite should be updated
	protected final int ATTACKANIMATIONDURATION = 30; // a default measure for how long non-idle animations should take
														// (30 = frames = 0.5sec since the game runs at 60FPS)

	private int frames = 0; // used to determine for how many frames an animation should be played
	private int frameCounter = 0; // frameCounter to track for how many frames the animation has been played
	private boolean forwards; // determines whether the battle entity is moving forward or backwards

	/**
	 * Internal state of the battle entity. Mostly is used to display the
	 * appropriate animation, but is also used to have the model (battleSystem) wait
	 * for the animation or attack to finish.
	 * IDLE: when not moving, taking damage, activating an ability
	 * The rest are self explanatory hopefully.
	 * 
	 * @author Matthijs
	 */
	protected enum State {
		IDLE,
		MOVING,
		DAMAGE,
		ABILITY_ACTIVATE
	}

	protected State state = State.IDLE;

	/**
	 * Stats for BattleEntities.
	 * 
	 * @author Matthijs
	 */
	protected String name;
	protected HashMap<String, Integer> attributes = new HashMap<>();

	public BattleEntity() {
		configureAttributes();
		setDefaultValues();
		setAbilities();
		setEntityImage();
		setSpeed(2);
	}

	/**
	 * Here the attributes are defined and temporarely set to 0.
	 * 
	 * @author Matthijs
	 */
	protected void configureAttributes() {
		attributes.put("maxHP", 0);
		attributes.put("HP", 0);
		attributes.put("maxMP", 0);
		attributes.put("MP", 0);
		attributes.put("physPow", 0);
		attributes.put("physDef", 0);
		attributes.put("magicPow", 0);
		attributes.put("magicDef", 0);
	}

	/**
	 * When the main model (BattleSystem) updates during the game loop
	 * this method is run to respectively update the player
	 * (1) updates (x, y) if applicable
	 * (2) updates sprite information (but does not draw them yet)
	 * 
	 * @author Matthijs
	 */
	public void update() {
		updateInfo();
		updateSpriteInfo();
	}

	/**
	 * (1) updates (x, y) if applicable
	 * 
	 * @author Matthijs
	 * @author Niclas
	 */
	private void updateInfo() {
		if (state == State.MOVING) {
			if (getframeCounter() > getFrames()) {
				state = State.IDLE;
				resetframeCounter();
			} else {
				switch (getDirection()) {
					case "right":
						if (forwards) {
							dWorldX(getSpeed());
						} else {
							dWorldX(-getSpeed());
						}
						break;
					case "left":
						if (forwards) {
							dWorldX(-getSpeed());
						} else {
							dWorldX(getSpeed());
						}
						break;
				}
				incrementframeCounter();
			}
		}
	}

	/**
	 * (2) updates sprite information (but does not draw them yet)
	 * 
	 * @author Matthijs
	 */
	private void updateSpriteInfo() {
		if (state == State.ABILITY_ACTIVATE || state == State.DAMAGE) {
			if (getframeCounter() > getFrames()) {
				state = State.IDLE;
				resetframeCounter();
			} else {
				incrementframeCounter();
			}
		}

		incrementSpriteCounter();

		// player image changes
		if (getSpriteCounter() > SPRITEUPDATERATE) {
			switch (state) {
				case IDLE:
					if (idleAnim.hasNoAnimation()) {
						currentFrame = getDefaultImage();
					} else {
						currentFrame = idleAnim.getFrame();
						idleAnim.modeToNextFrame();
					}
					break;
				case MOVING:
					if (movingAnim.hasNoAnimation()) {
						currentFrame = getDefaultImage();
					} else {
						currentFrame = movingAnim.getFrame();
						movingAnim.modeToNextFrame();
					}
					break;
				case DAMAGE:
					if (damageAnim.hasNoAnimation()) {
						currentFrame = getDefaultImage();
					} else {
						currentFrame = damageAnim.getFrame();
						damageAnim.modeToNextFrame();
					}
					break;
				case ABILITY_ACTIVATE:
					if (actionAnim.hasNoAnimation()) {
						currentFrame = getDefaultImage();
					} else {
						currentFrame = actionAnim.getFrame();
						actionAnim.modeToNextFrame();
					}
					break;
			}
			resetSpriteCounter();
		}
	}

	/**
	 * Draws the appropriate sprite at the appropriate position on the world map
	 * Assumption
	 * 
	 * @author Matthijs
	 */
	@Override
	public void draw(Graphics2D g2) {

		BufferedImage image = currentFrame;

		g2.drawImage(image, getWorldX(), getWorldY(), null);
	}

	/**
	 * Moves the entity forward or backward for a given amount of
	 * frames.
	 * 
	 * @author Matthijs
	 * @param amountOfFrames
	 */
	public void move(int amountOfFrames, Boolean forward) {
		state = State.MOVING;
		setframes(amountOfFrames);
		forwards = forward;
	}

	/**
	 * Sets the state to ABILITY_ACTIVATE and the animation being played
	 * will take amountOfFrames.
	 * 
	 * @author Matthijs
	 * @param amountOfFrames
	 */
	public void setStateToAbilityActivate(int amountOfFrames) {
		state = State.ABILITY_ACTIVATE;
		setframes(amountOfFrames);
	}

	/**
	 * Sets the state to DAMAGE and the animation being played will
	 * take amountOfFrames.
	 * 
	 * @author Matthijs
	 * @param amountOfFrames
	 */
	public void setStateToDamage(int amountOfFrames) {
		state = State.DAMAGE;
		setframes(amountOfFrames);
	}

	/**
	 * Applies the given effects.
	 * 
	 * @author Matthijs
	 */
	public void applyEffects() {
		if (!effects.isEmpty()) {
			Iterator<Effect> i = effects.iterator();
			while (i.hasNext()) {
				AbstractEffect eff = (AbstractEffect) i.next();
				if (eff.applyEffect()) {
					i.remove();
				} else {
					((AbstractEffect) eff).incrementTimer();
				}
			}
		}
	}

	/**
	 * Gets all attributes
	 * @author Matthijs
	 * @author Niclas
	 * @return hashmap of all attributes
	 */
	public HashMap<String, Integer> getAttributes() {
		return attributes;
	}

	/**
	 * Adds an effect to the entity effects set.
	 * Effects are applied when applyEffects() is run.
	 * @author Matthijs
	 * @author Niclas
	 * @param eff
	 */
	public void addEffect(Effect eff) {
		effects.add(eff);
	}

	/**
	 * Set the default abilities a certain BattleEntity should have.
	 * 
	 * @author Matthijs
	 *         Spawn point, speed
	 */
	protected abstract void setAbilities();

	/**
	 * Set default MaxHP, HP, MaxMP, MP, agility etc...
	 * 
	 * @author Matthijs
	 *         Spawn point, speed
	 */
	protected abstract void setDefaultValues();

	/**
	 * Load sprite data.
	 * 
	 * @author Matthijs
	 *         Spawn point, speed
	 */
	protected abstract void setEntityImage();

	/**
	 * A battle entity is considere dead iff its HP < 0.
	 * 
	 * @author Matthijs
	 * @return
	 */
	public boolean isDead() {
		return getHP() <= 0;
	}

	/**
	 * Adds an ability with a name. What is usually passed is an anonymous inner
	 * class
	 * For example Heal can be specified to cost 10MP and increase the targets HP by
	 * 50.
	 * 
	 * @author Matthijs
	 * @param a
	 * @param name
	 */
	public void addAbility(Ability a, String name) {
		addAbility(a, name, false);
	}

	/**
	 * Adds an ability with a name. What is usually passed is an anonymous inner
	 * class.
	 * 
	 * @author Matthijs
	 * @param a
	 * @param name
	 */
	public void addAbility(Ability a, String name, Boolean targetOwn) {
		a.setTargetOwn(targetOwn);
		if (a instanceof MagicAbility) {
			a.setName(name);
			MagicAbilities.add(a);
		}
		if (a instanceof AttackAbility) {
			a.setName(name);
			AttackAbilities.add(a);
		}
	}

	/**
	 * Gets abilities that are magic abilities
	 * 
	 * @author Matthijs
	 * @return
	 */
	public List<Command> getMagicAbilities() {
		return MagicAbilities;
	}

	/**
	 * Gets abilities that are non-magic (attack) abilities
	 * 
	 * @author Matthijs
	 * @return
	 */
	public List<Command> getAttackAbilities() {
		return AttackAbilities;
	}

	public Boolean isIdle() {
		return state == State.IDLE;
	}

	/**
	 * Sets default image (sprite) that gets displayed when
	 * any of the animation lists are empty. It is also
	 * the image that gets displayed in the entity order at the top
	 * of the UI
	 * 
	 * @author Matthijs
	 * @return
	 */
	public BufferedImage getDefaultImage() {
		return defaultImage;
	}

	/**
	 * Also sets currentFrame
	 * 
	 * @param defaultImage
	 */
	public void setDefaultImage(BufferedImage defaultImage) {
		this.defaultImage = defaultImage;
		this.currentFrame = defaultImage;
	}

	/**
	 * frames is used to determine for how many frames an animation should be
	 * played.
	 * 
	 * @author Matthijs
	 * @param frames
	 */
	private void setframes(int frames) {
		this.frames = frames;
	}

	/**
	 * frames is used to determine for how many frames an animation should be
	 * played.
	 * 
	 * @author Matthijs
	 * 
	 * @return
	 */
	private int getFrames() {
		return frames;
	}

	/**
	 * frameCounter to track for how many frames the animation has been played.
	 * 
	 * @author Matthijs
	 */
	private void incrementframeCounter() {
		frameCounter++;
	}

	/**
	 * frameCounter to track for how many frames the animation has been played.
	 * 
	 * @author Matthijs
	 */
	private void resetframeCounter() {
		frameCounter = 0;
	}

	/**
	 * frameCounter to track for how many frames the animation has been played.
	 * 
	 * @author Matthijs
	 * @return
	 */
	private int getframeCounter() {
		return frameCounter;
	}

	/**
	 * Sets the attribute with a given value.
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @throws IllegalArgumentException when attribute does not have a setter or
	 *                                  does not exist
	 * @param attribute
	 * @param value
	 */
	public void setAttribute(String attribute, Integer value) {
		if (attributes.get(attribute) != null) {
			switch (attribute) {
				case "maxHP":
					setMaxHP(value);
					break;
				case "HP":
					setHP(value);
					break;
				case "maxMP":
					setMaxMP(value);
					break;
				case "MP":
					setMP(value);
					break;
				case "physPow":
					setPhysPow(value);
					break;
				case "physDef":
					setPhysDef(value);
					break;
				case "magicPow":
					setMagicPow(value);
					break;
				case "magicDef":
					setMagicDef(value);
					break;
				default:
					throw new IllegalArgumentException("attribute exists but is not settable");
			}
		} else
			throw new IllegalArgumentException("attribute does not exist");
	}

	/**
	 * Sets name
	 * @author Matthijs
	 * @author Niclas
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets name
	 * @author Matthijs
	 * @author Niclas
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the HP.
	 * @author Matthijs
	 * @author Niclas
	 * @param HP
	 * @throws IllegalArgumentException whenever HP value > maxHP
	 */
	public void setHP(int HP) {
		if (HP <= getMaxHP()) {
			attributes.computeIfPresent("HP", (k, v) -> HP);
		} else
			throw new IllegalArgumentException("Illegal assignment: HP cannot be set to value greater than maxHP");
	}

	/**
	 * Change the HP by an amount. If HP + amount > maxHP then sets to maxHP
	 * d stands for increasing something by some amount
	 * 
	 * @author Matthijs
	 * @author Niclas
	 */
	public void dHP(int amount) {
		if (getHP() + amount > getMaxHP()) {
			attributes.computeIfPresent("HP", (k, v) -> getMaxHP());
		} else
			attributes.computeIfPresent("HP", (k, v) -> v + amount);
	}

	/**
	 * Gets HP
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public Integer getHP() {
		return attributes.get("HP");
	}

	/**
	 * Sets max HP
	 * @author Matthijs
	 * @author Niclas
	 * @param maxHP
	 */
	public void setMaxHP(int maxHP) {
		attributes.computeIfPresent("maxHP", (k, v) -> maxHP);
	}

	/**
	 * Get max HP
	 * @author Matthijs
	 * @author Niclas
	 * @return maxHP
	 */
	public Integer getMaxHP() {
		return attributes.get("maxHP");
	}

	/**
	 * Sets MP
	 * @author Matthijs
	 * @author Niclas
	 * @param MP
	 * @throws IllegalArgumentException whenever MP > maxMP
	 */
	public void setMP(int MP) {
		if (MP <= getMaxMP()) {
			attributes.computeIfPresent("MP", (k, v) -> MP);
		} else
			throw new IllegalArgumentException("Illegal assignment: MP cannot be set to value greater than maxMP");
	}

	/**
	 * Changes MP by amount. If MP + amount > maxMP sets to maxMP
	 * @author Matthijs
	 * @author Niclas
	 * @param amount
	 */
	public void dMP(int amount) {
		if (getMP() + amount > getMaxMP()) {
			attributes.computeIfPresent("MP", (k, v) -> getMaxMP());
		} else
			attributes.computeIfPresent("MP", (k, v) -> v + amount);
	}

	/**
	 * Gets MP
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public Integer getMP() {
		return attributes.get("MP");
	}

	/**
	 * Sets max MP
	 * @author Matthijs
	 * @author Niclas
	 * @param maxMP
	 */
	public void setMaxMP(int maxMP) {
		attributes.computeIfPresent("maxMP", (k, v) -> maxMP);
	}

	/**
	 * gets max MP
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public Integer getMaxMP() {
		return attributes.get("maxMP");
	}

	/**
	 * Sets physical defense
	 * @author Matthijs
	 * @author Niclas
	 * @param physDef
	 */
	public void setPhysDef(int physDef) {
		attributes.computeIfPresent("physDef", (k, v) -> physDef);
	}

	/**
	 * Gets physical defense
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public int getPhysDef() {
		return attributes.get("physDef");
	}

	/**
	 * Sets magical defense
	 * @author Matthijs
	 * @author Niclas
	 * @param magicDef
	 */
	public void setMagicDef(int magicDef) {
		attributes.computeIfPresent("magicDef", (k, v) -> magicDef);
	}

	/**
	 * Gets magical defense
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public int getMagicDef() {
		return attributes.get("magicDef");
	}

	/**
	 * Sets physical power
	 * @author Matthijs
	 * @author Niclas
	 * @param physPow
	 */
	public void setPhysPow(int physPow) {
		attributes.computeIfPresent("physPow", (k, v) -> physPow);
	}

	/**
	 * Gets physical power
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public int getPhysPow() {
		return attributes.get("physPow");
	}

	/**
	 * Sets magical power
	 * @author Matthijs
	 * @author Niclas
	 * @param magicPow
	 */
	public void setMagicPow(int magicPow) {
		attributes.computeIfPresent("magicPow", (k, v) -> magicPow);
	}

	/**
	 * Gets magical power
	 * @author Matthijs
	 * @author Niclas
	 * @return
	 */
	public int getMagicPow() {
		return attributes.get("magicPow");
	}

	/**
	 * Loads image without rescaling
	 * @author Matthijs
	 * @author Niclas
	 * @param source
	 * @return Unscaled BufferedImage
	 */
	protected BufferedImage setup(String source) {
		return setup(source, false);
	}

	/**
	 * Loads and scales image
	 * 
	 * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
	 * @return Scaled BufferedImage
	 */
	protected BufferedImage setup(String source, Boolean scaleImageToTileSize) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;

		try {
			image = ImageIO.read(getClass().getResourceAsStream(source));
			if (scaleImageToTileSize) {
				image = uTool.scaleImage(image, GameView.tileSize, GameView.tileSize);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Loads and scales image
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @return Scaled Image
	 */
	protected BufferedImage setup(String source, int manualScale) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;

		try {
			image = ImageIO.read(getClass().getResourceAsStream(source));
			image = uTool.scaleImage(image, manualScale, manualScale);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public String toString() {
		return getName();
	}
}
