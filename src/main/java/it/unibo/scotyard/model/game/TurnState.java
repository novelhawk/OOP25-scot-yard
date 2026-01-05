package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.entities.MoveAction;
import java.util.ArrayList;
import java.util.List;

public class TurnState {
    private final List<MoveAction> moves = new ArrayList<>();
    private boolean usedDoubleMove = false;
    private int remainingMoves = 1;

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
}
