package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.map.NodeId;
import java.util.EnumMap;
import java.util.Map;

public class Detective extends PlayerImpl {

    private static final int NUMBER_TICKETS_TAXI = 11;
    private static final int NUMBER_TICKETS_BUS = 8;
    private static final int NUMBER_TIKCETS_UNDERGROUND = 4;

    /**
     * Creates a new AI Detective player starting at the given position.
     *
     * @param position the starting position
     * @param brain the AI brain
     */
    public Detective(NodeId position, PlayerBrain brain) {
        super(position, brain);
        this.name = "Mister X";
    }

    /**
     * Creates a new Detective player starting at the given position.
     *
     * @param position the starting position
     */
    public Detective(NodeId position) {
        super(position);
        this.name = "Detective";
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
}
