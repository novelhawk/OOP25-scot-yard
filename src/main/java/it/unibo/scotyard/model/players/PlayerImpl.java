package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.map.MapNode;
import java.util.List;
import java.util.Map;

public abstract class PlayerImpl implements Player {

    protected static final int NONE = 0;
    protected static final int INFINITE = -1;

    protected MapNode currentPosition;
    protected Map<TicketType, Integer> tickets;

    public PlayerImpl() {
        this.tickets = this.setInitialTickets();
        // this.setInitialPosition(null);
    }

    @Override
    public abstract Map<TicketType, Integer> setInitialTickets();

    protected MapNode generateRandomInitialPosition(List<Integer> initialPositions) {
        MapNode initialMapNode = new MapNode(INFINITE, NONE, INFINITE);
        return initialMapNode;
    }

    @Override
    public abstract void setInitialPosition(List<Integer> initialPositions);

    @Override
    public MapNode getCurrentPosition() {
        return this.currentPosition;
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
