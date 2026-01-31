package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.game.turn.TurnManager;
import it.unibo.scotyard.model.inventory.Inventory;
import it.unibo.scotyard.model.inventory.InventoryImpl;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Optional;

/**
 * The default player entity implementation.
 *
 */
public abstract class PlayerImpl implements Player {

    private final PlayerBrain brain;
    private NodeId position;
    protected Inventory inventory;
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
        this.inventory = new InventoryImpl();
        this.initializeInventory();
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
    public void initializeInventory() {
        this.inventory.initialize(this);
    }

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
        return this.inventory.getNumberTickets(ticketType);
    }

    @Override
    public boolean useTicket(final TicketType ticket) {
        if (this.inventory.containsTicket(ticket)) {
            this.inventory.decrementTickets(ticket);
            return true;
        } else {
            return false;
        }
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

    @Override
    public boolean hasTransportModeTicket(TransportType transportType) {
        final TicketType specificTicketType = Inventory.getTicketTypeForTransport(transportType);
        return getNumberTickets(specificTicketType) > 1;
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
