package it.unibo.scotyard.view.game;

import javax.swing.JPanel;

import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;

public interface GameView {

    /**
     * @return the game panel (with the map panel and the sidebar)
     */
    JPanel getMainPanel();
    
    /**
     * @return the sidebar component
     */
    SidebarPanel getSidebar();

    /**
     * @return the map panel component
     */
    MapPanel getMapPanel();
}
