package it.unibo.scotyard.model.inventory;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.EnumMap;
import java.util.Map;

public class InventoryImpl implements Inventory {

    protected Map<TicketType, Integer> ticketsMap;

    public InventoryImpl() {
        this.ticketsMap = new EnumMap<>(TicketType.class);
    }

    @Override
    public void initialize(Player player) {
        if (player instanceof Detective) {
            this.ticketsMap.put(TicketType.TAXI, Constants.NUMBER_TICKETS_TAXI);
            this.ticketsMap.put(TicketType.BUS, Constants.NUMBER_TICKETS_BUS);
            this.ticketsMap.put(TicketType.UNDERGROUND, Constants.NUMBER_TICKETS_UNDERGROUND);
            this.ticketsMap.put(TicketType.BLACK, Constants.NONE);
            this.ticketsMap.put(TicketType.DOUBLE_MOVE, Constants.NONE);
        }
        if (player instanceof Bobby) {
            this.ticketsMap.put(TicketType.TAXI, Constants.INFINITE);
            this.ticketsMap.put(TicketType.BUS, Constants.INFINITE);
            this.ticketsMap.put(TicketType.UNDERGROUND, Constants.INFINITE);
            this.ticketsMap.put(TicketType.BLACK, Constants.NONE);
            this.ticketsMap.put(TicketType.DOUBLE_MOVE, Constants.NONE);
        }
        if (player instanceof MisterX) {
            this.ticketsMap.put(TicketType.TAXI, Constants.INFINITE);
            this.ticketsMap.put(TicketType.BUS, Constants.INFINITE);
            this.ticketsMap.put(TicketType.UNDERGROUND, Constants.INFINITE);
            this.ticketsMap.put(TicketType.BLACK, Constants.NUMBER_TICKETS_BLACK);
            this.ticketsMap.put(TicketType.DOUBLE_MOVE, Constants.NUMBER_TICKETS_DOUBLE_MOVE);
        }
    }

    @Override
    public int getNumberTickets(TicketType ticketType) {
        return this.ticketsMap.get(ticketType);
    }

    @Override
    public boolean containsTicket(TicketType ticketType) {
        return this.ticketsMap.containsKey(ticketType)
                && (this.ticketsMap.get(ticketType) > 0 || this.ticketsMap.get(ticketType) == Constants.INFINITE);
    }

    @Override
    public void decrementTickets(TicketType ticketType) {
        final int currentTickets = this.ticketsMap.get(ticketType);
        if (currentTickets != Constants.INFINITE && currentTickets != Constants.NONE) {
            this.ticketsMap.put(ticketType, currentTickets - 1);
        }
    }
}
