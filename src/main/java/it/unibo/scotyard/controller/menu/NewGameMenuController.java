package it.unibo.scotyard.controller.menu;

/**
 * Controller for the start new game menu screen.
 */
public interface NewGameMenuController {

    /**
     * Displays the start new game menu.
     */
    void run();

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