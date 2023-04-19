package nl.rug.ai.oop.rpg.general;
/**
 * Class that has variables keeping track of:
 * TileSizes (+ their scale),
 * The amount of tiles displayed on the screen at once,
 * Screen size and aspect ratio,
 * World size,
 * FPS
 * @author Matthijs
 * @author Niclas
 */
public class GameView {
    static public final int tileSize = 48; // 48 x 48 tile
    static public final int maxScreenCol = 21; //27;
    static public final int maxScreenRow = 12; //15;
    static public final int screenWidth = tileSize * maxScreenCol; // 1280 pixels
    static public final int screenHeight = tileSize * maxScreenRow; // 720 pixels

    // WORLD SETTING
    static public int maxWorldCol = 50;
    static public int maxWorldRow = 50;

    // FPS
    static public final int FPS = 60;
}
