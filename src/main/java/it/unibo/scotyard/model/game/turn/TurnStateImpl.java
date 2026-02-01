package it.unibo.scotyard.model.game.turn;

import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.map.NodeId;
import java.util.ArrayList;
import java.util.List;

public class TurnStateImpl implements TurnState {
    private List<MoveAction> legalMoves;
    private final List<MoveAction> moves = new ArrayList<>();
    private final List<NodeId> positionHistory;
    private boolean usedDoubleMove;
    private int remainingMoves = 1;

    public TurnStateImpl(final NodeId startingPosition) {
        this.positionHistory = new ArrayList<>();
        this.positionHistory.add(startingPosition);
    }

    @Override
    public void addMove(final MoveAction moveAction) {
        if (remainingMoves <= 0) {
            throw new IllegalStateException("No moves available");
        }

        moves.add(moveAction);
        positionHistory.add(moveAction.destination());
        remainingMoves -= 1;
    }

    @Override
    public void doubleMove() {
        usedDoubleMove = true;
        remainingMoves += 1;
    }

    @Override
    public List<MoveAction> getMoves() {
        return moves;
    }

    @Override
    public boolean hasUsedDoubleMove() {
        return usedDoubleMove;
    }

    @Override
    public int getRemainingMoves() {
        return remainingMoves;
    }

    @Override
    public List<MoveAction> getLegalMoves() {
        return legalMoves;
    }

    @Override
    public void setLegalMoves(final List<MoveAction> legalMoves) {
        this.legalMoves = legalMoves;
    }

    @Override
    public List<NodeId> getPositionHistory() {
        return positionHistory;
    }
}
