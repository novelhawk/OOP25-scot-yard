package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.game.turn.TurnManager;
import it.unibo.scotyard.model.map.NodeId;
import java.util.Map;
import java.util.Optional;

/**
 * The default player entity implementation.
 *
 */
public abstract class PlayerImpl implements Player {

    protected static final int NONE = 0;
    protected static final int INFINITE = -1;

    private final PlayerBrain brain;
    private NodeId position;
    protected Map<TicketType, Integer> tickets;
    protected String name;

    // For Mr.X game mode turn management
    protected TurnManager<?> turnManager;

    /**
     * Creates a new AI player starting at the given position.
     *
     * @param position the starting position
     * @param brain the AI brain
     */
    public PlayerImpl(NodeId position, PlayerBrain brain) {
        this.position = position;
        this.tickets = this.setInitialTickets();
        this.brain = brain;
    }

    /**
     * Creates a new player starting at the given position.
     *
     * @param position the starting position
     */
    public PlayerImpl(NodeId position) {
        this(position, null);
    }

    @Override
    public abstract Map<TicketType, Integer> setInitialTickets();

    @Override
    public void setPosition(final NodeId newPosition) {
        this.position = newPosition;
    }

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
        if (this.tickets.containsKey(ticket)) {
            final int currentTickets = this.tickets.get(ticket);
            // INFINITE (-1) o > 0
            if (currentTickets == INFINITE || currentTickets > NONE) {
                // Decrementa solo se non infinito
                if (currentTickets != INFINITE) {
                    this.tickets.put(ticket, currentTickets - 1);
                }
                return true;
            }
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
    public boolean isHuman() {
        return brain == null;
    }

    @Override
    public Optional<PlayerBrain> getBrain() {
        return Optional.ofNullable(brain);
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
