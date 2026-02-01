package it.unibo.scotyard.model.game.turn;

import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.map.NodeId;
import java.util.List;

/**
 * The state of the current turn.
 */
public interface TurnState {

    /**
     * Adds a new move made during the turn.
     *
     * @param moveAction the move made
     */
    void addMove(MoveAction moveAction);

    /**
     * Add a remaining move count and enable double move mode.
     */
    void doubleMove();

    /**
     * Gets the moves made during this turn.
     *
     * @return the moves made during this turn.
     */
    List<MoveAction> getMoves();

    /**
     * Gets whether the player has used the double move this turn.
     *
     * @return whether the player ha used the double move this turn.
     */
    boolean hasUsedDoubleMove();

    /**
     * Gets the number of remaining moves available.
     *
     * @return the number of remaining moves available
     */
    int getRemainingMoves();

    /**
     * Gets the list of legal moves available currently to the current player.
     *
     * @return the list of legal moves available currently to the player
     */
    List<MoveAction> getLegalMoves();

    /**
     * Sets the list of legal moves available to the current player.
     *
     * @param legalMoves the list of legal moves available to the current player
     */
    void setLegalMoves(List<MoveAction> legalMoves);

    /**
     * Gets the previous positions of the player during the current turn,
     * including the position at the start of the round
     *
     * @return the previous position of the player during the current turn.
     */
    List<NodeId> getPositionHistory();
}
