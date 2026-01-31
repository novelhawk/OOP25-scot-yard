package it.unibo.scotyard.controller.menu;

import it.unibo.scotyard.view.menu.MainMenuView;

/** Controller for the main menu screen. */
public interface MainMenuController {

    /**
     * Sets the view using this controller.
     *
     * @param view the MainMenu view
     */
    void setView(MainMenuView view);

    /** Displays new game menu. */
    void newGameMenu();

    /** Shows game statistics window. */
    void showStatistics();

    /** Exits the application. */
    void exit();
}
