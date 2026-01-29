package it.unibo.scotyard.model.game.turn;

import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Set;

/**
 * This interface defines the contract for managing player turns, including move
 * validation and execution.
 *
 * @param <M> the type of move option this turn manager handles
 */
public interface TurnManager<M extends TurnManager.MoveOption> {

    /**
     * Calculates all valid moves from the current position. A move is valid if
     * there is a connection to the destination
     * node via the specified transport.
     *
     * @param currentPosition   the current node position
     * @param occupiedPositions the set of node IDs currently occupied by other
     *                          players
     * @return a set of valid move options
     * @throws IllegalArgumentException if currentPosition is invalid
     */
    Set<M> getValidMoves(NodeId currentPosition, Set<NodeId> occupiedPositions);

    /**
     * Checks if a specific move is valid. A move is valid if there exists a
     * connection from 'from' to 'to' using the
     * specified transport type.
     *
     * @param from      the starting node ID
     * @param to        the destination node ID
     * @param transport the transport type to use
     * @return true if the move is valid, false otherwise
     * @throws NullPointerException if transport is null
     */
    boolean isValidMove(NodeId from, NodeId to, TransportType transport);

    /**
     * Executes a move from current position to destination using the specified
     * transport. This method validates the
     * move, returns the new position.
     *
     * @param currentPosition the current position
     * @param destination     the destination node ID
     * @param transport       the transport type to use
     * @param turnNumber      the current turn number
     * @return the new position after the move (should equal destination)
     * @throws IllegalArgumentException if the move is invalid
     * @throws NullPointerException     if transport is null
     */
    NodeId executeMove(NodeId currentPosition, NodeId destination, TransportType transport, int turnNumber);

    /**
     * Represents a valid move option in the game. A move consists of a destination
     * node and the transport type used to
     * reach it.
     */
    interface MoveOption {
        /**
         * Gets the destination node ID for this move.
         *
         * @return the node ID
         */
        NodeId getDestinationNode();

        /**
         * Gets the transport type used for this move.
         *
         * @return the transport type
         */
        TransportType getTransport();
    }
}
