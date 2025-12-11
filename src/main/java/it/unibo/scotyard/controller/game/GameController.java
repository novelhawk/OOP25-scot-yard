package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import javax.swing.JPanel;

public interface GameController {

    /** @return the main game panel */
    JPanel getMainPanel();

    /** @return the map panel */
    MapPanel getMapPanel();

    /** @return the sidebar panel */
    SidebarPanel getSidebarPanel();

    /**
     * Returns the current game mode.
     *
     * @return the current game mode
     */
    GameMode getGameMode();

    /**
     * Returns the current round number.
     *
     * @return the round number
     */
    int getNumberRound();

    /**
     * Returns the number of tickets of a specific type possessed by the user.
     *
     * @param ticketType the ticket type
     * @return the number of tickets
     */
    int getNumberTicketsUserPlayer(TicketType ticketType);

    /** Updates the sidebar, whenever a new round starts. */
    void updateSidebar();

    /**
     * @return a boolean value which indicates whether the game is over or not
     */
    boolean isGameOver();

    /**
     * Load the game over window.
     */
    void loadGameOverWindow();

    /** 
     * Load the main menu.
     */
    void loadMainMenu();

    /**
     * Manages a round of a game. 
     * If the game is over, it calls a method of the GameView, which opens a the game over
     * window, which takes back the user to the main menu.
     */
    void manageGameRound();
}
