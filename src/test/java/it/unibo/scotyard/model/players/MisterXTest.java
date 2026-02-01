package it.unibo.scotyard.model.players;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;

import org.junit.jupiter.api.Test;

public class MisterXTest {

    @Test
    void testCreateMisterX() {
        final MisterX mrX = new MisterX(new NodeId(1));

        assertTrue(mrX.isHuman());
        assertEquals(ViewConstants.MRX_STRING, mrX.getName());
        assertEquals(new NodeId(1), mrX.getPosition());
    }

    @Test
    void testInventoryInitialization() {
        final MisterX mrX = new MisterX(new NodeId(1));

        assertEquals(MagicNumbers.INFINITE, mrX.getNumberTickets(TicketType.TAXI));
        assertEquals(MagicNumbers.INFINITE, mrX.getNumberTickets(TicketType.BUS));
        assertEquals(MagicNumbers.INFINITE, mrX.getNumberTickets(TicketType.UNDERGROUND));
        assertEquals(MagicNumbers.NUMBER_TICKETS_BLACK, mrX.getNumberTickets(TicketType.BLACK));
        assertEquals(MagicNumbers.NUMBER_TICKETS_DOUBLE_MOVE, mrX.getNumberTickets(TicketType.DOUBLE_MOVE));
    }

    @Test
    void testInfiniteTicketsUsage() {
        final MisterX mrX = new MisterX(new NodeId(1));

        assertTrue(mrX.useTicket(TicketType.TAXI));
        assertEquals(MagicNumbers.INFINITE, mrX.getNumberTickets(TicketType.TAXI));
    }

    @Test
    void testFiniteTicketsDepletion() {
        final MisterX mrX = new MisterX(new NodeId(1));
        final int initialDoubleMoveTickets = mrX.getNumberTickets(TicketType.DOUBLE_MOVE);

        assertTrue(mrX.useTicket(TicketType.DOUBLE_MOVE));
        assertEquals(initialDoubleMoveTickets - 1, mrX.getNumberTickets(TicketType.DOUBLE_MOVE));

        for (int i = 1; i < MagicNumbers.NUMBER_TICKETS_DOUBLE_MOVE; i++) {
            mrX.useTicket(TicketType.DOUBLE_MOVE);
        }

        assertFalse(mrX.useTicket(TicketType.DOUBLE_MOVE));
        assertEquals(0, mrX.getNumberTickets(TicketType.DOUBLE_MOVE));
    }

    @Test
    void testSetPosition() {
        final MisterX mrX = new MisterX(new NodeId(1));

        mrX.setPosition(new NodeId(5));
        assertEquals(new NodeId(5), mrX.getPosition());
    }

    @Test
    void testUninitializedOperationsThrow() {
        final MisterX mrX = new MisterX(new NodeId(1));

        assertThrows(
                IllegalStateException.class,
                () -> mrX.makeMove(new NodeId(2), TransportType.TAXI, 1));
        assertThrows(
                IllegalStateException.class,
                () -> mrX.startDoubleMove(new NodeId(2), TransportType.TAXI, 1));
        assertThrows(
                IllegalStateException.class,
                () -> mrX.completeDoubleMove(new NodeId(3), TransportType.BUS, 1));
        assertThrows(IllegalStateException.class, mrX::isDoubleMoveAvailable);
        assertThrows(IllegalStateException.class, () -> mrX.getValidMoves(java.util.Set.of()));
    }
}
