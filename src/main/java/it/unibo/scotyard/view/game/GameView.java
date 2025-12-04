package it.unibo.scotyard.view.game;

import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import javax.swing.JPanel;

public interface GameView {

    /** @return the game panel (with the map panel and the sidebar) */
    JPanel getMainPanel();

    /** @return the sidebar component */
    SidebarPanel getSidebar();

    /** @return the map panel component */
    MapPanel getMapPanel();

    /**
     * Displays the small window which displays a summary of the game rules. This method is called after the user has
     * pressed the button rules in the sidebar.
     *
     * @param panel the panel of the rules window.
     */
    void displayRulesWindow(JPanel panel);
}
