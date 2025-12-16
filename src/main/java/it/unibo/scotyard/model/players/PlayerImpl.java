package it.unibo.scotyard.model.players;

import java.util.Map;

public abstract class PlayerImpl implements Player {

    protected static final int NONE = 0;
    protected static final int INFINITE = -1;

    protected int currentPositionId;
    protected Map<TicketType, Integer> tickets;

    public PlayerImpl() {
        this.tickets = this.setInitialTickets();
    }

    @Override
    public abstract Map<TicketType, Integer> setInitialTickets();

    @Override
    public void setPosition(int newPosition) {
        this.currentPositionId = newPosition;
    }

    @Override
    public int getCurrentPositionId() {
        return this.currentPositionId;
    }

    @Override
    public int getNumberTickets(TicketType ticketType) {
        return this.tickets.get(ticketType);
    }

    @Override
    public boolean useTicket(TicketType ticket) {
        if (this.tickets.containsKey(ticket) && this.tickets.get(ticket) > NONE) {
            if (this.tickets.get(ticket) != INFINITE) {
                this.tickets.put(ticket, this.tickets.get(ticket) - 1);
            }
            return true;
        }
        return false;
    }
}
