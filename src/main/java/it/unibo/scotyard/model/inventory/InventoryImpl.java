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
    abstract public void initialize();

    @Override
    public void addTicket(TicketType ticketType, int numberTickets){
        this.ticketsMap.put(ticketType, numberTickets);
    }


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
