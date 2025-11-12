package it.unibo.scotyard.controller;

import javax.swing.JPanel;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.model.game.Game;

/**
 * Main controller interface for the application flow.
 */
public interface Controller {

    /**
     * Launches the application.
     */
    void launch();

    /**
     * Displays the panel passed.
     * @param panel the panel to display on the window.
     */
    void displayPanel(JPanel panel);

    /**
     * Loads the main menu.
     */
    void loadMainMenu();

    /** 
     * Loads the new game menu.
    */
    void loadNewGameMenu();

    /**
     * Loads the game panel and initializes the view with map data.
     * 
     * @param resolution the resoultion of the window
     */
    void loadGamePanel(final Size resolution);

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