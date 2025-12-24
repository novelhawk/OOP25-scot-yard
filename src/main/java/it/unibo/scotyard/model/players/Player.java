package it.unibo.scotyard.model.players;

import java.util.Map;

import it.unibo.scotyard.model.map.MapNode;
import it.unibo.scotyard.model.map.TransportType;

/*
 * Player interface
 */
public interface Player {

    /**
     * Sets the position for the player.
     *
     * @param position the input position
     */
    void setPosition(int newPosition);

    /** @return the map containing the number of tickets possessed by the player for each ticket type */
    Map<TicketType, Integer> setInitialTickets();

    /** @return the current position of the player on the map */
    int getCurrentPositionId();

    /** @return thhe current MapNode position */
    MapNode getCurrentPosition();

    /** @return the number of tickets possessed by the player of the type passed as a paremeter. */
    int getNumberTickets(TicketType ticketType);

    /**
     * Given a transport type, returns the corresponding TicketType.
     * 
     * @param transport
     * @return the corresponding TicketType to the given TransportType
     */
    public static TicketType getTicketTypeForTransport(final TransportType transport){
        switch (transport) {
            case TAXI:
                return TicketType.TAXI;
            case BUS:
                return TicketType.BUS;
            case UNDERGROUND:
                return TicketType.UNDERGROUND;
            case FERRY:
                return TicketType.BLACK;
            default:
                throw new IllegalArgumentException("Trasporto non conosciuto: " + transport);
        }
    }

    /**
     * The player uses a specific type of ticket, if it's available. The method returns a boolean value, which indicates
     * whether the operation has been successfull or not.
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
     * Sets the current position of the player.
     *
     * @param nodeId the node ID to set as current position
     */
    void setCurrentPosition(int nodeId);
}
