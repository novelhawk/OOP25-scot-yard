package it.unibo.scotyard.view.menu;

import javax.swing.JPanel;

/**
 * start new game menu screen.
 */
public interface NewGameMenuView {

    /**
     * @return the main panel of the main menu.
     */
    JPanel getMainPanel();

    /**
     * Closes the new game menu window.
     */
    void close();
}