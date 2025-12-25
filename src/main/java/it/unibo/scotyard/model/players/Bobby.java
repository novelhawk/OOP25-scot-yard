package it.unibo.scotyard.model.players;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Bobby extends PlayerImpl {

    public Bobby() {
        super();
    }

    @Override
    public Map<TicketType, Integer> setInitialTickets() {
        final Map<TicketType, Integer> ticketsMap = new EnumMap<>(TicketType.class);
        ticketsMap.put(TicketType.TAXI, INFINITE);
        ticketsMap.put(TicketType.BUS, INFINITE);
        ticketsMap.put(TicketType.UNDERGROUND, INFINITE);
        ticketsMap.put(TicketType.BLACK, NONE);
        ticketsMap.put(TicketType.DOUBLE_MOVE, NONE);
        return ticketsMap;
    }

    @Override
    public void setInitialPosition(final List<Integer> initialPositions) {
        this.position = this.generateRandomInitialPosition(initialPositions);
    }
}
