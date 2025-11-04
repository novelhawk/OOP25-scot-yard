package it.unibo.scotyard.controller.menu;

/**
 * Controller for the start menu screen.
 */
public interface StartMenuController {

    /**
     * Displays the start menu.
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