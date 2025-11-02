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
     */
    void play();

    /**
     * Exits the application.
     */
    void exit();
}