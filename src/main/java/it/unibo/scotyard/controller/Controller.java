package it.unibo.scotyard.controller;

/**
 * Main controller interface for the application flow.
 */
public interface Controller {

    /**
     * Launches the application.
     */
    void launch();

    /**
     * Displays the main menu.
     */
    void displayMainMenu();

    /**
     * Starts the game with selected configuration.
     * @param gameMode the game mode 
     * @param difficultyLevel the difficulty level
     * @param playerName the name of the player (useful to save the current game)
     */
    void startGame(String gameMode, String difficultyLevel, String playerName);

    /**
     * Exits the application.
     */
    void exit();

    
}