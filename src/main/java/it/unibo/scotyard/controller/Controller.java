package it.unibo.scotyard.controller;

import it.unibo.scotyard.controller.game.GameController;

import java.util.List;

import javax.swing.JPanel;

/** Main controller interface for the application flow. */
public interface Controller {

    /** Launches the application. */
    void launch();

    /**
     * Displays the panel passed.
     *
     * @param panel the panel to display on the window.
     */
    void displayPanel(JPanel panel);

    /** Loads the main menu. */
    void loadMainMenu();

    /** Loads the new game menu. */
    void loadNewGameMenu();

    /**
     * Loads the game panel and initializes the view with map data.
     *
     * @param gameController the Controller relative to the game
     */
    void loadGamePanel(GameController gameController);

    /**
     * Starts the game with selected configuration.
     *
     * @param gameMode the game mode
     * @param difficultyLevel the difficulty level
     * @param playerName the name of the player (useful to save the current game)
     */
    void startGame(String gameMode, String difficultyLevel, String playerName);

    /**
     * Return a list of integers representing the ids of the possible destinations
     * (loaded from the Model) for a player in a certain initial position.
     * 
     * @param initialPosition the id of the starting position of the player
     * @return a list of ids of possible destinations
     */
    List<Integer> getPossibleDestinations(int initialPosition);

    /** Exits the application. */
    void exit();
}
