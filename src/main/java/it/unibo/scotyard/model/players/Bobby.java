package it.unibo.scotyard.model.players;

import java.util.HashMap;
import java.util.Map;

public class Bobby extends PlayerImpl {

    public Bobby() {
        super();
        this.name = "Bobby";
    }

    @Override
    public Map<TicketType, Integer> setInitialTickets() {
        Map<TicketType, Integer> ticketsMap = new HashMap<>();
        ticketsMap.put(TicketType.TAXI, INFINITE);
        ticketsMap.put(TicketType.BUS, INFINITE);
        ticketsMap.put(TicketType.UNDERGROUND, INFINITE);
        ticketsMap.put(TicketType.BLACK, NONE);
        ticketsMap.put(TicketType.DOUBLE_MOVE, NONE);
        return ticketsMap;
    }
}
