package it.unibo.scotyard.model.players;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.MapReader;
import it.unibo.scotyard.model.map.MapReader.MapLoadException;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MisterXMovesTest {

    private MisterX mrX;
    private MapData mapData;

    @BeforeEach
    void setup() throws MapLoadException {
        // Load test map
        final MapReader mapReader = new MapReader();
        mapData = mapReader.loadMap("/it/unibo/scotyard/map/mapTest.json");

        // Create Mr. X at node 1
        mrX = new MisterX(new NodeId(1));
        mrX.initialize(mapData);
    }

    @Test
    void makeMoveChangesPosition() {

        final NodeId startPosition = new NodeId(1);
        final NodeId destination = new NodeId(2);
        assertEquals(startPosition, mrX.getPosition());

        assertDoesNotThrow(() -> {
            mrX.makeMove(destination, TransportType.TAXI, 1);
        });

        assertEquals(destination, mrX.getPosition(), "Position should change after move");
        assertNotEquals(startPosition, mrX.getPosition());
    }

    @Test
    void makeMoveUsesTicket() {
        final int initialTaxiTickets = mrX.getNumberTickets(TicketType.TAXI);
        final NodeId destination = new NodeId(2);

        assertDoesNotThrow(() -> {
            mrX.makeMove(destination, TransportType.TAXI, 1);
        });

        // Mr. X has infinite standard tickets, so they remain the same
        assertEquals(initialTaxiTickets, mrX.getNumberTickets(TicketType.TAXI), "Mr. X has infinite taxi tickets");
    }

    @Test
    void makeMoveWithBlackTicketDecrementsTicket() {

        mrX.makeMove(new NodeId(3), TransportType.BUS, 1);
        final int initialBlackTickets = mrX.getNumberTickets(TicketType.BLACK);

        assertDoesNotThrow(() -> {
            mrX.makeMove(new NodeId(4), TransportType.FERRY, 2);
        });

        assertEquals(
                initialBlackTickets - 1, mrX.getNumberTickets(TicketType.BLACK), "Black ticket should be decremented");
    }

    @Test
    void startDoubleMoveUsesDoubleMoveTicket() {
        final int initialDoubleMove = mrX.getNumberTickets(TicketType.DOUBLE_MOVE);
        final NodeId firstDestination = new NodeId(2);

        assertDoesNotThrow(() -> {
            mrX.startDoubleMove(firstDestination, TransportType.TAXI, 1);
        });

        assertEquals(
                initialDoubleMove - 1,
                mrX.getNumberTickets(TicketType.DOUBLE_MOVE),
                "Double move ticket should be used");
    }

    @Test
    void startDoubleMoveChangesPosition() {
        final NodeId startPosition = mrX.getPosition();
        final NodeId firstDestination = new NodeId(2);

        assertDoesNotThrow(() -> {
            mrX.startDoubleMove(firstDestination, TransportType.TAXI, 1);
        });

        assertEquals(firstDestination, mrX.getPosition(), "Position should change after first move of double move");
        assertNotEquals(startPosition, mrX.getPosition());
    }

    @Test
    void completeDoubleMoveChangesPositionAgain() {
        final NodeId firstDestination = new NodeId(2);
        final NodeId secondDestination = new NodeId(3);

        mrX.startDoubleMove(firstDestination, TransportType.TAXI, 1);
        assertEquals(firstDestination, mrX.getPosition());

        assertDoesNotThrow(() -> {
            mrX.completeDoubleMove(secondDestination, TransportType.TAXI, 1);
        });

        assertEquals(secondDestination, mrX.getPosition(), "Position should change after second move of double move");
    }

    @Test
    void completeDoubleMoveUsesTransportTicket() {
        final int initialBusTickets = mrX.getNumberTickets(TicketType.BUS);
        final NodeId firstDestination = new NodeId(3);
        final NodeId secondDestination = new NodeId(2);

        mrX.startDoubleMove(firstDestination, TransportType.BUS, 1);

        assertDoesNotThrow(() -> {
            mrX.completeDoubleMove(secondDestination, TransportType.TAXI, 1);
        });

        assertEquals(initialBusTickets, mrX.getNumberTickets(TicketType.BUS), "Mr. X has infinite bus tickets");
    }

    @Test
    void isDoubleMoveAvailableReturnsTrueWhenAvailable() {
        assertTrue(mrX.isDoubleMoveAvailable(), "Double move should be available initially");
    }

    @Test
    void isDoubleMoveAvailableReturnsFalseAfterUse() {
        final int doubleMoveCount = mrX.getNumberTickets(TicketType.DOUBLE_MOVE);

        for (int i = 0; i < doubleMoveCount; i++) {
            mrX.useTicket(TicketType.DOUBLE_MOVE);
        }

        assertFalse(mrX.isDoubleMoveAvailable(), "Double move should not be available when no tickets left");
    }

    @Test
    void startDoubleMoveThrowsWhenNoTickets() {
        final int doubleMoveCount = mrX.getNumberTickets(TicketType.DOUBLE_MOVE);

        for (int i = 0; i < doubleMoveCount; i++) {
            mrX.useTicket(TicketType.DOUBLE_MOVE);
        }

        assertThrows(
                IllegalStateException.class,
                () -> {
                    mrX.startDoubleMove(new NodeId(2), TransportType.TAXI, 1);
                },
                "Should throw when no double move tickets available");
    }

    @Test
    void getValidMovesReturnsNonEmptySet() {
        final Set<NodeId> occupiedPositions = Set.of();

        final var validMoves = mrX.getValidMoves(occupiedPositions);

        assertFalse(validMoves.isEmpty(), "Mr. X at node 1 should have valid moves");
    }

    @Test
    void getValidMovesExcludesOccupiedPositions() {
        final Set<NodeId> occupiedPositions = Set.of(new NodeId(2));

        final var validMoves = mrX.getValidMoves(occupiedPositions);

        assertFalse(
                validMoves.stream().anyMatch(move -> new NodeId(2).equals(move.getDestinationNode())),
                "Valid moves should not include occupied positions");
    }

    @Test
    void getValidMovesIncludesMultipleTransportOptions() {
        final Set<NodeId> occupiedPositions = Set.of();

        final var validMoves = mrX.getValidMoves(occupiedPositions);
        final long movesToNode3 = validMoves.stream()
                .filter(move -> new NodeId(3).equals(move.getDestinationNode()))
                .count();

        assertTrue(movesToNode3 >= 1, "Should have at least one way to reach node 3");
    }

    @Test
    void multipleMovesWorkSequentially() {
        assertEquals(new NodeId(1), mrX.getPosition());

        mrX.makeMove(new NodeId(2), TransportType.TAXI, 1);
        assertEquals(new NodeId(2), mrX.getPosition());

        mrX.makeMove(new NodeId(1), TransportType.TAXI, 2);
        assertEquals(new NodeId(1), mrX.getPosition());

        mrX.makeMove(new NodeId(3), TransportType.BUS, 3);

        assertEquals(new NodeId(3), mrX.getPosition(), "Should be able to make multiple sequential moves");
    }

    @Test
    void doubleMoveSequenceWorks() {
        final NodeId start = new NodeId(1);
        final NodeId first = new NodeId(2);
        final NodeId second = new NodeId(3);

        assertEquals(start, mrX.getPosition());

        mrX.startDoubleMove(first, TransportType.TAXI, 1);
        assertEquals(first, mrX.getPosition());

        mrX.completeDoubleMove(second, TransportType.TAXI, 1);

        assertEquals(second, mrX.getPosition(), "Double move sequence should move from 1->2->3");
    }
}
