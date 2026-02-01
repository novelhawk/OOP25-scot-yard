package it.unibo.scotyard.commons.dtos.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class NodeImplTest {

    @Test
    void testCreateNodeImpl() {
        final NodeId id = new NodeId(1);
        final Set<TransportType> transports = Set.of(TransportType.TAXI, TransportType.BUS);
        final NodeImpl node = new NodeImpl(id, 100, 200, transports);

        assertEquals(id, node.getId());
        assertEquals(100, node.getX());
        assertEquals(200, node.getY());
        assertEquals(2, node.getAvailableTransports().size());
        assertTrue(node.getAvailableTransports().contains(TransportType.TAXI));
        assertTrue(node.getAvailableTransports().contains(TransportType.BUS));
    }

    @Test
    void testNullTransportsThrows() {
        assertThrows(NullPointerException.class, () -> new NodeImpl(new NodeId(1), 100, 200, null));
    }

    @Test
    void testDefensiveCopy() {
        final Set<TransportType> originalSet = new java.util.HashSet<>();
        originalSet.add(TransportType.TAXI);

        final NodeImpl node = new NodeImpl(new NodeId(1), 100, 200, originalSet);
        originalSet.add(TransportType.BUS);

        assertEquals(1, node.getAvailableTransports().size());
        assertTrue(node.getAvailableTransports().contains(TransportType.TAXI));
    }

    @Test
    void testReturnedSetIsImmutable() {
        final NodeImpl node = new NodeImpl(new NodeId(1), 100, 200, Set.of(TransportType.TAXI));

        assertThrows(UnsupportedOperationException.class, () -> node.getAvailableTransports()
                .add(TransportType.BUS));
    }

}
