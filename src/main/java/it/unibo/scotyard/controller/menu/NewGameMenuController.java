package it.unibo.scotyard.controller.menu;

import javax.swing.JPanel;

/**
 * Controller for the start new game menu screen.
 */
public interface NewGameMenuController {

    /**
     * @return the main panel of the new game menu.
     */
     JPanel getMainPanel();

    /**
     * Initiates game start sequence.
     * @param gameMode the game mode 
     * @param difficultyLevel the difficulty level
     * @param playerName the name of the player (useful to save the current game)
     */
    void play(String gameMode, String difficultyLevel, String playerName);

    /**
     * Exits the application.
     */
    void exit();

    /**
     * Displays the main menu.
     */
    void mainMenu();
}