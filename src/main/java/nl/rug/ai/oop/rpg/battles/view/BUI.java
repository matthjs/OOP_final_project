package nl.rug.ai.oop.rpg.battles.view;

import nl.rug.ai.oop.rpg.general.Drawable;
import nl.rug.ai.oop.rpg.general.GameView;
import nl.rug.ai.oop.rpg.battles.model.abilities.Ability;
import nl.rug.ai.oop.rpg.battles.model.abilities.Command;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.battles.model.entities.Party;
import nl.rug.ai.oop.rpg.battles.model.entities.opponents.Opponent;
import nl.rug.ai.oop.rpg.battles.model.entities.party.PartyMember;
import nl.rug.ai.oop.rpg.battles.model.system.BattleSystem;
import nl.rug.ai.oop.rpg.battles.model.system.CommandsManager;
import nl.rug.ai.oop.rpg.battles.model.system.BattleSystem.ControlState;

import java.awt.*;
import java.util.List;

/**
 * UI for battles
 * 
 * @author Matthijs
 * @author Niclas
 */
public class BUI implements Drawable {
    /**
     * The Graphics2D variable
     */
    protected Graphics2D g2;
    private List<BattleEntity> entityList;
    private Party mainParty;
    private Party opponentParty;
    private BattleEntity currentEntity;
    private BattleEntity targetEntity = null;
    private CommandsManager commandsManager;

    private Font arial_40 = new Font("Arial", Font.PLAIN, 40);
    private final Color red = new Color(255, 0, 30);
    private final Color black1 = new Color(35, 35, 35);
    private final Color black2 = new Color(0, 0, 0, 210);
    private final Color white = new Color(255, 255, 255);

    private ControlState subState;

    /**
     * Whenever the battleSystem runs its update() which updates information in the
     * current model
     * then the BUI is notified of just the elements that it needs for displaying
     * the
     * appropriate information (see updateCommand())
     * 
     * @author Matthijs
     * @param battleSystem
     */
    public void setupBUI(BattleSystem battleSystem) {
        battleSystem.addListener(evt -> updateCommand(battleSystem));
    }

    /**
     * This information is relayed to the UI
     * 
     * @author Matthijs
     * @param battleSystem
     */
    private void updateCommand(BattleSystem battleSystem) {
        this.subState = battleSystem.getControlState();
        if (battleSystem.getControlState() == ControlState.TARGETING) {
            this.targetEntity = battleSystem.getTargetedEntity();
        }
        this.commandsManager = battleSystem.getCommandManager();
        this.entityList = battleSystem.getEntityList();
        this.mainParty = battleSystem.getMainParty();
        this.opponentParty = battleSystem.getOpponentParty();
    }

    /**
     * Draws UI
     * 
     * @author Matthijs
     * @author Niclas
     * @param g2
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        this.g2 = g2;
        try {
            currentEntity = entityList.get(0);
            getOptionsScreen();
            drawOrder();

            g2.setFont(g2.getFont().deriveFont(22F));

            drawMainPartyInfo();

            drawOpponentPartyInfo();

        } catch (NullPointerException e) {
            System.out.println("Entity list empty");
        }
    }

    /**
     * Prints out names in addition to HP and MP information
     * of all the party members. And if the state is appropriate targeting icon.
     * 
     * @author Matthijs
     */
    private void drawMainPartyInfo() {
        String text;
        int textX = GameView.screenWidth / 25;
        int textY = GameView.screenHeight / 10;
        for (BattleEntity e : mainParty.getMembers()) {
            g2.setColor(Color.white);
            text = e.getName();
            textY += GameView.tileSize / 2;
            g2.drawString(text, textX, textY);
            text = "HP " + e.getHP().toString() + " / " + e.getMaxHP().toString();
            textY += GameView.tileSize / 2;
            g2.drawString(text, textX, textY);

            int mp = e.getMP();
            if (mp > 0) {
                text = "MP " + e.getMP().toString() + " / " + e.getMaxMP().toString();
            } else {
                text = "MP " + 0 + " / " + e.getMaxMP().toString();
            }

            textY += GameView.tileSize / 2;
            g2.drawString(text, textX, textY);

            textY += GameView.tileSize;
            if ((subState == ControlState.TARGETING) && e.equals(targetEntity)) {
                drawTargetCursor(e);
            }
        }
    }

    /**
     * Displays enemy health bars and if the state is appropriate targeting icon.
     * 
     * @author Matthijs
     */
    private void drawOpponentPartyInfo() {
        for (BattleEntity e : opponentParty.getMembers()) {
            double oneScale = (double) GameView.tileSize / e.getMaxHP();
            double hpBarValue = oneScale * e.getHP();

            g2.setColor(black1);
            int x = e.getWorldX() + Math.abs((e.getWorldX() + e.getDefaultImage().getWidth()) - e.getWorldX()) / 2 - 1
                    - ((GameView.tileSize + 2) / 2);
            g2.fillRect(x, e.getWorldY() - 16, GameView.tileSize + 2, 12);

            g2.setColor(red);
            g2.fillRect(x + 1, e.getWorldY() - 15, (int) hpBarValue, 10);

            if (subState == ControlState.TARGETING && e.equals(targetEntity)) {
                drawTargetCursor(e);
            }
        }
    }

    /**
     * Draw order is exactly what it sounds like: it displays entities at the top
     * of the screen in order: the entity on the left is the one whose turn it is
     * Partymembers have a green rectangle and Opponents a red rectangle.
     * (More specifically: it displays a scaled version of the image (sprite) of
     * battlEntity e and draws a square around it. If e is an instance of opponent
     * than the square is coloured red else if it is a partymember it is coloured
     * green).
     * 
     * @author Matthijs
     * @author Niclas
     * @param x
     * @param y
     * @param e
     * @param bias
     */
    private void drawOrder() {
        int x = GameView.screenWidth / 2 - entityList.size() * GameView.tileSize / 2;
        int y = GameView.screenHeight / 25;
        int bias = 0;
        for (BattleEntity e : entityList) {

            int width = GameView.tileSize - 10;
            int height = GameView.tileSize - 10;

            Color c = null;
            if (e instanceof PartyMember) {
                c = new Color(0, 255, 30, 100);
            }
            if (e instanceof Opponent) {
                c = new Color(255, 0, 30, 100);
            }
            g2.setColor(c);
            g2.fillRect(x + bias, y, width, height);

            g2.drawImage(e.getDefaultImage(), x + bias,
                    y, width, height, null);
            bias = bias + GameView.tileSize;
        }

    }

    /**
     * (1) draws main options that is attack, magic, item (+ any additional command
     * lists that are added to the commandManager)
     * (2) draws the appropriate subscreen depending on the controlState of the
     * battleSystem
     * Note: right now the drawOptionsScreen is not super flexble. When a party
     * member has lots of abilities
     * especially with long names.
     * 
     * @author Matthijs
     */
    private void getOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(22F));
        // SUB WINDOW
        int frameWidth = GameView.tileSize * 12;
        int frameHeight = GameView.tileSize * 3; // was 4
        int frameX = GameView.screenWidth / 2 - frameWidth / 2;
        int frameY = GameView.screenHeight / 2 - frameHeight / 3 + GameView.screenHeight / 3; // frameHeight was / 2

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        if (subState == ControlState.NAVIGATING) {
            drawMainOptions(frameX, frameY, frameWidth, frameHeight, true);
        } else {
            drawMainOptions(frameX, frameY, frameWidth, frameHeight, false);
            drawOptionsScreen(frameX, frameY, frameWidth, frameHeight);
        }

    }

    /**
     * Draws attack, magic, item screen.
     * 
     * @author Matthijs
     * @param frameX
     * @param frameY
     * @param frameWidth
     * @param frameHeight
     * @param drawCursor
     */
    private void drawMainOptions(int frameX, int frameY, int frameWidth, int frameHeight, boolean drawCursor) {
        int textX;
        int textY;

        String text = "menu";
        textX = getXforCenteredText(text);
        textY = frameY + GameView.tileSize / 8;
        textX = frameX + GameView.tileSize;
        textY += GameView.tileSize;

        // This can be a lot nicer but gets the job done for now
        for (int i = 0; i < commandsManager.getNumberOfLists(); i++) {
            switch (i) {
                case 0:
                    text = "Attack";
                    break;
                case 1:
                    text = "Magic";
                    break;
                case 2:
                    text = "Item";
                    break;
                default:
                    text = "Command " + i;
                    break;
            }
            g2.drawString(text, textX, textY);
            if (drawCursor && commandsManager.getCurrentListIdx() == i) {
                g2.drawString(">", textX - 25, textY);
            }
            textY += GameView.tileSize / 2;
            if (textY > frameY + frameHeight - GameView.tileSize / 2) {
                textX = textX + GameView.tileSize * 2;
                textY = frameY + GameView.tileSize / 8 + GameView.tileSize;
            }
        }
    }

    /**
     * Draws the appropriate subscreen depending on the controlState of the
     * battleSystem
     * 
     * @author Matthijs
     * @param frameX
     * @param frameY
     * @param frameWidth
     * @param frameHeight
     */
    private void drawOptionsScreen(int frameX, int frameY, int frameWidth, int frameHeight) {

        if (currentEntity instanceof PartyMember) {
            drawSubWindow(frameX + GameView.tileSize * 3, frameY + GameView.tileSize / 4,
                    frameWidth - GameView.tileSize * 3 - 10, frameHeight - GameView.tileSize / 4 - 10);
            drawOptions(frameX + GameView.tileSize * 3, frameY + GameView.tileSize / 4,
                    frameWidth - GameView.tileSize * 3 - 10, frameHeight - GameView.tileSize / 4 - 10,
                    commandsManager.getCurrentList(), true);
        }
    }

    /**
     * Draws the commands on the screen. So for example if the mage class has magic
     * abilities
     * heal, fire, light II then those will be displayed in the subwindow
     * with an optional cursor on the one that is selected (the commandManager is
     * the underlying mechanism)
     * 
     * @author Matthijs
     * @param frameX
     * @param frameY
     * @param frameWidth
     * @param frameHeight
     * @param c
     * @param drawCursor
     */
    private void drawOptions(int frameX, int frameY, int frameWidth, int frameHeight, List<Command> c,
            boolean drawCursor) {
        int textX;
        int textY;

        String text = "menu";
        textX = getXforCenteredText(text);
        textY = frameY + GameView.tileSize / 8;
        textX = frameX + GameView.tileSize;
        textY += GameView.tileSize;

        for (Command command : c) {
            g2.drawString(((Ability) command).getName(), textX, textY);
            if (drawCursor && command == commandsManager.getCurrentlySelected()) { // may be a nicer way
                g2.drawString(">", textX - 25, textY);
            }
            textY += GameView.tileSize / 2;
            if (textY > frameY + frameHeight - GameView.tileSize / 2) {
                textX = textX + GameView.tileSize * 2;
                textY = frameY + GameView.tileSize / 8 + GameView.tileSize;
            }
        }
    }

    /**
     * When the battleSystem is in the Targeting control state then
     * we indicate which enemy is currently being targeted.
     * 
     * @author Matthijs
     * @param e
     */
    private void drawTargetCursor(BattleEntity e) {
        int x = e.getWorldX() + Math.abs((e.getWorldX() + e.getDefaultImage().getWidth()) - e.getWorldX()) / 2 - 2;
        g2.setFont(g2.getFont().deriveFont(35F));
        g2.setColor(red);
        g2.drawString("!", x, e.getWorldY() - 25);
        g2.setFont(g2.getFont().deriveFont(22F));
        g2.setColor(Color.white);
    }

    /**
     * Draws subswindow to possibly display text on
     * 
     * @author Matthijs
     * @param x
     * @param y
     * @param width
     * @param height
     */
    private void drawSubWindow(int x, int y, int width, int height) {
        Color c = black2;
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = white;
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    /**
     * Gets the appropriate x-position for drawing centered text on the screen
     * 
     * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
     * @param text
     * @return
     */
    private int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = GameView.screenWidth / 2 - length / 2;
        return x;
    }
}
