package it.unibo.scotyard.model.players;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The mister X player entity.
 *
 */
public final class MisterX extends PlayerImpl {

    private static final int NUMBER_TICKETS_BLACK = 5;
    private static final int NUMBER_TICKETS_DOUBLE_MOVE = 2;
    private static final int INFINITE = -1;

    /**
     * Creates a new mister X player entity.
     */
    public MisterX() {
        super();
    }

    @Override
    public Map<TicketType, Integer> setInitialTickets() {
        final Map<TicketType, Integer> ticketsMap = new EnumMap<>(TicketType.class);
        ticketsMap.put(TicketType.BLACK, NUMBER_TICKETS_BLACK);
        ticketsMap.put(TicketType.DOUBLE_MOVE, NUMBER_TICKETS_DOUBLE_MOVE);
        ticketsMap.put(TicketType.TAXI, INFINITE);
        ticketsMap.put(TicketType.BUS, INFINITE);
        ticketsMap.put(TicketType.UNDERGROUND, INFINITE);
        return ticketsMap;
    }

    @Override
    public void setInitialPosition(final List<Integer> initialPositions) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setInitialPosition'");
    }
}
