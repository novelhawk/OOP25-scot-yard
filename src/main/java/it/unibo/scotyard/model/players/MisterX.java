package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.game.turn.TurnManager;
import it.unibo.scotyard.model.game.turn.TurnManagerImpl;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.TransportType;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * The mister X player entity.
 *
 */
public final class MisterX extends PlayerImpl {

    private static final int NUMBER_TICKETS_BLACK = 5;
    private static final int NUMBER_TICKETS_DOUBLE_MOVE = 2;
    private static final int INFINITE = -1;

    /**
     * Creates a new mister X player entity.
     */
    public MisterX() {
        this.name = "Mister X";
    }

    @Override
    public Map<TicketType, Integer> setInitialTickets() {
        final Map<TicketType, Integer> ticketsMap = new EnumMap<>(TicketType.class);
        ticketsMap.put(TicketType.BLACK, NUMBER_TICKETS_BLACK);
        ticketsMap.put(TicketType.DOUBLE_MOVE, NUMBER_TICKETS_DOUBLE_MOVE);
        ticketsMap.put(TicketType.TAXI, INFINITE);
        ticketsMap.put(TicketType.BUS, INFINITE);
        ticketsMap.put(TicketType.UNDERGROUND, INFINITE);
        return ticketsMap;
    }

    public void makeMove(final int destination, final TransportType transport, final int turnNumber) {
        ensureInitialized();

        // Get ticket richiesto per questo trasporto
        final TicketType ticketType = Player.getTicketTypeForTransport(transport);

        // valida e usa il ticket
        if (!useTicket(ticketType)) {
            throw new IllegalStateException("Cannot make move: insufficient " + ticketType + " tickets");
        }

        // esegue la mossa
        final int newPosition = executeMoveInternal(destination, transport, turnNumber);
        setCurrentPosition(newPosition);
    }

    /**
     * Initializes the player with turn manager. This must be called before using turn-related methods.
     *
     * @param mapData the map data for move validation
     */
    public void initialize(final MapData mapData) {
        this.turnManager = createTurnManager(mapData);
    }

    /**
     * Factory method for creating player-specific turn manager. Subclasses override this to provide their specific turn
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

    protected int executeMoveInternal(final int destination, final TransportType transport, final int turnNumber) {
        return getTurnManagerImpl().executeMove(this.currentPosition.getId(), destination, transport, turnNumber);
    }

    // --- MrX- Metodi Specifici (Double Move) ---

    /**
     * Starts a double move sequence with the first move.
     *
     * @param firstDestination the first move destination
     * @param firstTransport the first move transport
     * @param turnNumber the current turn number
     * @throws IllegalStateException if DOUBLE_MOVE ticket not available
     */
    public void startDoubleMove(final int firstDestination, final TransportType firstTransport, final int turnNumber) {
        ensureInitialized();

        // Usa DOUBLE_MOVE
        if (!useTicket(TicketType.DOUBLE_MOVE)) {
            throw new IllegalStateException("Cannot perform double move: no DOUBLE_MOVE tickets available");
        }

        final TicketType ticketType = Player.getTicketTypeForTransport(firstTransport);
        useTicket(ticketType);

        // first move
        final int newPosition = getTurnManagerImpl()
                .startDoubleMove(this.currentPosition.getId(), firstDestination, firstTransport, turnNumber);
        setCurrentPosition(newPosition);
    }

    /**
     * Completes a double move sequence with the second move.
     *
     * @param secondDestination the second move destination
     * @param secondTransport the second move transport
     * @param turnNumber the current turn number
     */
    public void completeDoubleMove(
            final int secondDestination, final TransportType secondTransport, final int turnNumber) {
        ensureInitialized();

        final TicketType ticketType = Player.getTicketTypeForTransport(secondTransport);
        useTicket(ticketType);

        // second move
        final int newPosition = getTurnManagerImpl().completeDoubleMove(secondDestination, secondTransport, turnNumber);
        setCurrentPosition(newPosition);
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
    public Set<TurnManagerImpl.MoveOption> getValidMoves(final Set<Integer> occupiedPositions) {
        ensureInitialized();
        return getTurnManagerImpl().getValidMoves(this.currentPosition.getId(), occupiedPositions);
    }
}
