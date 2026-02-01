package it.unibo.scotyard.model.game.turn;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnStateImplTest {
    private TurnStateImpl turnState;

    @BeforeEach
    void initialize() {
        turnState = new TurnStateImpl(new NodeId(1));
    }

    @Test
    void addMoveTracksSuccessfully() {
        // Act
        final MoveAction move = new MoveAction(new NodeId(2), TransportType.BUS);
        turnState.addMove(move);

        // Assert
        assertIterableEquals(List.of(new NodeId(1), new NodeId(2)), turnState.getPositionHistory());
        assertIterableEquals(List.of(move), turnState.getMoves());
        assertEquals(0, turnState.getRemainingMoves());
    }

    @Test
    void addTwoMovesWithoutDoubleMoveFails() {
        // Arrange
        turnState.addMove(new MoveAction(new NodeId(2), TransportType.BUS));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            turnState.addMove(new MoveAction(new NodeId(3), TransportType.TAXI));
        });
    }

    @Test
    void addTwoMovesWithDoubleMoveSucceeds() {
        // Arrange
        final MoveAction firstMove = new MoveAction(new NodeId(2), TransportType.BUS);
        final MoveAction secondMove = new MoveAction(new NodeId(3), TransportType.TAXI);
        turnState.addMove(firstMove);

        // Act
        turnState.doubleMove();
        assertDoesNotThrow(() -> turnState.addMove(secondMove));

        // Assert
        assertIterableEquals(List.of(firstMove, secondMove), turnState.getMoves());
        assertIterableEquals(List.of(new NodeId(1), new NodeId(2), new NodeId(3)), turnState.getPositionHistory());
    }
}
