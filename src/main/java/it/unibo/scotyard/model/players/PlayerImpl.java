package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.map.NodeId;
import java.util.List;
import java.util.Map;

/**
 * The default player entity implementation.
 *
 */
public abstract class PlayerImpl implements Player {

    protected static final int NONE = 0;
    protected static final int INFINITE = -1;

    protected NodeId position;
    protected Map<TicketType, Integer> tickets;

    public PlayerImpl() {
        this.tickets = this.setInitialTickets();
        // this.setInitialPosition(null);
    }

    @Override
    public abstract Map<TicketType, Integer> setInitialTickets();

    protected NodeId generateRandomInitialPosition(List<Integer> initialPositions) {
        return new NodeId(1);
    }

    @Override
    public abstract void setInitialPosition(List<Integer> initialPositions);

    @Override
    public NodeId getPosition() {
        return this.position;
    }

    @Override
    public int getNumberTickets(final TicketType ticketType) {
        return this.tickets.get(ticketType);
    }

    @Override
    public boolean useTicket(final TicketType ticket) {
        if (this.tickets.containsKey(ticket) && this.tickets.get(ticket) > NONE) {
            if (this.tickets.get(ticket) != INFINITE) {
                this.tickets.put(ticket, this.tickets.get(ticket) - 1);
            }
            return true;
        }
        return false;
    }
}
