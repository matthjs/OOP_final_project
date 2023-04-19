package nl.rug.ai.oop.rpg.general;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import java.util.Random;

import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntityFactory;
import nl.rug.ai.oop.rpg.battles.model.entities.opponents.OpponentParty;
import nl.rug.ai.oop.rpg.battles.model.entities.party.MainParty;
import nl.rug.ai.oop.rpg.battles.model.system.BattleSystem;
import nl.rug.ai.oop.rpg.battles.model.system.BattleSystemBuilder;
import nl.rug.ai.oop.rpg.battles.view.BattleView;
import nl.rug.ai.oop.rpg.controller.KeyController;
import nl.rug.ai.oop.rpg.overworld.model.Player;
import nl.rug.ai.oop.rpg.overworld.view.GamePanel;

/**
 * The glue that sticks the battles and overworld together.
 * 
 * @author Matthijs
 * @author Niclas
 * @author Sanad
 */
public class ControlManager implements Runnable {
    private Thread thread;
    private GlobalState state;
    private BattleSystem battleSystem;
    private GamePanel gamePanel;
    private BattleView battleView;
    private Player player;
    private JFrame frame;
    private JPanel parentPanel;
    private MainParty mainParty;
    private FactoryReflection factory;

    /**
     * Gets the state of the contorlManager {BATTLE, OVERWORLD}
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public GlobalState getState() {
        return state;
    }

    /**
     * Gets the model for the battle system
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public BattleSystem getBattleSystem() {
        return battleSystem;
    }

    /**
     * Gets the player. Player is the model for the character
     * moving in the overworld
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * BATTLE: when in battles
     * OVERWORLD: when in overworld
     * @author Matthijs
     * @author Niclas
     */
    public enum GlobalState {
        BATTLE, OVERWORLD
    }

    /**
     * Gets the main party used in battles
     * @author matthijs
     * @author Niclas
     * @return
     */
    public MainParty getMainParty() {
        return mainParty;
    }

    /**
     * Creates and starts the game thread. 
     * Runs the init() method that causes the necessary objects to be created
     * By default the state is set to OVERWORLD
     * @author Matthijs
     * @author Niclas
     */
    public void startGameThread() {
        thread = new Thread(this);
        state = GlobalState.OVERWORLD;
        init();
        thread.start();
    }

    /**
     * Initializes game, by creating all the appropriate objects
     * Models, View, Controller etc.
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void init() {
        System.out.println("[ControlManager] in init()");
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Object-Oriented Programming: RPG");

        GameView.maxWorldCol = 50;
        GameView.maxWorldRow = 50;

        this.parentPanel = new JPanel();
        this.parentPanel.setLayout(new CardLayout());
        setEverythingNeededForBattles(true);
        setEverythingNeededForOverworld();
        this.parentPanel.addKeyListener(new KeyController(this));
        this.parentPanel.setFocusable(true);

        this.parentPanel.add(gamePanel);
        this.parentPanel.add(battleView);
        frame.add(parentPanel);

        player = gamePanel.getPlayer();
        player.setMainParty(mainParty);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * Creates and sets up everything that is needed for the battles. This includes
     * the initial party (in case of fresh game)
     * 
     * @author Matthijs
     * @author Niclas
     * @param musicOn
     */
    private void setEverythingNeededForBattles(boolean musicOn) {
        System.out.println("[ControlManager] in set[...]Battles");
        factory = new BattleEntityFactory();

        BattleView battleView = BattleView.getInstance();
        this.mainParty = new MainParty();
        if (!Player.loadedGame) {
            this.mainParty.addPartyMember((BattleEntity) factory.createProduct("Attacker"));
            this.mainParty.addPartyMember((BattleEntity) factory.createProduct("Mage"));
        }

        OpponentParty opponentParty = new OpponentParty();
        BattleSystem s = new BattleSystemBuilder().setMainParty(mainParty).setOpponentParty(opponentParty).build();

        battleView.setup(s, "Forest.png");
        this.battleView = battleView;
        this.battleSystem = s;
        if (musicOn) {
            battleSystem.playMusic(new Random().nextInt(41, 49));
        }
    }

    /**
     * Set everything needed for the overworld.
     * 
     * @author Sanad
     * @return
     */
    private GamePanel setEverythingNeededForOverworld() {
        System.out.println("[ControlManager] in set[...]Overworld");
        this.gamePanel = new GamePanel();
        return gamePanel;
    }

    /**
     * depending on state runs the appriopriate run() method of the
     * appropriate model.
     * 
     * @author Matthijs
     * @author Niclas
     */
    @Override
    public void run() {
        double drawInterval = 1000000000 / GameView.FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (thread != null) {
            checkState();
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;
            if (delta >= 1) {
                switch (state) {
                    case BATTLE:
                        battleSystem.run();
                        break;
                    case OVERWORLD:
                        gamePanel.run();
                        break;
                    default:
                        break;
                }
                delta--;
            }
        }
    }

    /**
     * Switches state to BATTLE or OVERWORLD when the conditions are met
     * 
     * @author Matthijs
     * @author Niclas
     * @author Sanad
     */
    private void checkState() {
        if (state == GlobalState.BATTLE && (battleSystem.hasWon() || battleSystem.hasLost())) {
            gamePanel.updateCounters(player);
            player.stopDirectionOfMovement("up");
            player.stopDirectionOfMovement("down");
            player.stopDirectionOfMovement("right");
            player.stopDirectionOfMovement("left");
            if (battleSystem.hasWon()) {
                player.incrementBattlesWon();
                if (player.getKeysObtained() < 3) {
                    player.incrementKeysObtained();
                }
            }
            if (player.getBattlesWon() == 3 || battleSystem.hasLost()) {
                player.setGameState(7);
            } else {
                player.setGameState(1);
            }
            battleView.setVisible(false);
            gamePanel.setVisible(true);
            gamePanel.setFocusable(true);
            battleView.setFocusable(false);
            mainParty.addPartyMember((BattleEntity) ((BattleEntityFactory) factory).getNewRandomPartMember());
            state = GlobalState.OVERWORLD;
        }
        if (state == GlobalState.OVERWORLD && (player.getGameState() == 2)) {
            player.setWorldY(23 * GameView.tileSize);
            player.setWorldX(23 * GameView.tileSize);
            player.stopDirectionOfMovement("down");

            OpponentParty opponentParty = null;
            switch (player.getBattlesWon()) {
                case 0:
                    opponentParty = getBattle0();
                    break;
                case 1:
                    opponentParty = getBattle1();
                    break;
                case 2:
                    opponentParty = getBattle2();
                    break;
                default:
                    break;
            }
            battleSystem.restartSystem(mainParty, opponentParty);

            battleView.setVisible(true);
            gamePanel.setVisible(false);
            battleView.setFocusable(true);
            gamePanel.setFocusable(false);

            state = GlobalState.BATTLE;
        }
    }

    /**
     * Sets up first battle
     * 
     * @author Matthijs
     * @author Niclas
     * @return
     */
    private OpponentParty getBattle0() {
        OpponentParty opponentParty = new OpponentParty();
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("NeuralNetwork"));
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("BigSlime"));
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("Slime"));

        return opponentParty;
    }

    /**
     * Sets up second battle
     * 
     * @author Matthijs
     * @author Niclas
     * @return
     */
    private OpponentParty getBattle1() {
        OpponentParty opponentParty = new OpponentParty();
        battleView.setup("BBLL3.jpg");
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("Ninja"));
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("Slime"));
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("Ninja"));


        return opponentParty;
    }

    /**
     * Sets up third battle
     * 
     * @author Matthijs
     * @author Niclas
     * @return
     */
    private OpponentParty getBattle2() {
        OpponentParty opponentParty = new OpponentParty();
        battleView.setup("Space.jpg");
        battleSystem.stopMusic();
        battleSystem.playMusic(49);
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("Mog"));
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("DarkMage"));
        opponentParty.addPartyMember((BattleEntity) factory.createProduct("Mog"));

        return opponentParty;
    }
}
