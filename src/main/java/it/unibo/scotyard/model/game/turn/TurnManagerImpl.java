package it.unibo.scotyard.model.game.turn;

import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * implementation of TurnManager. - Standard move validation - Double move sequence management (for Mr. X)
 *
 * @implNote This class maintains state for double move sequences. It can be used by all players for basic move
 *     validation.
 */
public final class TurnManagerImpl implements TurnManager<TurnManagerImpl.MoveOption> {

    /** Represents a valid move option. Immutable value object combining destination and transport type. */
    public static final class MoveOption implements TurnManager.MoveOption {
        private final NodeId destinationNode;
        private final TransportType transport;

        /**
         * Creates a new move option.
         *
         * @param destinationNode the destination node ID
         * @param transport the transport type
         * @throws NullPointerException if transport is null
         */
        public MoveOption(final NodeId destinationNode, final TransportType transport) {
            this.destinationNode = destinationNode;
            this.transport = Objects.requireNonNull(transport, "Transport cannot be null");
        }

        @Override
        public NodeId getDestinationNode() {
            return destinationNode;
        }

        @Override
        public TransportType getTransport() {
            return transport;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MoveOption)) {
                return false;
            }
            final MoveOption that = (MoveOption) o;
            return destinationNode == that.destinationNode && transport == that.transport;
        }

        @Override
        public int hashCode() {
            return Objects.hash(destinationNode, transport);
        }

        @Override
        public String toString() {
            return "Vai al nodo " + destinationNode + " utilizzando " + transport;
        }
    }

    private final MapData mapData;

    // Double move state
    private boolean doubleMoveAvailable;
    private boolean isFirstMoveOfDouble;
    private NodeId firstMoveDestination;

    /**
     * Creates a new turn manager.
     *
     * @param mapData the map data for move validation
     * @throws NullPointerException if parameter is null
     */
    public TurnManagerImpl(final MapData mapData) {
        this.mapData = Objects.requireNonNull(mapData, "MapData cannot be null");
        this.doubleMoveAvailable = true;
        this.isFirstMoveOfDouble = false;
    }

    @Override
    public Set<MoveOption> getValidMoves(final NodeId currentPosition, final Set<NodeId> occupiedPositions) {
        return mapData.getConnectionsFrom(currentPosition).stream()
                .filter(conn -> !occupiedPositions.contains(conn.getTo()))
                .map(conn -> new MoveOption(conn.getTo(), conn.getTransport()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValidMove(final NodeId from, final NodeId to, final TransportType transport) {
        final List<MapConnection> connections = mapData.getConnectionsFrom(from, transport);

        return connections.stream().anyMatch(conn -> conn.getTo() == to);
    }

    @Override
    public NodeId executeMove(
            final NodeId currentPosition,
            final NodeId destination,
            final TransportType transport,
            final int turnNumber) {
        validateMove(currentPosition, destination, transport);
        return destination;
    }

    /**
     * Starts a double move sequence.
     *
     * @param currentPosition the current position
     * @param firstDestination the first move destination
     * @param firstTransport the first move transport
     * @param turnNumber the current turn number
     * @return the new position after first move
     * @throws IllegalStateException if double move not available
     * @throws IllegalArgumentException if the move is invalid
     */
    public NodeId startDoubleMove(
            final NodeId currentPosition,
            final NodeId firstDestination,
            final TransportType firstTransport,
            final int turnNumber) {
        if (!doubleMoveAvailable) {
            throw new IllegalStateException("Double move non disponibile");
        }

        validateMove(currentPosition, firstDestination, firstTransport);

        isFirstMoveOfDouble = true;
        firstMoveDestination = firstDestination;

        return firstDestination;
    }

    /**
     * Completes a double move sequence.
     *
     * @param secondDestination the second move destination
     * @param secondTransport the second move transport
     * @param turnNumber the current turn number
     * @return the new position after second move
     * @throws IllegalStateException if not in double move sequence
     * @throws IllegalArgumentException if the move is invalid
     */
    public NodeId completeDoubleMove(
            final NodeId secondDestination, final TransportType secondTransport, final int turnNumber) {
        if (!isFirstMoveOfDouble) {
            throw new IllegalStateException("Non nella giusta sequenza");
        }

        validateMove(firstMoveDestination, secondDestination, secondTransport);

        isFirstMoveOfDouble = false;
        doubleMoveAvailable = false;

        return secondDestination;
    }

    /**
     * Checks if double move is available and not currently in progress.
     *
     * @return true if double move can be initiated
     */
    public boolean isDoubleMoveAvailable() {
        return doubleMoveAvailable && !isFirstMoveOfDouble;
    }

    /**
     * Checks if currently in a double move sequence.
     *
     * @return true if waiting for second move
     */
    public boolean isInDoubleMoveSequence() {
        return isFirstMoveOfDouble;
    }

    /** Resets the double move availability. This should be called at the start of a new game. */
    public void resetDoubleMove() {
        doubleMoveAvailable = true;
        isFirstMoveOfDouble = false;
    }

    /**
     * Validates a move or throws an exception.
     *
     * @param from the starting position
     * @param to the destination
     * @param transport the transport type
     * @throws IllegalArgumentException if the move is invalid
     */
    private void validateMove(final NodeId from, final NodeId to, final TransportType transport) {
        if (!isValidMove(from, to, transport)) {
            throw new IllegalArgumentException(
                    "Mossa non valida: nessuna connessione via " + transport + " da " + from + " verso " + to);
        }
    }
}
