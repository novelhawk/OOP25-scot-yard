package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.game.turn.TurnManager;
import it.unibo.scotyard.model.map.MapNode;
import java.util.Map;

public abstract class PlayerImpl implements Player {

    protected static final int NONE = 0;
    protected static final int INFINITE = -1;

    protected int currentPositionId;
    protected MapNode currentPosition;
    protected Map<TicketType, Integer> tickets;
    protected String name;

    // For Mr.X game mode turn managament
    protected TurnManager<?> turnManager;

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

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setCurrentPosition(final int nodeId) {
        // Create a MapNode with the given ID
        this.currentPositionId = nodeId;
        this.currentPosition = new MapNode(nodeId, 0, 0);
    }

    /**
     * Ensures turn manager has been initialized.
     *
     * @throws IllegalStateException if not initialized
     */
    protected void ensureInitialized() {
        if (turnManager == null) {
            throw new IllegalStateException("Player not initialized. Call initialize(mapData) first.");
        }
    }
}
