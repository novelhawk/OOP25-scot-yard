package it.unibo.scotyard.model.players;

import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.game.turn.TurnManager;
import it.unibo.scotyard.model.game.turn.TurnManagerImpl;
import it.unibo.scotyard.model.inventory.Inventory;
import it.unibo.scotyard.model.inventory.InventoryImpl;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Set;

/**
 * The mister X player entity.
 *
 */
public final class MisterX extends AbstractPlayerImpl {

    /**
     * Creates a new AI Mister X player starting at the given position.
     *
     * @param position the starting position
     * @param brain    the AI brain
     */
    public MisterX(final NodeId position, final PlayerBrain brain) {
        super(position, brain);
        this.name = ViewConstants.MRX_STRING;
    }

    /**
     * Creates a new Mister X player starting at the given position.
     *
     * @param position the starting position
     */
    public MisterX(final NodeId position) {
        this(position, null);
    }

    @Override
    public void initializeInventory() {
        this.inventory = new InventoryImpl() {
            public int getInitialTickets(TicketType ticket){
                switch (ticket) {
                    default:
                    case TAXI: 
                    case BUS:
                    case UNDERGROUND:
                        return MagicNumbers.INFINITE;
                    case BLACK : return MagicNumbers.NUMBER_TICKETS_BLACK;
                    case DOUBLE_MOVE : return MagicNumbers.NUMBER_TICKETS_DOUBLE_MOVE;
                }
            }
        };
        this.inventory.initialize();
    }

    public void makeMove(final NodeId destination, final TransportType transport, final int turnNumber) {
        ensureInitialized();

        // Get ticket richiesto per questo trasporto
        final TicketType ticketType = Inventory.getTicketTypeForTransport(transport);

        // valida e usa il ticket
        if (!useTicket(ticketType)) {
            throw new IllegalStateException("Cannot make move: insufficient " + ticketType + " tickets");
        }

        // esegue la mossa
        final NodeId newPosition = executeMoveInternal(destination, transport, turnNumber);
        setPosition(newPosition);
    }

    /**
     * Initializes the player with turn manager. This must be called before using
     * turn-related methods.
     *
     * @param mapData the map data for move validation
     */
    public void initialize(final MapData mapData) {
        this.turnManager = createTurnManager(mapData);
    }

    /**
     * Factory method for creating player-specific turn manager. Subclasses override
     * this to provide their specific turn
     * manager implementation.
     *
     * @param mapData the map data
     * @return the turn manager instance
     */
    protected TurnManager<?> createTurnManager(final MapData mapData) {
        return new TurnManagerImpl(mapData);
    }

    /**
     * Gets the turn manager implementation.
     *
     * @return the turn manager implementation
     */
    private TurnManagerImpl getTurnManagerImpl() {
        return (TurnManagerImpl) turnManager;
    }

    protected NodeId executeMoveInternal(
            final NodeId destination, final TransportType transport, final int turnNumber) {
        return getTurnManagerImpl().executeMove(getPosition(), destination, transport, turnNumber);
    }

    // --- MrX- Metodi Specifici (Double Move) ---

    /**
     * Starts a double move sequence with the first move.
     *
     * @param firstDestination the first move destination
     * @param firstTransport   the first move transport
     * @param turnNumber       the current turn number
     * @throws IllegalStateException if DOUBLE_MOVE ticket not available
     */
    public void startDoubleMove(
            final NodeId firstDestination, final TransportType firstTransport, final int turnNumber) {
        ensureInitialized();

        // Usa DOUBLE_MOVE
        if (!useTicket(TicketType.DOUBLE_MOVE)) {
            throw new IllegalStateException("Cannot perform double move: no DOUBLE_MOVE tickets available");
        }

        final TicketType ticketType = Inventory.getTicketTypeForTransport(firstTransport);
        useTicket(ticketType);

        // first move
        final NodeId newPosition =
                getTurnManagerImpl().startDoubleMove(getPosition(), firstDestination, firstTransport, turnNumber);
        setPosition(newPosition);
    }

    /**
     * Completes a double move sequence with the second move.
     *
     * @param secondDestination the second move destination
     * @param secondTransport   the second move transport
     * @param turnNumber        the current turn number
     */
    public void completeDoubleMove(
            final NodeId secondDestination, final TransportType secondTransport, final int turnNumber) {
        ensureInitialized();

        final TicketType ticketType = Inventory.getTicketTypeForTransport(secondTransport);
        useTicket(ticketType);

        // second move
        final NodeId newPosition =
                getTurnManagerImpl().completeDoubleMove(secondDestination, secondTransport, turnNumber);
        setPosition(newPosition);
    }

    /**
     * Checks if double move is available.
     *
     * @return true if double move can be used
     */
    public boolean isDoubleMoveAvailable() {
        ensureInitialized();
        return getTurnManagerImpl().isDoubleMoveAvailable();
    }

    /**
     * Gets all valid moves from the current position.
     *
     * @param occupiedPositions set of positions occupied by other players
     * @return set of valid move options
     */
    public Set<TurnManagerImpl.MoveOption> getValidMoves(final Set<NodeId> occupiedPositions) {
        ensureInitialized();
        return getTurnManagerImpl().getValidMoves(this.getPosition(), occupiedPositions);
    }
}
