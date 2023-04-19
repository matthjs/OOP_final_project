package nl.rug.ai.oop.rpg.battles.model.system;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import nl.rug.ai.oop.rpg.battles.model.abilities.Ability;
import nl.rug.ai.oop.rpg.battles.model.entities.BattleEntity;
import nl.rug.ai.oop.rpg.battles.model.entities.Party;
import nl.rug.ai.oop.rpg.battles.model.entities.opponents.Opponent;
import nl.rug.ai.oop.rpg.battles.model.entities.opponents.OpponentParty;
import nl.rug.ai.oop.rpg.battles.model.entities.party.MainParty;
import nl.rug.ai.oop.rpg.battles.model.entities.party.PartyMember;
import nl.rug.ai.oop.rpg.general.GameView;
import nl.rug.ai.oop.rpg.general.Sound;

/**
 * Main game logic for battles
 * 
 * @author Matthijs
 * @author Niclas
 */
public class BattleSystem implements Runnable {
    public static BattleSystem instance;
    private final int TURNDISTANCE = 20; // how much should an battle entity move when its their turn?
    ArrayList<PropertyChangeListener> listeners = new ArrayList<>();

    private BattleSystem() {
    }

    /**
     * Singleton pattern: only one BattleSystem can exists at a time
     * Lazy implementation: creates an object on request.
     * 
     * @author Matthijs
     * @author Niclas
     * @param GameView
     * @return instance of battleSystem
     */
    public static BattleSystem getInstance() {
        if (instance == null) {
            instance = new BattleSystem();
        }
        return instance;
    }
    enum GameState {
        INACTIVE, PLAYING, WON, LOST
    }

    /**
     * ControlStates
     * NAVIGATING: when cycling through options (attack, magic)
     * CHOOSING: when choosing an ability (ex. slash)
     * TARGETING: when choosing which enemy to target
     * ACTIVATING: when the target has been selected
     * WAITING: when waiting
     * 
     * @author Matthijs
     */
    public enum ControlState {
        NAVIGATING,
        CHOOSING,
        TARGETING,
        ACTIVATING,
        WAITING
    }

    // CONTROL MANAGEMENT
    private GameState state = GameState.PLAYING;
    private ControlState controlState = ControlState.NAVIGATING;
    private CommandsManager commandManager;

    // BATTLE ENTITY MANAGEMENT
    private MainParty mainParty;
    private OpponentParty opponentParty;
    private List<BattleEntity> deadList = new ArrayList<>();
    private List<BattleEntity> entityList = new ArrayList<>();

    // TARGETING
    private BattleEntity currentEntity;
    private Party targetParty = null;
    private BattleEntity target;
    private int targetIdx = 0;

    // MUSIC AND SOUND EFFECTS
    private Sound music = new Sound();
    private Sound se = new Sound();

    /**
     * Restarts the battle system without the need to create a whole new instance.
     * 
     * @author Matthijs
     * @author Niclas
     * @param mainParty
     * @param opponentParty
     */
    public void restartSystem(MainParty mainParty, OpponentParty opponentParty) {
        state = GameState.PLAYING;
        controlState = ControlState.NAVIGATING;
        this.mainParty = mainParty;
        this.opponentParty = opponentParty;
        targetParty = null;
        target = null;
        targetIdx = 0;
        deadList.clear();
        entityList.clear();
        commandManager.resetElementIdx();
        commandManager.resetListIdx();
        constructEntityList();
    }

    /**
     * CommandManager manages contains the internal logic for the command screen.
     * 
     * @author Matthijs
     */
    public void setCommandsManager() {
        commandManager = new CommandsManager();
    }

    /**
     * Gets the command manager.
     * 
     * @author Matthijs
     */
    public CommandsManager getCommandManager() {
        return commandManager;
    }

    /**
     * Sets which party should be targeting in the TARGINING phase.
     * 
     * @auhor Matthijs
     */
    private void setTargeting() {
        if (controlState == ControlState.TARGETING) {
            resetTarget();
            Ability a = (Ability) getCommandManager().getCurrentlySelected();
            boolean targetOwn = a.targetOwn();
            if (currentEntity instanceof PartyMember) {
                if (targetOwn) {
                    targetParty = getMainParty();
                } else {
                    targetParty = getOpponentParty();
                }
            }
            if (currentEntity instanceof Opponent) {
                if (targetOwn) {
                    targetParty = getOpponentParty();
                } else {
                    targetParty = getMainParty();
                }
            }
        }
    }

    /**
     * Passes the turn to the next party member
     * SideEffect: activates the effects of the next party member
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void moveToNextEntity() {
        if (state == GameState.PLAYING) {
            currentEntity.move(TURNDISTANCE, false);
            entityList.add(currentEntity);
            entityList.remove(0);
            currentEntity = entityList.get(0);
            currentEntity.move(TURNDISTANCE, true);
            currentEntity.applyEffects();
        }
    }

    /**
     * Appropriately handles dead entities.
     * Removes them from the appriate parties and adds them to a dead list.
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void deadCheck() {
        ListIterator<BattleEntity> i = null;
        try {
            i = entityList.listIterator();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception");
        }

        while (i.hasNext()) {
            BattleEntity e = i.next();
            if (e.isDead()) {
                System.out.println(e + " died!");
                addToDeadListAndRemoveFromParty(e);
                i.remove();
                updateState();
            }
        }
        currentEntity = entityList.get(0);
    }

    /**
     * Adds an entity to dead list if they are dead
     * And removes them from their party
     */
    private void addToDeadListAndRemoveFromParty(BattleEntity e) {
        playSE(13);

        if (e instanceof Opponent) {
            deadList.add(e);
            opponentParty.removePartyMember(e);
        } else if (e instanceof PartyMember) {
            deadList.add(e);
            mainParty.removePartyMember(e);
        }
    }

    public boolean hasWon() {
        return state == GameState.WON;
    }

    public boolean hasLost() {
        return state == GameState.LOST;
    }

    /**
     * Run method of the battle System
     * update() and notifyListeners is run 60 times per second
     * update() updates the information of the model
     * notifying the listeners causes the view to be updated
     * 
     * @author Matthijs
     * @author Niclas
     */
    public void run() {
        /* as long as external gameThread exists the process is repeated */

        // 1 UPDATE: update information such as character position
        update();
        // 2 DRAW: draw the screen with the updated information
        notifyListeners("repaint", null);

        checkEndState();
    }

    /**
     * Prints end state if applicable.
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void checkEndState() {
        if (state == GameState.WON) {
            System.out.println("Game Won");
        }
        if (state == GameState.LOST) {
            System.out.println("Game Lost");
        }
    }

    /**
     * Updates the model (automatically runs the update method for each entity)
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void update() {
        for (BattleEntity e : entityList) {
            e.update();
        }

        // NPC obviously do not go through the control phases
        if (controlState == ControlState.NAVIGATING && currentEntity instanceof Opponent) {
            controlState = ControlState.ACTIVATING;
            updateCommandManager();
        }

        if (controlState == ControlState.ACTIVATING && currentEntity.isIdle()) {
            setTarget();
            nextControlState();
        }

        // If block runs when animation for ability is finished
        if (controlState == ControlState.WAITING && currentEntity.isIdle()) {
            applyAbility();
            moveToNextEntity();
            deadCheck();
            updateCommandManager();
            nextControlState();
        }
    }

    /**
     * Immediately applies the effects of the targeted entity
     * Slight bug: in reality what you want is that it ONLY applies the effect
     * of the ability that was chosen.
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void applyAbility() {
        target.applyEffects();
        target = null;
    }

    /**
     * Updates the state to WIN if all opponents are dead.
     * Updates the state to LOSE if all party members are dead.
     * 
     * @author Matthijs
     * @author Niclas
     */
    private void updateState() {
        boolean noPartyMembers = true;
        boolean noOpponents = true;

        for (BattleEntity e : entityList) {
            if (e instanceof PartyMember) {
                noPartyMembers = false;
            }
            if (e instanceof Opponent) {
                noOpponents = false;
            }
        }
        if (noPartyMembers && noOpponents) {
            state = GameState.WON;
        }
        if (noOpponents) {
            state = GameState.WON;
        } else if (noPartyMembers) {
            state = GameState.LOST;
        }
    }

    /**
     * Updates the relevant information: that is
     * what set of ability to use and display.
     * For example when its party member i's turn
     * then i's abilities will be used
     * If MP < 0 then user cannot use magicAbilities of party member.
     * 
     * @author Matthijs
     */
    private void updateCommandManager() {
        commandManager.resetListIdx();
        commandManager.resetElementIdx();
        commandManager.removeAll();
        if (currentEntity.getAttackAbilities().size() != 0) {
            commandManager.addList(currentEntity.getAttackAbilities());
        }
        if (currentEntity.getMP() > 0 && currentEntity.getMagicAbilities().size() != 0) {
            commandManager.addList(currentEntity.getMagicAbilities());
        }
    }

    /**
     * Apply chosen ability to target
     * (also contains opponents decision making: choosing a random target)
     * 
     * @author Matthijs
     */
    private void setTarget() {
        if (currentEntity instanceof PartyMember) {
            target = targetParty.getMember(targetIdx);
            ((Ability) commandManager.getCurrentlySelected()).execute(target);
        }
        if (currentEntity instanceof Opponent) {
            Ability a = ((Opponent) currentEntity).chooseRandomAbility();
            if (a.targetOwn()) {
                target = opponentParty.getRandomMember();
                a.execute(target);
            } else {
                target = mainParty.getRandomMember();
                a.execute(target);
            }
        }
    }

    /**
     * Construct playing order
     * 
     * @author Matthijs (revision)
     * @author Niclas
     */
    public void constructEntityList() {

        OpponentParty opponentParty = getOpponentParty();
        MainParty mainParty = getMainParty();
        if (opponentParty == null || mainParty == null) {
            throw new IllegalStateException("opponentParty or mainParty is not assigned");
        }

        setSpawnLocation(mainParty, opponentParty);

        currentEntity = getEntityList().get(0);
        currentEntity.move(TURNDISTANCE, true);
        state = GameState.PLAYING;
        updateCommandManager();
    }

    /**
     * Sets the spawn locations for the battle entities
     * 
     * @author Matthijs
     * @author Niclas
     * @param mainParty
     * @param opponentParty
     */
    private void setSpawnLocation(MainParty mainParty, OpponentParty opponentParty) {
        int dx = 0;
        int dy = 0;
        int spawnBias = -10;
        for (BattleEntity e : mainParty.getMembers()) {
            e.setWorldX(GameView.tileSize * 5);
            e.setWorldY(GameView.tileSize * 4   - GameView.tileSize / 3);
            e.setDirection("right");
        }
        for (BattleEntity e : opponentParty.getMembers()) {
            e.setWorldX(GameView.tileSize * 15);
            e.setWorldY(GameView.tileSize * 4 - GameView.tileSize / 3);
            e.setDirection("left");
        }
        for (BattleEntity e : mainParty.getMembers()) {
            e.dWorldX(dx);
            e.dWorldY(dy);
            addToEntityList(e);
            dx = dx + spawnBias + e.getDefaultImage().getWidth();
            dy = dy + spawnBias + e.getDefaultImage().getHeight();
        }
        dx = 0;
        dy = 0;
        for (BattleEntity e : opponentParty.getMembers()) {
            e.dWorldX(dx);
            e.dWorldY(dy);
            addToEntityList(e);
            dx = dx + spawnBias + e.getDefaultImage().getWidth();
            dy = dy + spawnBias + e.getDefaultImage().getHeight();
        }
    }

    /**
     * @author Matthijs
     * @author Niclas
     * @return true if the system is in a state where the player should have control
     */
    public boolean isControllingTurn() {
        return controlState != ControlState.WAITING && currentEntity instanceof PartyMember;
    }

    /**
     * Goes to next control state. The controller runs this method
     * when ENTER is pressed.
     * 
     * @author Matthijs
     */
    public void nextControlState() {
        switch (controlState) {
            case NAVIGATING:
                controlState = ControlState.CHOOSING;
                break;
            case CHOOSING:
                controlState = ControlState.TARGETING;
                setTargeting();
                break;
            case TARGETING:
                controlState = ControlState.ACTIVATING;
                break;
            case ACTIVATING:
                controlState = ControlState.WAITING;
                break;
            case WAITING:
                controlState = ControlState.NAVIGATING;
                break;
            default:
                break;
        }
    }

    /**
     * Goes to previous control state. The controller runs this method
     * when SHIFT is pressed.
     * 
     * @author Matthijs
     */
    public void previousControlState() {
        switch (controlState) {
            case CHOOSING:
                controlState = ControlState.NAVIGATING;
                break;
            case NAVIGATING:
                break;
            case TARGETING:
                controlState = ControlState.CHOOSING;
                break;
            default:
                break;
        }
    }

    /**
     * Gets control state {NAVIGATING, CHOOSING, TARGETING, ACTIVATING, WAITING}
     * @author Mattijs
     * @author Niclas
     * @return
     */
    public ControlState getControlState() {
        return controlState;
    }

    /**
     * EntityList is the order in which entities take turns
     * 
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public List<BattleEntity> getEntityList() {
        return entityList;
    }

    /**
     * Adds entity to entity list
     * @author Matthijs
     * @author Niclas
     * @param e
     */
    public void addToEntityList(BattleEntity e) {
        entityList.add(e);
    }

    /**
     * Gets opponent party
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public OpponentParty getOpponentParty() {
        return opponentParty;
    }

    /**
     * Sets opponent party
     * @author Matthijs
     * @author Niclas
     * @param opponentParty
     */
    public void setOpponentParty(OpponentParty opponentParty) {
        this.opponentParty = opponentParty;
    }

    /**
     * Set main party
     * @author Mattijs
     * @author Niclas
     * @param mainParty
     */
    public void setMainParty(MainParty mainParty) {
        this.mainParty = mainParty;
    }

    /**
     * Gets main party
     * @author Matthijs
     * @author Niclas
     * @return
     */
    public MainParty getMainParty() {
        return mainParty;
    }

    /**
     * Resets targeting index
     * @author Matthijs
     * @author Niclas
     */
    public void resetTarget() {
        targetIdx = 0;
    }

    /**
     * Goes to the next target in the targetParty
     * 
     * @author Matthijs
     */
    public void nextTarget() {
        if (controlState == ControlState.TARGETING) {
            targetIdx = (targetIdx + 1) % targetParty.size();
        } else
            throw new IllegalStateException("Targeting while not in targeting control state");
    }

    /**
     * Gets index of target
     * 
     * @author Matthijs
     */
    private int getTargetIdx() {
        return targetIdx;
    }

    public BattleEntity getTargetedEntity() {
        return targetParty.getMember(getTargetIdx());
    }

    /**
     * Goes to the previous target in the targetParty
     * 
     * @author Matthijs
     */
    public void previousTarget() {
        if (controlState == ControlState.TARGETING) {
            targetIdx--;
            if (targetIdx < 0) {
                targetIdx = targetIdx + targetParty.size();
            }
            targetIdx = targetIdx % targetParty.size();
        }

    }

    /**
     * Adds listener
     * @author Matthijs
     * @author Niclas
     * @param listener
     */
    public void addListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove listener
     * @author Matthijs
     * @author Niclas
     * @param listener
     */
    public void removeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies listeners with a PropertyChangeEvent object, this contains a
     * reference to this object
     * and an optional payload. This method should be called ideally when the model
     * changes its state
     * somehow so that the view can update accordingly
     * 
     * @author Matthijs
     * @author Niclas
     * @param propName Name of the event
     * @param payload  Payload object to pass onto the listener
     */
    private void notifyListeners(String propName, Object payload) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propName, null, payload);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }

    /**
     * Play music i
     * 
     * @author Matthijs
     * @author Niclas
     * @param i
     */
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    /**
     * Stops music
     * 
     * @author Matthijs
     * @author Niclas
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     * Plays sound effect i
     * 
     * @author Matthijs
     * @author Niclas
     */
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
