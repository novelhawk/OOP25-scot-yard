package it.unibo.scotyard.model.players;

import java.util.Map;

import it.unibo.scotyard.model.map.MapNode;

/*
 * Player interface
 */
public interface Player {

    /**
     * Generates a random initial position for the player.
     * @return the initial position of the player.
     */
    MapNode setInitialPosition(); 

    /**
     * @return the map containing the number of tickets possessed by the player for each ticket type.
     */
    Map<TicketType,Integer> setInitialTickets();

    /**
     * @return the current position of the player on the map
     */
    MapNode getCurrentPosition();

    /**
     * @return the number of tickets possessed by the player of the type passed as a paremeter.
     */
    int getNumberTickets(TicketType ticketType);
}
