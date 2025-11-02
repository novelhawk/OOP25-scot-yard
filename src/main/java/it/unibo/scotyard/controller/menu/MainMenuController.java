package it.unibo.scotyard.controller.menu;

/**
 * Controller for the main menu screen.
 */
public interface MainMenuController {

    /**
     * Displays the main menu.
     */
    void run();

    /**
     * Displays new game menu.
     */
    void newGame();

    /**
     * Exits the application.
     */
    void exit();
}