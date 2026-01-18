package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.map.NodeId;
import java.util.ArrayList;
import java.util.List;

public class TurnState {
    private List<MoveAction> legalMoves;
    private final List<MoveAction> moves = new ArrayList<>();
    private final List<NodeId> positionHistory;
    private boolean usedDoubleMove = false;
    private int remainingMoves = 1;

    public TurnState(NodeId startingPosition) {
        this.positionHistory = new ArrayList<>();
        this.positionHistory.add(startingPosition);
    }

    public void addMove(MoveAction moveAction) {
        moves.add(moveAction);
        remainingMoves -= 1;
    }

    public void doubleMove() {
        usedDoubleMove = true;
        remainingMoves += 1;
    }

    public List<MoveAction> getMoves() {
        return moves;
    }

    public boolean isUsedDoubleMove() {
        return usedDoubleMove;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public List<MoveAction> getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves(List<MoveAction> legalMoves) {
        this.legalMoves = legalMoves;
    }

    public List<NodeId> getPositionHistory() {
        return positionHistory;
    }
}
