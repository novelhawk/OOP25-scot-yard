package it.unibo.scotyard.commons.dtos.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import org.junit.jupiter.api.Test;

public class ConnectionImplTest {

    @Test
    void testCreateConnectionImpl() {
        final NodeId from = new NodeId(1);
        final NodeId to = new NodeId(2);
        final ConnectionImpl conn = new ConnectionImpl(from, to, TransportType.TAXI);

        assertEquals(from, conn.getFrom());
        assertEquals(to, conn.getTo());
        assertEquals(TransportType.TAXI, conn.getTransport());
    }

    @Test
    void testNullTransportThrows() {
        assertThrows(NullPointerException.class, () -> new ConnectionImpl(new NodeId(1), new NodeId(2), null));
    }

    @Test
    void testAllTransportTypes() {
        final ConnectionImpl taxi = new ConnectionImpl(new NodeId(1), new NodeId(2), TransportType.TAXI);
        final ConnectionImpl bus = new ConnectionImpl(new NodeId(1), new NodeId(2), TransportType.BUS);
        final ConnectionImpl underground = new ConnectionImpl(new NodeId(1), new NodeId(2), TransportType.UNDERGROUND);
        final ConnectionImpl ferry = new ConnectionImpl(new NodeId(1), new NodeId(2), TransportType.FERRY);

        assertEquals(TransportType.TAXI, taxi.getTransport());
        assertEquals(TransportType.BUS, bus.getTransport());
        assertEquals(TransportType.UNDERGROUND, underground.getTransport());
        assertEquals(TransportType.FERRY, ferry.getTransport());
    }

    @Test
    void testBidirectionalConnectionsAreDifferent() {
        final ConnectionImpl forward = new ConnectionImpl(new NodeId(1), new NodeId(2), TransportType.TAXI);
        final ConnectionImpl reverse = new ConnectionImpl(new NodeId(2), new NodeId(1), TransportType.TAXI);

        assertNotEquals(forward, reverse);
    }
}
