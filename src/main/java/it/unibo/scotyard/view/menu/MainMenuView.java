package it.unibo.scotyard.view.menu;

import javax.swing.JPanel;

/**
 * Main menu screen.
 */
public interface MainMenuView {

    /**
     * @return the main panel of the main menu.
     */
    JPanel getMainPanel();

    /**
     * Closes the main menu window and exits the application.
     */
    void close();
}
