package it.unibo.scotyard.model.inventory;

import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.model.players.TicketType;
import java.util.EnumMap;
import java.util.Map;

public abstract class InventoryImpl implements Inventory {

    private Map<TicketType, Integer> ticketsMap;

    public InventoryImpl() {
        this.ticketsMap = new EnumMap<>(TicketType.class);
    }

    @Override
    public void initialize() {
        this.ticketsMap.put(TicketType.TAXI, getInitialTickets(TicketType.TAXI));
        this.ticketsMap.put(TicketType.BUS, getInitialTickets(TicketType.BUS));
        this.ticketsMap.put(TicketType.UNDERGROUND, getInitialTickets(TicketType.UNDERGROUND));
        this.ticketsMap.put(TicketType.BLACK, getInitialTickets(TicketType.BLACK));
        this.ticketsMap.put(TicketType.DOUBLE_MOVE, getInitialTickets(TicketType.DOUBLE_MOVE));
    }

    @Override
    public abstract int getInitialTickets(TicketType ticket);

    @Override
    public int getNumberTickets(TicketType ticketType) {
        return this.ticketsMap.get(ticketType);
    }

    @Override
    public boolean containsTicket(TicketType ticketType) {
        return this.ticketsMap.containsKey(ticketType)
                && (this.ticketsMap.get(ticketType) > 0 || this.ticketsMap.get(ticketType) == MagicNumbers.INFINITE);
    }

    @Override
    public void decrementTickets(TicketType ticketType) {
        final int currentTickets = this.ticketsMap.get(ticketType);
        if (currentTickets != MagicNumbers.INFINITE && currentTickets != MagicNumbers.NONE) {
            this.ticketsMap.put(ticketType, currentTickets - 1);
        }
    }
}
