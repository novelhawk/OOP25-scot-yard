package it.unibo.scotyard.model.players;

import java.util.Map;

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

    /** @return the number of tickets possessed by the player of the type passed as a paremeter. */
    int getNumberTickets(TicketType ticketType);

    /**
     * The player uses a specific type of ticket, if it's available. The method returns a boolean value, which indicates
     * whether the operation has been successfull or not.
     *
     * @return true if the player can use the ticket (according to the availabilty), else false
     */
    boolean useTicket(TicketType ticket);
}
