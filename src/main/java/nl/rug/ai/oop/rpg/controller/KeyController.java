package nl.rug.ai.oop.rpg.controller;

import java.awt.event.*;
import nl.rug.ai.oop.rpg.general.ControlManager;
import nl.rug.ai.oop.rpg.general.ControlManager.GlobalState;

/**
 * Controller class
 * 
 * @author Matthijs (BATTLE PART)
 * @author Niclas (BATTLE PART)
 * @author Sanad (OVERWORLD PART)
 */
public class KeyController implements KeyListener {
    private ControlManager controlM;

    public KeyController(ControlManager controlM) {
        this.controlM = controlM;
    }

    /**
     * Method that acts as the controller for the battles.
     * 
     * @author Matthijs
     * @author Niclas
     * @param code
     */
    private void battleKeyPressed(int code) {
        switch (code) {
            case KeyEvent.VK_W:
                if (controlM.getBattleSystem().isControllingTurn()) {
                    controlM.getBattleSystem().playSE(5);
                    switch (controlM.getBattleSystem().getControlState()) {
                        case NAVIGATING:
                            controlM.getBattleSystem().getCommandManager().nextList();
                            controlM.getBattleSystem().resetTarget();
                            break;
                        case CHOOSING:
                            controlM.getBattleSystem().getCommandManager().previousCommand();
                            controlM.getBattleSystem().resetTarget();
                            break;
                        case TARGETING:
                            controlM.getBattleSystem().previousTarget();
                            break;
                        default:
                            break;

                    }
                }
                break;
            case KeyEvent.VK_S:
                if (controlM.getBattleSystem().isControllingTurn()) {
                    controlM.getBattleSystem().playSE(5);
                    switch (controlM.getBattleSystem().getControlState()) {
                        case NAVIGATING:
                            controlM.getBattleSystem().getCommandManager().previousList();
                            controlM.getBattleSystem().resetTarget();
                            break;
                        case CHOOSING:
                            controlM.getBattleSystem().getCommandManager().nextCommand();
                            controlM.getBattleSystem().resetTarget();
                            break;
                        case TARGETING:
                            controlM.getBattleSystem().nextTarget();
                            break;
                        default:
                            break;
                    }
                }
                break;
            case KeyEvent.VK_ENTER:
                if (controlM.getBattleSystem().isControllingTurn()) {
                    controlM.getBattleSystem().playSE(15);
                    controlM.getBattleSystem().nextControlState();
                }
                break;
            case KeyEvent.VK_SHIFT:
                if (controlM.getBattleSystem().isControllingTurn()) {
                    controlM.getBattleSystem().playSE(15);
                    controlM.getBattleSystem().previousControlState();
                }
                break;
        }
    }

    /**
     * Method that acts as the overworld controller.
     * 
     * @author Sanad
     * @param e
     */
    private void overworldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            controlM.getPlayer().setDirectionOfMovement("up");
        }
        /**
         * Pressing S or Down indicates controlM.getPlayer() intends to move downwards
         */
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            controlM.getPlayer().setDirectionOfMovement("down");
        }
        /**
         * Pressing A or Left indicates controlM.getPlayer() intends to move leftwards
         */
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            controlM.getPlayer().setDirectionOfMovement("left");
        }
        /**
         * Pressing D or Right indicates controlM.getPlayer() intends to move rightwards
         */
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            controlM.getPlayer().setDirectionOfMovement("right");
        }
        /**
         * Pressing I indicates that the controlM.getPlayer() wants to search their inventory
         * Pressing it twice performs the opposite
         */
        if (e.getKeyCode() == KeyEvent.VK_I) {
            if (controlM.getPlayer().isSearchingInventory()) {
                controlM.getPlayer().setSearchingInventory(false);
            } else {
                controlM.getPlayer().setSearchingInventory(true);
            }
        }
        /**
         * Pressing Space while on the title screen starts the game
         */
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (controlM.getPlayer().getGameState() == 0) {
                controlM.getPlayer().setGameState(1);
            }
        }
        /**
         * Press P to set the game in pause mode
         * Press it twice to perform the opposite
         */
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (controlM.getPlayer().getGameState() != 3) {
                controlM.getPlayer().setGameState(3);
            } else {
                controlM.getPlayer().setGameState(1);
            }
        }
        /**
         * Press M to save the controlM.getPlayer()'s progress
         */
        if (e.getKeyCode() == KeyEvent.VK_M) {
            controlM.getPlayer().saveGame();
        }
        /**
         * Press L to load the game at the title screen
         */
        if (e.getKeyCode() == KeyEvent.VK_L) {
            if (controlM.getPlayer().getGameState() == 0 || controlM.getPlayer().getGameState() == 1 ||
            controlM.getPlayer().getGameState() == 3) {
                controlM.getPlayer().setGameState(4);
            }
        }
        /**
         * Press Esc on the pause screen to exit completely
         */
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (controlM.getPlayer().getGameState() == 3) {
                controlM.getPlayer().setGameState(5);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            if (controlM.getPlayer().getGameState() != 8) {
                controlM.getPlayer().setGameState(8);
            } else {
                controlM.getPlayer().setGameState(1);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (controlM.getState()) {
            case BATTLE:
                battleKeyPressed(code);
                break;
            case OVERWORLD:
                overworldKeyPressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Overriden keyReleased method
     * The key released indicates the direction the
     * controlM.getcontrolM.getPlayer()() stopped
     * moving in
     * 
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (controlM.getState() == GlobalState.OVERWORLD) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                controlM.getPlayer().stopDirectionOfMovement("up");
            }
            if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                controlM.getPlayer().stopDirectionOfMovement("down");
            }
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                controlM.getPlayer().stopDirectionOfMovement("left");
            }
            if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                controlM.getPlayer().stopDirectionOfMovement("right");
            }

        }

    }

}
