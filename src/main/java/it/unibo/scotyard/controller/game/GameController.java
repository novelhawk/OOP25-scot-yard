package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;

import java.util.Set;

import javax.swing.JPanel;

/**
 * The controller for all game related actions
 *
 */
public interface GameController {

    /**
     * Initializes game.
     */
    void initializeGame();

    /**
     * @return the main game panel
     */
    JPanel getMainPanel();

    /**
     * @return the map panel
     */
    MapPanel getMapPanel();

    /**
     * @return the sidebar panel
     */
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

    /**
     * Updates the sidebar, whenever a new round for a player starts. This function takes as an argument the current
     * player.
     *
     * @param currentPlayer
     */
    void updateSidebar(Player currentPlayer);

    /** @return a boolean value which indicates whether the game is over or not */
    boolean isGameOver();

    /** Load the game over window. */
    void loadGameOverWindow();

    /** Load the main menu. */
    void loadMainMenu();

    /**
     * Loads the possible destinations for the current player, from main controller.
     * 
     * @return a set of pairs of possible destinations
     */
    Set<Pair<NodeId, TransportType>> loadPossibleDestinations();

    /**
     * Checks if there are multiple transport types to reach destination or not. Used only in
     * DetectiveGameControllerImpl.
     *
     * @param newPositionId the id of the destination
     */
    void destinationChosen(NodeId newPositionId);

    /**
     * Sets the selcted transport type to reach destination. Used only in DetectiveGameControllerImpl.
     *
     * @param transportType the type of transport selected
     */
    void selectTransport(TransportType transportType);

}
