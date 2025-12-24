package it.unibo.scotyard.view.game;

import it.unibo.scotyard.controller.game.GameController;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import java.util.Set;
import javax.swing.JPanel;

public interface GameView {

    /**
     * Set the observer, that is the GameController
     *
     * @param gameController the observer
     */
    void setObserver(GameController gameController);

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

    /**
     * Displays the game over window, which contains a label inidicating the result of user player and a button that
     * takes the user back to the main menu.
     *
     * @param result the strind inidicating the result (win or loss)
     */
    void displayGameOverWindow(String result);

    /**
     * Load a window to make the user select the preferred transport type to reach the destination. This method gets
     * called only if there are multiple transport types available to reach the destination.
     *
     * @param availableTransportTypes a set of available transport types
     */
    void loadTransportSelectionDialog(Set<TransportType> availableTransportTypes);

    /**
     * Calls the method destinationChosen of the GameController by passing the destinationId, when the user has clicked
     * on one destination in the map panel.
     *
     * @param destinationId the id of the selected destination
     */
    void destinationChosen(int destinationId);
}
