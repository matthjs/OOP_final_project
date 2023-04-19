package nl.rug.ai.oop.rpg.general;

/**
 * Entities. Characters being displays in battles or in the overworld or
 * battles. Keeps track location,
 * speed, the sprite images.
 * 
 * @author Matthijs
 * @author Niclas
 * @author Sanad
 *         Based on: RyiSnow (https://www.youtube.com/c/RyiSnow)
 */
public abstract class Entity {

	// entities position on world map
	private int worldX, worldY;
	private int speed;

	protected String direction;

	private int spriteCounter = 0;

	public enum SpriteID {
		SPRITE1, SPRITE2
	}

	private SpriteID spriteNum;
	private boolean collisionOn = false;

	/**
	 * For determining which variation of the sprite to display
	 * @author Matthijs
	 * @author Niclas
	 * @auhor Sanad
	 * @return
	 */
	public SpriteID getSpriteNum() {
		return spriteNum;
	}

	/**
	 * SpriteNum is an old procedure for managing sprite animations.
	 * Used in the overworld, but upgraded for battle entities.
	 * @author Matthijs
	 * @author Niclas
	 * @autor Sanad
	 * 
	 * @param spriteNum
	 */
	protected void setSpriteNum(SpriteID spriteNum) {
		this.spriteNum = spriteNum;
	}

	/**
	 * Entity X position in world map
	 * Ex: if (worldX, worldY) = (48, 48) then the entity is on tile (1, 1)
	 * because tileSize = 48
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @return
	 */
	public int getWorldX() {
		return worldX;
	}

	/**
	 * Entity Y position in world map
	 * Ex: if (worldX, worldY) = (48, 48) then the entity is on tile (1, 1)
	 * because tileSize = 48
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 */
	public int getWorldY() {
		return worldY;
	}

	/**
	 * sets Entity X position in world map
	 * Ex: if (worldX, worldY) = (48, 48) then the entity is on tile (1, 1)
	 * because tileSize = 48
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 */
	public void setWorldX(int worldX) {
		this.worldX = worldX;
	}

	/**
	 * sets Entity X position in world map
	 * Ex: if (worldX, worldY) = (48, 48) then the entity is on tile (1, 1)
	 * because tileSize = 48
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 */
	public void setWorldY(int worldY) {
		this.worldY = worldY;
	}

	/**
	 * Changes Entity X position in world map by amount
	 * Ex: if (worldX, worldY) = (48, 48) then the entity is on tile (1, 1)
	 * because tileSize = 48
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 */
	public void dWorldX(int amount) {
		this.worldX = this.worldX + amount;
	}

	/**
	 * Changes Entity Y position in world map by amount
	 * Ex: if (worldX, worldY) = (48, 48) then the entity is on tile (1, 1)
	 * because tileSize = 48
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 */
	public void dWorldY(int amount) {
		this.worldY = this.worldY + amount;
	}

	/**
	 * Gets speed
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @return
	 */
	protected int getSpeed() {
		return speed;
	}

	/**
	 * Sets speed
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @param speed
	 */
	protected void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Get direction (up, down, right, left)
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @return
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * Set direction (up, down, right, left)
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @return
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * Sprite counter is used as a buffer
	 * when updating sprites.
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @return
	 */
	protected int getSpriteCounter() {
		return spriteCounter;
	}

	/**
	 * Increment Sprite Counter 
	 * Sprite counter is used as a buffer
	 * when updating sprites.
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @return
	 */
	protected void incrementSpriteCounter() {
		spriteCounter++;
	}

	/**
	 * Reset sprite count
	 * Sprite counter is used as a buffer
	 * when updating sprites.
	 * 
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 * @return
	 */
	protected void resetSpriteCounter() {
		spriteCounter = 0;
	}

	/**
	 * True iff entity has collision
	 * @return
	 */
	protected Boolean hasCollision() {
		return collisionOn;
	}

	/**
	 * Sets collisionOn to false
	 * @author Matthijs
	 * @author Niclas
	 * @author Sanad
	 */
	protected void collisionOff() {
		collisionOn = false;
	}

	/**
	 * Sets collisionOn to true
	 * @author Matthijs
	 * @author Nilcas
	 * @author Sanad
	 */
	protected void collisionOn() {
		collisionOn = true;
	}

}