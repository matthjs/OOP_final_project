package nl.rug.ai.oop.rpg.overworld.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;

import nl.rug.ai.oop.rpg.general.GameView;
import nl.rug.ai.oop.rpg.general.UtilityTool;
import nl.rug.ai.oop.rpg.general.tile.Tile;
import nl.rug.ai.oop.rpg.overworld.model.Player;

/**
 * Class to manage the tiles of the overworld
 * @author Sanad
 */
public class OverworldTileManager {
    /**
     * An array of tiles to store the different types of tiles
     */
    private final nl.rug.ai.oop.rpg.general.tile.Tile[] tile;

    /**
     * A matrix to store the exact coordinates of the tiles for their reference number
     */
    private final int[][] mapTileNum;

    /**
     * Getter method for tile variable
     */
    public Tile[] getTile() {
        return tile;
    }

    /***
     * Getter method for mapTileNum
     */
    public int[][] getMapTileNum() {
        return mapTileNum;
    }

    /**
     * Constructor which initializes the array and matrix
     * The consturctor also loads different maps, according the Player's current game state.
     */
    public OverworldTileManager(Player player) {
        tile = new nl.rug.ai.oop.rpg.general.tile.Tile[50]; // 50 kinds of tiles
        mapTileNum = new int[GameView.maxWorldCol][GameView.maxWorldRow];
        getTileImage();
        if(player.getBattlesWon() == 1) {
            loadMap("worldv4.txt");
        } else if (player.getBattlesWon() == 2) {
            loadMap("worldv5.txt");
        } else {
            loadMap("worldV2.txt");
        }
    }

    /**
     * Load tile images and assigns which tiles act as collision
     */
    public void getTileImage() {
        setup(0, "grass00", false);
        setup(1, "grass00", false);
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4, "grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);

        setup(10, "grass00", false);
        setup(11, "grass01", false);

        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);

        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);
        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41, "tree", true);
        setup(42, "specialTree", true);
        setup(43, "hut", true);
    }

    /**
     * Setup method for the tiles
     * The method obtains the images from the resource and assigns them an index
     * The method also assigns them the ability to collide or not
     * @param index The registered index for the tile
     * @param collision The ability to collide
     */
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool utilityTool = new UtilityTool();

        String path = "/BBA/Tiles/New version/";
        try {
            tile[index] = new Tile();
            tile[index].setImage(ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(path + imageName + ".png"))));
            tile[index].setImage(utilityTool.scaleImage(tile[index].getImage(), GameView.tileSize, GameView.tileSize));
            tile[index].setCollision(collision);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loading the map data
     * The method opens a stream and then reads the txt file of the world to decide where each tile goes
     * The method is obtained from the work of the following author:
     * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
     */
    public void loadMap(String filePath) {
        String s = "/BBA/Map/";
        try {
            InputStream is = getClass().getResourceAsStream(s + filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while (col < GameView.maxWorldCol && row < GameView.maxWorldRow) {
                String line = br.readLine();
                while (col < GameView.maxWorldCol) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == GameView.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
