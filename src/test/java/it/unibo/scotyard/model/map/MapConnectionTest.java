package it.unibo.scotyard.model.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MapConnectionTest {

    @Test
    void testCreateConnectionWithNodeIds() {
        final NodeId from = new NodeId(1);
        final NodeId to = new NodeId(2);
        final MapConnection connection = new MapConnection(from, to, TransportType.TAXI);

        assertEquals(from, connection.getFrom());
        assertEquals(to, connection.getTo());
        assertEquals(TransportType.TAXI, connection.getTransport());
        assertTrue(connection.getId().isEmpty());
    }

    @Test
    void testCreateConnectionWithId() {
        final MapConnection connection = new MapConnection(42, new NodeId(1), new NodeId(2), TransportType.BUS);

        assertTrue(connection.getId().isPresent());
        assertEquals(42, connection.getId().get());
    }

    @Test
    void testNullTransportThrows() {
        assertThrows(NullPointerException.class, () -> new MapConnection(new NodeId(1), new NodeId(2), null));
    }

    @Test
    void testSupportsTransport() {
        final MapConnection connection = new MapConnection(new NodeId(1), new NodeId(2), TransportType.TAXI);

        assertTrue(connection.supportsTransport(TransportType.TAXI));
        assertFalse(connection.supportsTransport(TransportType.BUS));
        assertFalse(connection.supportsTransport(TransportType.UNDERGROUND));
    }

    @Test
    void testConnectionEquality() {
        final NodeId from = new NodeId(1);
        final NodeId to = new NodeId(2);
        final MapConnection conn1 = new MapConnection(from, to, TransportType.TAXI);
        final MapConnection conn2 = new MapConnection(from, to, TransportType.TAXI);
        final MapConnection conn3 = new MapConnection(10, from, to, TransportType.TAXI);

        assertEquals(conn1, conn2);
        assertEquals(conn1, conn3);
        assertEquals(conn1.hashCode(), conn2.hashCode());
    }

    @Test
    void testConnectionInequality() {
        final MapConnection conn1 = new MapConnection(new NodeId(1), new NodeId(2), TransportType.TAXI);
        final MapConnection conn2 = new MapConnection(new NodeId(3), new NodeId(2), TransportType.TAXI);
        final MapConnection conn3 = new MapConnection(new NodeId(1), new NodeId(3), TransportType.TAXI);
        final MapConnection conn4 = new MapConnection(new NodeId(1), new NodeId(2), TransportType.BUS);

        assertNotEquals(conn1, conn2);
        assertNotEquals(conn1, conn3);
        assertNotEquals(conn1, conn4);
    }

    @Test
    void testBidirectionalConnectionsAreDifferent() {
        final MapConnection forward = new MapConnection(new NodeId(1), new NodeId(2), TransportType.TAXI);
        final MapConnection reverse = new MapConnection(new NodeId(2), new NodeId(1), TransportType.TAXI);

        assertNotEquals(forward, reverse);
        assertEquals(forward.getFrom(), reverse.getTo());
        assertEquals(forward.getTo(), reverse.getFrom());
    }

    @Test
    void testToString() {
        final MapConnection connection = new MapConnection(5, new NodeId(1), new NodeId(2), TransportType.BUS);
        final String str = connection.toString();

        assertTrue(str.contains("MapConnection"));
        assertTrue(str.contains("id=5"));
        assertTrue(str.contains("BUS"));
    }
}
