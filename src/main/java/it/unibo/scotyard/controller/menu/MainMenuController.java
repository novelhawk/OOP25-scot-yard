package it.unibo.scotyard.controller.menu;

import javax.swing.JPanel;

/** Controller for the main menu screen. */
public interface MainMenuController {

    /**
     * @return the main panel of the main menu.
     */
    JPanel getMainPanel();

    /** Displays new game menu. */
    void newGameMenu();

    /** Shows game statistics window. */
    void showStatistics();

    /** Resets all game records. */
    void resetRecords();

    /** Exits the application. */
    void exit();
}
