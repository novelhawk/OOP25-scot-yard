package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.inventory.Inventory;
import it.unibo.scotyard.model.map.NodeId;
import java.util.Optional;

/*
 * Player interface.
 */
public interface Player {

    /**
     * Sets the position for the player.
     *
     * @param nodeId the input position
     */
    void setPosition(NodeId nodeId);

    /**
     * @return the current position of the player on the map
     */
    NodeId getPosition();

    /**
     * Intialized the inventory
     */
    void initializeInventory();

    /**
     * Gets the player's inventory.
     *
     * @return the player's inventory
     */
    Inventory getInventory();

    /**
     * @param ticketType the enum of ticket
     * @return the number of tickets possessed by the player of the type passed as a
     *         paremeter.
     */
    int getNumberTickets(TicketType ticketType);

    /**
     * The player uses a specific type of ticket, if it's available. The method
     * returns a boolean
     * value, which indicates whether the operation has been successfull or not.
     *
     * @return true if the player can use the ticket (according to the availabilty),
     *         else false
     * @param ticket the ticket type
     */
    boolean useTicket(TicketType ticket);

    /**
     * Sets a new name for the player.
     *
     * @param newName the new name to assign to player
     */
    void setName(String newName);

    /**
     * Return a String representing the name of the kind of player (detective,
     * bobby, mister X).
     *
     * @return the name of the player (their kind : detective, etc.)
     */
    String getName();

    /**
     * Gets whether the player is controlled by a human or by AI.
     *
     * @return true if the player is controlled by a human
     */
    boolean isHuman();

    /**
     * Gets the AI brain that plays the player if present.
     *
     * @return the AI brain
     */
    Optional<PlayerBrain> getBrain();
}
