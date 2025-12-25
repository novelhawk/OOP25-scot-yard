package it.unibo.scotyard.model.players;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Detective extends PlayerImpl {

    private static final int NUMBER_TICKETS_TAXI = 11;
    private static final int NUMBER_TICKETS_BUS = 8;
    private static final int NUMBER_TIKCETS_UNDERGROUND = 4;

    public Detective() {
        super();
    }

    @Override
    public Map<TicketType, Integer> setInitialTickets() {
        final Map<TicketType, Integer> ticketsMap = new EnumMap<>(TicketType.class);
        ticketsMap.put(TicketType.TAXI, NUMBER_TICKETS_TAXI);
        ticketsMap.put(TicketType.BUS, NUMBER_TICKETS_BUS);
        ticketsMap.put(TicketType.UNDERGROUND, NUMBER_TIKCETS_UNDERGROUND);
        ticketsMap.put(TicketType.BLACK, NONE);
        ticketsMap.put(TicketType.DOUBLE_MOVE, NONE);
        return ticketsMap;
    }

    @Override
    public void setInitialPosition(List<Integer> initialPositions) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setInitialPosition'");
    }
}
