package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Map;
import java.util.Optional;

/*
 * Player interface
 */
public interface Player {

    /**
     * Sets the position for the player.
     *
     * @param nodeId the input position
     */
    void setPosition(NodeId nodeId);

    /**
     * @return the map containing the number of tickets possessed by the player for each ticket type
     */
    Map<TicketType, Integer> setInitialTickets();

    /**
     * @return the current position of the player on the map
     */
    NodeId getPosition();

    /**
     * @return the number of tickets possessed by the player of the type passed as a paremeter.
     */
    int getNumberTickets(TicketType ticketType);

    /**
     * Given a transport type, returns the corresponding TicketType.
     *
     * @param transport the transport type
     * @return the corresponding TicketType to the given TransportType
     */
    static TicketType getTicketTypeForTransport(final TransportType transport) {
        return switch (transport) {
            case TAXI -> TicketType.TAXI;
            case BUS -> TicketType.BUS;
            case UNDERGROUND -> TicketType.UNDERGROUND;
            case FERRY -> TicketType.BLACK;
        };
    }

    /**
     * The player uses a specific type of ticket, if it's available. The method returns a boolean
     * value, which indicates whether the operation has been successfull or not.
     *
     * @return true if the player can use the ticket (according to the availabilty), else false
     */
    boolean useTicket(TicketType ticket);

    /**
     * Sets a new name for the player.
     *
     * @param newName the new name to assign to player
     */
    void setName(String newName);

    /**
     * Return a String representing the name of the kind of player (detective, bobby, mister X).
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

    /**
     * Returns whether the player has the tickets to take the supplied transport type.
     *
     * @param transportType the transport type
     * @return whether the user has the ticket
     */
    boolean hasTransportModeTicket(TransportType transportType);
}
