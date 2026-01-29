package it.unibo.scotyard.model.inventory;

import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;

public interface Inventory {

    /**
     * Initialized the inventory according to the type of player received in input.
     * 
     * @param player the player
     */
    void initialize(Player player);
    
    /**
     * @return the number of tickets possessed by the player of the type passed as a paremeter.
     */
    int getNumberTickets(TicketType ticketType);

    /**
     * Returns a boolean whicih indicates whether the inventory contains the ticket type received
     * as input, in a quantity > 0.
     * 
     * @param ticketType the ticket type
     * @return a boolean that indicates if the inventory contains the ticket type pssed as paremeter
     */
    boolean containsTicket(TicketType ticketType);

    /**
     * Decrements the number of tickets of ticket type given in input.
     * 
     * @param ticketType the type of ticket
     */
    void decrementTickets(TicketType ticketType);

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
}
