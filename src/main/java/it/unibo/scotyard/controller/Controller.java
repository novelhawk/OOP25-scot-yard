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
     * Starts the game with selected configuration.
     */
    void startGame();

    /**
     * Exits the application.
     */
    void exit();
}