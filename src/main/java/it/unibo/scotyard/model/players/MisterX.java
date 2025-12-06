package it.unibo.scotyard.model.players;

import java.util.HashMap;
import java.util.Map;

public class MisterX extends PlayerImpl {

    private static final int NUMBER_TICKETS_BLACK = 5;
    private static final int NUMBER_TICKETS_DOUBLE_MOVE = 2;
    private static final int INFINITE = -1;

    public MisterX() {
        super();
    }

    @Override
    public Map<TicketType, Integer> setInitialTickets() {
        Map<TicketType, Integer> ticketsMap = new HashMap<>();
        ticketsMap.put(TicketType.BLACK, NUMBER_TICKETS_BLACK);
        ticketsMap.put(TicketType.DOUBLE_MOVE, NUMBER_TICKETS_DOUBLE_MOVE);
        ticketsMap.put(TicketType.TAXI, INFINITE);
        ticketsMap.put(TicketType.BUS, INFINITE);
        ticketsMap.put(TicketType.UNDERGROUND, INFINITE);
        return ticketsMap;
    }
}
