package nl.rug.ai.oop.rpg.overworld.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.battles.model.entities.party.*;
import nl.rug.ai.oop.rpg.general.*;
import nl.rug.ai.oop.rpg.overworld.model.Player;

/**
 * Class for the overworld view of the game
 * @author Sanad
 */
public class GamePanel extends JPanel implements PropertyChangeListener {
    /**
     * Hash map for the key-value storage of the player's images
     */
    private static final Map<String, BufferedImage> playerImages = new HashMap<>(6);

    /**
     * Tile manager variable for the overworld's tiles
     */
    private OverworldTileManager overworldTileManager;

    /**
     * The player model
     */
    private Player player;

    /**
     * Number of battles won by player
     */
    private int battlesWon = 0;

    /**
     * Number of keys obtained by player
     */
    private int keysObtained = 0;

    /**
     * Constructor calling the init method
     */
    public GamePanel() {
        init();
    }

    /**
     * Getter method for player
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * The initialization method which initializes the tile manager and the player
     */
    private void init() {
        player = new Player();
        overworldTileManager = new OverworldTileManager(player);
        setPlayerImages();
        this.setPreferredSize(new Dimension(GameView.screenWidth, GameView.screenHeight));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        player.addListener(this);
    }

    /**
     * Run method which moves the player when necessary and repaints appearance
     */
    public void run() {
        if (player.getGameState() == 1) {
            player.Movement(overworldTileManager);
        }
        repaint();
    }

    /**
     * Method to add the player's images from the resources into the hash map variable
     */
    public void setPlayerImages() {
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage image;
        String path = "/BBA/Player/Walking sprites/";
        try {
            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_up_1" + ".png")));
            playerImages.put("playerUp1", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));

            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_up_2" + ".png")));
            playerImages.put("playerUp2", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));

            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_down_1" + ".png")));
            playerImages.put("playerDown1", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));

            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_down_2" + ".png")));
            playerImages.put("playerDown2", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));

            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_right_1" + ".png")));
            playerImages.put("playerRight1", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));

            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_right_2" + ".png")));
            playerImages.put("playerRight2", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));

            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_left_1" + ".png")));
            playerImages.put("playerLeft1", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));

            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + "boy_left_2" + ".png")));
            playerImages.put("playerLeft2", utilityTool.scaleImage(image, GameView.tileSize, GameView.tileSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to draw the player according to their direction
     * The method alternates between sprites to animate the player's motion
     * @param g2     the Graphics2D variable
     * @param player the player model variable
     */
    public void drawPlayer(Graphics2D g2, Player player) {
        BufferedImage image = null;
        switch (player.getDirection()) {
            case "up" -> {
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE1) {
                    image = playerImages.get("playerUp1");
                }
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE2) {
                    image = playerImages.get("playerUp2");
                }
            }
            case "down" -> {
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE1) {
                    image = playerImages.get("playerDown1");
                }
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE2) {
                    image = playerImages.get("playerDown2");
                }
            }
            case "left" -> {
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE1) {
                    image = playerImages.get("playerLeft1");
                }
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE2) {
                    image = playerImages.get("playerLeft2");
                }
            }
            case "right" -> {
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE1) {
                    image = playerImages.get("playerRight1");
                }
                if (player.getSpriteNum() == Entity.SpriteID.SPRITE2) {
                    image = playerImages.get("playerRight2");
                }
            }
        }
        g2.drawImage(image, player.getScreenX(), player.getScreenY(), null);
    }

    /**
     * Method to draw the world
     * @param g2                   the Graphics2D variable
     * @param player               the player model variable
     * @param overworldTileManager the tile manager of the overworld
     */
    public void drawWorld(Graphics2D g2, Player player, OverworldTileManager overworldTileManager) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < GameView.maxWorldCol && worldRow < GameView.maxWorldRow) {
            int tileNum = overworldTileManager.getMapTileNum()[worldCol][worldRow];

            int worldX = worldCol * GameView.tileSize;
            int worldY = worldRow * GameView.tileSize;
            int screenX = worldX - player.getWorldX() + player.getScreenX();
            int screenY = worldY - player.getWorldY() + player.getScreenY();

            /**
             * In order to increase efficiency, the program only draws the parts of the world that are
             * visible on the screen
             */
            if (worldX + GameView.tileSize > player.getWorldX() - player.getScreenX() &&
                    worldX - GameView.tileSize < player.getWorldX() + player.getScreenX() &&
                    worldY + GameView.tileSize > player.getWorldY() - player.getScreenY() &&
                    worldY - GameView.tileSize < player.getWorldY() + player.getScreenY()) {
                if(worldX == GameView.tileSize*23 && worldY == GameView.tileSize*38 && player.getBattlesWon() > 0) {
                    g2.drawImage(overworldTileManager.getTile()[10].getImage(), screenX, screenY, null);
                } else if (worldX == GameView.tileSize*23 && worldY == GameView.tileSize*36 && player.getBattlesWon() > 0) {
                    g2.drawImage(overworldTileManager.getTile()[40].getImage(), screenX, screenY, null);
                } else if (worldX == GameView.tileSize*38 && worldY == GameView.tileSize*10 && player.getBattlesWon() > 1) {
                    g2.drawImage(overworldTileManager.getTile()[10].getImage(), screenX, screenY, null);
                } else if (worldX == GameView.tileSize*38 && worldY == GameView.tileSize*12 && player.getBattlesWon() > 1) {
                    g2.drawImage(overworldTileManager.getTile()[40].getImage(), screenX, screenY, null);
                } else if (worldX == GameView.tileSize*10 && worldY == GameView.tileSize*12 && player.getBattlesWon() == 2) {
                    g2.drawImage(overworldTileManager.getTile()[10].getImage(), screenX, screenY, null);
                } else {
                    g2.drawImage(overworldTileManager.getTile()[tileNum].getImage(), screenX, screenY, null);
                }
            }
            worldCol++;
            /**
             * After drawing the first row, the program begins drawing the second row
             */
            if (worldCol == GameView.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    /**
     * A method to draw the inventory
     * The method iterates through the party members to see which members are still alive
     * The alive party members are added to the inventory
     * @param g2 the Graphics2D variable
     */
    public void drawInventory(Graphics2D g2) {
        /**
         * Create a UtilityTool variable to scale the images
         */
        UtilityTool utilityTool = new UtilityTool();
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, GameView.screenWidth, GameView.screenHeight);
        g2.setColor(Color.WHITE);
        Font titleFont = new Font("Helvetica", Font.PLAIN, 30);
        g2.setFont(titleFont);
        g2.drawString("Your Party", 40, 40);
        g2.drawString("Their Powers", 400, 40);
        g2.drawString("Keys", 700, 40);
        MainParty mainParty = player.getMainParty();
        int textY = GameView.tileSize;
        int spriteBias = (GameView.tileSize * 10) / mainParty.size();
        for (BattleEntity e : mainParty.getMembers()) {
            titleFont = new Font("Helvetica", Font.PLAIN, 20);
            g2.setFont(titleFont);
            String hp = "HP: " + e.getHP() + "/" + e.getMaxHP();
            g2.drawString(hp, 400, textY + 30);
            String mp = "MP: " + e.getMP() + "/" + e.getMaxMP();
            g2.drawString(mp, 400, textY + 25 + 30);

            g2.drawImage(e.getDefaultImage(),
            GameView.tileSize, textY, null);
            textY = textY + spriteBias;

        }
        BufferedImage image;
        try {
            for(int i = 0; i < player.getKeysObtained(); i++) {
                image = ImageIO.read(Objects.requireNonNull(
                        getClass().getResourceAsStream("/BBA/Object/key" + ".png")));
                g2.drawImage(utilityTool.scaleImage(image, GameView.tileSize * 2, GameView.tileSize * 2),
                        700, GameView.tileSize * (2 + 4*i), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to draw the String counters for the battles won and keys obtained
     * @param g2 the Graphics2D variable
     */
    public void drawCounters(Graphics2D g2) {
        Font titleFont = new Font("Helvetica", Font.PLAIN, 30);
        g2.setFont(titleFont);
        g2.setColor(Color.WHITE);
        String text = "Battles won: " + battlesWon + "/3";
        g2.drawString(text, GameView.screenWidth - 300, 80);
        text = "Keys obtained: " + keysObtained + "/3";
        g2.drawString(text, GameView.screenWidth - 300, 120);
        switch (player.getBattlesWon()) {
            case 0 -> text = "Travel South";
            case 1 -> text = "Travel East";
            case 2 -> text = "Travel West";
            default -> {
            }
        }
        g2.drawString(text, GameView.screenWidth - 1000, 120);
        titleFont = new Font("Helvetica", Font.PLAIN, 20);
        g2.setFont(titleFont);
        text = "Press shift for info tab";
        g2.drawString(text, GameView.screenWidth - 1000, 180);
    }

    /**
     * Method to draw the info tab
     * The information is drawn as strings
     * @param g2 Graphics2D variable
     */
    public void drawInfoTab(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, GameView.screenWidth, GameView.screenHeight);
        g2.setColor(Color.WHITE);
        Font titleFont = new Font("Helvetica", Font.PLAIN, 50);
        g2.setFont(titleFont);
        FontMetrics fontMetrics = g2.getFontMetrics();
        String title = "Overworld Controls & Guide";
        g2.drawString(title, (GameView.screenWidth - fontMetrics.stringWidth(title)) / 2,
                GameView.screenHeight / 2 - 150);
        Font subtitleFont = new Font("SansSerif", Font.ITALIC, 20);
        g2.setFont(subtitleFont);
        String subtitle = "P: Pause (and unpause)";
        fontMetrics = g2.getFontMetrics();
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 - 80);
        Font subtitleFont2 = new Font("SansSerif", Font.ITALIC, 20);
        g2.setFont(subtitleFont2);
        subtitle = "I: Inventory (and remove inventory screen)";
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 - 10);
        subtitle = "M: Save";
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 + 60);
        subtitle = "Shift: Go back";
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 + 130);
        subtitle = "Remember: text in the top left guides you to the next battle!";
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 + 200);

    }

    /**
     * A method to draw the starting game screen
     * The method only draws strings on the screen
     * @param g2 the Graphics2D variable
     */
    public void drawTitleGameScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, GameView.screenWidth, GameView.screenHeight);
        g2.setColor(Color.WHITE);
        Font titleFont = new Font("Helvetica", Font.PLAIN, 50);
        g2.setFont(titleFont);
        FontMetrics fontMetrics = g2.getFontMetrics();
        String title = "Welcome to OOP's RPG";
        g2.drawString(title, (GameView.screenWidth - fontMetrics.stringWidth(title)) / 2,
                GameView.screenHeight / 2 - 40);
        Font subtitleFont = new Font("SansSerif", Font.ITALIC, 30);
        g2.setFont(subtitleFont);
        String subtitle = "Press space to start new game";
        fontMetrics = g2.getFontMetrics();
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 + 70);
        subtitle = "Press L to load previous game";
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 + 140);
    }

    /**
     * A method to draw the pause screen
     * Similarly to above, the method only draws strings
     * @param g2 The Graphics2D variable
     */
    public void drawPauseScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, GameView.screenWidth, GameView.screenHeight);
        g2.setColor(Color.WHITE);
        Font titleFont = new Font("Helvetica", Font.PLAIN, 50);
        g2.setFont(titleFont);
        FontMetrics fontMetrics = g2.getFontMetrics();
        String title = "Pause Screen";
        g2.drawString(title, (GameView.screenWidth - fontMetrics.stringWidth(title)) / 2,
                GameView.screenHeight / 2 - 40);
        Font subtitleFont = new Font("SansSerif", Font.ITALIC, 30);
        g2.setFont(subtitleFont);
        String subtitle = "Press I to check inventory";
        fontMetrics = g2.getFontMetrics();
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 + 70);
        subtitle = "Press Esc to quit game";
        g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                GameView.screenHeight / 2 + 140);
    }

    /**
     * Method to draw the end game screen
     * @param g2 the Graphics2D variable
     */
    public void drawEndGameScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, GameView.screenWidth, GameView.screenHeight);
        g2.setColor(Color.WHITE);
        Font titleFont = new Font("Helvetica", Font.PLAIN, 50);
        g2.setFont(titleFont);
        FontMetrics fontMetrics = g2.getFontMetrics();
        if(player.getBattlesWon() > 2) {
            String title = "You've done it! You've won!";
            g2.drawString(title, (GameView.screenWidth - fontMetrics.stringWidth(title)) / 2,
                    GameView.screenHeight / 2 - 40);
            Font subtitleFont = new Font("SansSerif", Font.ITALIC, 30);
            g2.setFont(subtitleFont);
            String subtitle = "We hope you come back soon for more.";
            fontMetrics = g2.getFontMetrics();
            g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                    GameView.screenHeight / 2 + 70);
        } else {
            String title = "You've lost the battle.. and the war.";
            g2.drawString(title, (GameView.screenWidth - fontMetrics.stringWidth(title)) / 2,
                    GameView.screenHeight / 2 - 40);
            Font subtitleFont = new Font("SansSerif", Font.ITALIC, 30);
            g2.setFont(subtitleFont);
            String subtitle = "Better luck next time.";
            fontMetrics = g2.getFontMetrics();
            g2.drawString(subtitle, (GameView.screenWidth - fontMetrics.stringWidth(subtitle)) / 2,
                    GameView.screenHeight / 2 + 70);
        }

    }

    /**
     * Overriden method to paint components
     * The class draws the appropriate screen according to the Player's current game state
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawCounters(g2);
        switch (player.getGameState()) {
            case 0 -> drawTitleGameScreen(g2);
            case 1 -> {
                drawWorld(g2, player, overworldTileManager);
                drawPlayer(g2, player);
                drawCounters(g2);
                if (player.isSearchingInventory()) {
                    drawInventory(g2);
                }
            }
            case 3 -> {
                drawPauseScreen(g2);
                if (player.isSearchingInventory()) {
                    drawInventory(g2);
                }
            }
            case 4 -> {
                player.loadGame();
                overworldTileManager = new OverworldTileManager(player);
                drawWorld(g2, player, overworldTileManager);
                drawPlayer(g2, player);
                player.setGameState(1);
            }
            case 5 -> System.exit(0);
            case 7 -> drawEndGameScreen(g2);
            case 8 -> drawInfoTab(g2);
            default -> {
            }
        }
        g2.dispose();
    }

    /**
     * Overridden proprety change method
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateCounters((Player)evt.getSource());
    }

    /**
     * Method to update counters
     * @param player the model variable
     */
    public void updateCounters(Player player) {
        battlesWon = player.getBattlesWon();
        keysObtained = player.getKeysObtained();
    }

}