package nl.rug.ai.oop.rpg.battles.view;

import java.awt.Dimension;

import javax.swing.*;
import java.awt.*;
import nl.rug.ai.oop.rpg.battles.model.system.BattleSystem;
import nl.rug.ai.oop.rpg.general.Drawable;
import nl.rug.ai.oop.rpg.general.GameView;

/**
 * View of battles: has field for UI, tile placement, displaying characters,
 * playing music.
 * Extends JPanel
 * 
 * @author Matthijs
 * @author Niclas
 * 
 */
public class BattleView extends JPanel {
    public static BattleView instance;
    private Drawable bui, tileM, battleEntityView;

    private BattleView() {
        this.setPreferredSize(new Dimension(GameView.screenWidth, GameView.screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        tileM = new BattleBackgroundManager();
        battleEntityView = new BattleEntityView();
        bui = new BUI();
    }

    /**
     * Singleton pattern: only one BattleView can exists at a time
     * Lazy implementation: creates an object on request.
     * 
     * @author Matthijs
     * @author Niclas
     * @param GameView
     * @return instance of gameView
     */
    public static BattleView getInstance() {
        if (instance == null) {
            instance = new BattleView();
        }
        return instance;
    }

    /**
     * Sets up game: starts music (optional), adds keyListeners, instantiates
     * battleEntityView, BUI, loads map
     * adds a listener to the model (battleSystem) and starts the game thread.
     * 
     * @author Matthijs
     * @author Niclas
     * @param battleSystem
     * @param keyHandler
     */
    public void setup(BattleSystem battleSystem, String backgroundSource) {

        ((BattleBackgroundManager) tileM).loadBackground(backgroundSource);
        ((BattleBackgroundManager) tileM).enableBackground();

        ((BUI) bui).setupBUI(battleSystem);
        ((BattleEntityView) battleEntityView).setEntityList(battleSystem.getEntityList());

        battleSystem.addListener(evt -> updateView());
    }

    /**
     * Can be used to change just the background.
     * 
     * @author Matthijs
     * @author Niclas
     * 
     * @param backgroundSource
     */
    public void setup(String backgroundSource) {
        ((BattleBackgroundManager) tileM).loadBackground(backgroundSource);
        ((BattleBackgroundManager) tileM).enableBackground();
    }

    /**
     * Runs the repaint method.
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void updateView() {
        repaint();
    }

    /**
     * Method used to display graphics
     * 1. background is drawn
     * 2. entities are drawn
     * 3. ui is drawn
     * 
     * @author Matthijs
     * @author Niclas
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // BACKGROUND
        tileM.draw(g2);

        // ENTITIES
        battleEntityView.draw(g2);

        // UI
        bui.draw(g2);

        g2.dispose();
    }
}
