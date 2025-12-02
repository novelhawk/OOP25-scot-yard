package it.unibo.scotyard.controller.menu;

import javax.swing.JPanel;

/** Controller for the main menu screen. */
public interface MainMenuController {

    /** @return the main panel of the main menu. */
    JPanel getMainPanel();

    /** Displays new game menu. */
    void newGameMenu();

    /** Exits the application. */
    void exit();
}
