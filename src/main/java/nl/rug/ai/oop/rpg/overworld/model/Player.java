package nl.rug.ai.oop.rpg.overworld.model;

import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntityFactory;
import nl.rug.ai.oop.rpg.battles.model.entities.party.MainParty;
import nl.rug.ai.oop.rpg.general.*;
import nl.rug.ai.oop.rpg.general.tile.Tile;
import nl.rug.ai.oop.rpg.overworld.view.OverworldTileManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**
 * Class responsible for the main player in the Overworld
 * 
 * @author sanad
 */
public class Player extends Entity {
	/**
	 * The model's listeners
	 */
	private Collection<PropertyChangeListener> listeners = new ArrayList<>();

	/**
	 * The main party of the overworld player
	 */
	private MainParty mainParty;

	/**
	 * A counter for the number of frames the character has been standing
	 */
	private int standCounter = 0;

	/**
	 * Each boolean variable indicates the intended direction of the player
	 */
	private boolean movingDown, movingUp, movingLeft, movingRight;

	/**
	 * A boolean variable to check whether the player is intending to check their
	 * inventory
	 */
	private boolean searchingInventory = false;

	/**
	 * Variable which indicates the current game state of the player
	 * 0: Game screen
	 * 1: Overworld
	 * 2: Battle
	 * 3: Pause
	 * 4: Load old game
	 * 5: Terminate game
	 * 6: In battle
	 * 7: Game concluded
	 * 8: Info tab
	 */
	private static int gameState = 0;

	/**
	 * Number of battles won
	 */
	private int battlesWon = 0;

	/**
	 * Number of keys obtained
	 */
	private int keysObtained = 0;

	/**
	 * Static class variable to indicate whether this is a loaded progress for player
	 */
	public static boolean loadedGame = false;

	/**
	 * Getter method for keys obtained
	 * 
	 * @return number of keys obtained
	 */
	public int getKeysObtained() {
		return keysObtained;
	}

	/**
	 * Method to increment the number of keys obtained
	 */
	public void incrementKeysObtained() {
		keysObtained++;
	}

	/**
	 * Getter method for battlesWon
	 * 
	 * @return number of battles won
	 */
	public int getBattlesWon() {
		return battlesWon;
	}

	/**
	 * Getter method for the game state.
	 * 
	 * @return the game state.
	 */
	public int getGameState() {
		return gameState;
	}

	/**
	 * Setter method for the game state.
	 * 
	 * @param gameState the game state.
	 */
	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	/**
	 * Setter method for the searching inventory variable
	 * 
	 * @param isSearching true/false
	 */
	public void setSearchingInventory(boolean isSearching) {
		searchingInventory = isSearching;
	}

	/**
	 * Method which returns whether the player is indeed searching their inventory
	 * 
	 * @return true/false
	 */
	public boolean isSearchingInventory() {
		return searchingInventory;
	}

	/**
	 * Increment number of battles won
	 */
	public void incrementBattlesWon() {
		battlesWon++;
	}

	/**
	 * Constructor method for the player
	 */
	public Player() {
		setWorldX(GameView.tileSize * 23);
		setWorldY(GameView.tileSize * 21);
		setSpeed(4);
		setDirection("down");
	}

	/**
	 * Getter method for the mainParty variable
	 * 
	 * @return the main party
	 */
	public MainParty getMainParty() {
		return mainParty;
	}

	/**
	 * Setter method for the mainParty variable
	 * 
	 * @param mainParty the main party
	 */
	public void setMainParty(MainParty mainParty) {
		this.mainParty = mainParty;
	}

	/**
	 * Method to add listener to changes in properties of model
	 * 
	 * @param propertyChangeListener the property change listener
	 */
	public void addListener(PropertyChangeListener propertyChangeListener) {
		listeners.add(propertyChangeListener);
	}

	/**
	 * Getter method for the x position of the player on the screen
	 * 
	 * @return the player's x position on the screen
	 */
	public int getScreenX() {
		return GameView.screenWidth / 2 - (GameView.tileSize / 2);
	}

	/**
	 * Getter method for the y position of the player on the screen
	 * 
	 * @return the player's y position on the screen
	 */
	public int getScreenY() {
		return GameView.screenHeight / 2 - (GameView.tileSize / 2);
	}

	/**
	 * Auxiliary setter method for the direction of the player
	 * 
	 * @param direction the direction
	 */
	public void setDirection(String direction) {
		this.direction = direction;
		this.notifyListeners();
	}

	/**
	 * Main setter direction method, used in the controller class.
	 * 
	 * @param direction the intended direction
	 */
	public void setDirectionOfMovement(String direction) {
		switch (direction) {
			case "up" -> {
				movingUp = true;
				movingDown = false;
				movingRight = false;
				movingLeft = false;
			}
			case "down" -> {
				movingUp = false;
				movingDown = true;
				movingRight = false;
				movingLeft = false;
			}
			case "right" -> {
				movingUp = false;
				movingDown = false;
				movingRight = true;
				movingLeft = false;
			}
			case "left" -> {
				movingUp = false;
				movingDown = false;
				movingRight = false;
				movingLeft = true;
			}
		}
	}

	/**
	 * Method to indicate that the player is no longer moving in that direction
	 * 
	 * @param direction the old direction
	 */
	public void stopDirectionOfMovement(String direction) {
		switch (direction) {
			case "up" -> movingUp = false;
			case "down" -> movingDown = false;
			case "right" -> movingRight = false;
			case "left" -> movingLeft = false;
		}
	}

	/**
	 * Method to notify listeners of changes in relevant properties of model
	 */
	private void notifyListeners() {
		PropertyChangeEvent event = new PropertyChangeEvent(this, "property",
				null, getBattlesWon());
		for (PropertyChangeListener listener : listeners) {
			listener.propertyChange(event);
		}
		event = new PropertyChangeEvent(this, "property",
				null, getKeysObtained());
		for (PropertyChangeListener listener : listeners) {
			listener.propertyChange(event);
		}
	}

	/**
	 * Method to check whether the player has collided with any tiles that are solid
	 * and prevent movement
	 * 
	 * @param tileManager the overworld's tile manager
	 */
	public void checkCollision(OverworldTileManager tileManager) {
		/**
		 * Find the column and row the player is currently in
		 */
		int playerColumn = getWorldX() / GameView.tileSize;
		int playerRow = getWorldY() / GameView.tileSize;
		int tileNumber;
		Tile tile;
		switch (getDirection()) {
			case "up" -> {
				/**
				 * Find the tile that coincides with the player's tile's top right corner
				 */
				int entityTopRow = (getWorldY() - getSpeed()) / GameView.tileSize;
				tileNumber = tileManager.getMapTileNum()[playerColumn][entityTopRow];
				/**
				 * Tile number 42 indicates that the player has hit a special tile, which
				 * moves the player into battle mode
				 */
				if (tileNumber == 42) {
					gameState = 2;
				} else {
					tile = tileManager.getTile()[tileNumber];
					if (tile.hasCollision()) {
						this.collisionOn();
					}
				}
			}
			case "down" -> {
				int entityBottomRow = (getWorldY() + getSpeed()) / GameView.tileSize;
				tileNumber = tileManager.getMapTileNum()[playerColumn][entityBottomRow];
				if (tileNumber == 42) {
					gameState = 2;
				} else {
					tile = tileManager.getTile()[tileNumber];
					if (tile.hasCollision()) {
						this.collisionOn();
					}
				}
			}
			case "right" -> {
				int entityLeftCol = (getWorldX() + getSpeed()) / GameView.tileSize;
				tileNumber = tileManager.getMapTileNum()[entityLeftCol][playerRow];
				if (tileNumber == 42) {
					gameState = 2;
				} else {
					tile = tileManager.getTile()[tileNumber];
					if (tile.hasCollision()) {
						this.collisionOn();
					}
				}
			}
			case "left" -> {
				int entityRightCol = (getWorldX() - getSpeed()) / GameView.tileSize;
				tileNumber = tileManager.getMapTileNum()[entityRightCol][playerRow];
				if (tileNumber == 42) {
					gameState = 2;
				} else {
					tile = tileManager.getTile()[tileNumber];
					if (tile.hasCollision()) {
						this.collisionOn();
					}
				}
			}
		}
	}

	/**
	 * Method to check whether the player is moving in any direction
	 * 
	 * @return true/false
	 */
	public boolean isPlayerMoving() {
		return (movingLeft || movingRight || movingDown || movingUp);
	}

	/**
	 * Method to change the position of the player in the world according to their
	 * movement
	 * 
	 * @param tileManager the overworld's tile manager
	 */
	public void Movement(OverworldTileManager tileManager) {
		if (isPlayerMoving()) {
			if (movingUp) {
				setDirection("up");
			} else if (movingDown) {
				setDirection("down");
			} else if (movingLeft) {
				setDirection("left");
			} else if (movingRight) {
				setDirection("right");
			}
			/**
			 * Check whether the player is colliding with any solid objects
			 */
			collisionOff();
			checkCollision(tileManager);

			/**
			 * If there are no collisions, decrement or increment the player's world's
			 * position according to their speed
			 */
			if (!hasCollision()) {
				switch (getDirection()) {
					case "up" -> dWorldY(-getSpeed());
					case "down" -> dWorldY(getSpeed());
					case "left" -> dWorldX(-getSpeed());
					case "right" -> dWorldX(getSpeed());
				}
			}

			incrementSpriteCounter();
			if (getSpriteCounter() > 12) {
				if (getSpriteNum() == SpriteID.SPRITE1) {
					setSpriteNum(SpriteID.SPRITE2);
				} else if (getSpriteNum() == SpriteID.SPRITE2) {
					setSpriteNum(SpriteID.SPRITE1);
				}
				resetSpriteCounter();
			}
		} else {
			standCounter++;
			if (standCounter == 20) {
				setSpriteNum(SpriteID.SPRITE1);
				standCounter = 0;
			}
		}
	}

	/**
	 * Method to save the player's world's coordinates in a txt file using the
	 * Properties class
	 */
	public void saveGame() {
		Properties properties = new Properties();
		properties.setProperty("playerX", String.valueOf(getWorldX()));
		properties.setProperty("playerY", String.valueOf(getWorldY()));
		properties.setProperty("battlesWon", String.valueOf(getBattlesWon()));
		properties.setProperty("keysObtained", String.valueOf(getKeysObtained()));
		for (BattleEntity e : getMainParty().getMembers()) {
			System.out.println("Saving" + e);
			Set<String> attributeNames = e.getAttributes().keySet();
			for (String attributeName : attributeNames) {
				properties.setProperty(e.getClass().getSimpleName(), "exists");
				properties.setProperty(e.getClass().getSimpleName() + " " + attributeName,
						e.getAttributes().get(attributeName).toString());
				properties.setProperty(e.getClass().getSimpleName() + " " + "Name", e.getName());
			}
		}
		try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/Progress/game.txt")) {
			properties.store(fileOutputStream, "Player progress");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to load player's world's coordinates, number of keys, battles won, and
	 * party members from a txt file
	 */
	public void loadGame() {
		mainParty.clear();
		Properties properties = new Properties();
		FactoryReflection factory = new BattleEntityFactory();
		try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/Progress/game.txt")) {
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setWorldX(Integer.parseInt(properties.getProperty("playerX")));
		setWorldY(Integer.parseInt(properties.getProperty("playerY")));
		battlesWon = Integer.parseInt(properties.getProperty("battlesWon"));
		keysObtained = Integer.parseInt(properties.getProperty("keysObtained"));
		for (Class c : factory.getRegisteredProducts()) {
			if (properties.getProperty(c.getSimpleName()) != null) {
				this.mainParty.addPartyMember((BattleEntity) factory.createProduct(c.getSimpleName()));
			}
		}

		for (BattleEntity e : getMainParty().getMembers()) {
			System.out.println("loading" + e);
			Set<String> attributeNames = e.getAttributes().keySet();
			for (String attributeName : attributeNames) {
				Integer attributeVal = Integer
						.parseInt(properties.getProperty(e.getClass().getSimpleName() + " " + attributeName));
				e.setAttribute(attributeName, attributeVal);
				e.setName(properties.getProperty(e.getClass().getSimpleName() + " " + "Name"));
			}
		}
		loadedGame = true;
	}
}
