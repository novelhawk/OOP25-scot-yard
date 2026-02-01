package it.unibo.scotyard.commons.dtos.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.MapNode;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapInfoImplTest {

    private MapData mapData;
    private MapInfoImpl mapInfo;

    @BeforeEach
    void setup() {
        final List<MapNode> nodes = List.of(new MapNode(new NodeId(1), 100, 200), new MapNode(new NodeId(2), 300, 400));

        final List<MapConnection> connections = List.of(
                new MapConnection(null, new NodeId(1), new NodeId(2), TransportType.TAXI),
                new MapConnection(null, new NodeId(2), new NodeId(1), TransportType.TAXI),
                new MapConnection(null, new NodeId(1), new NodeId(2), TransportType.BUS));

        mapData = new MapData("Test", nodes, connections, List.of(3, 8), List.of(new NodeId(1)));
        mapInfo = new MapInfoImpl(mapData);
    }

    @Test
    void testNullMapDataThrows() {
        assertThrows(NullPointerException.class, () -> new MapInfoImpl(null));
    }

    @Test
    void testGetNodesReturnsStream() {
        final List<Node> nodes = mapInfo.getNodes().toList();

        assertEquals(2, nodes.size());
        assertNotNull(nodes.get(0));
    }

    @Test
    void testGetConnectionsReturnsStream() {
        final List<Connection> connections = mapInfo.getConnections().toList();

        assertEquals(3, connections.size());
        assertNotNull(connections.get(0));
    }

    @Test
    void testNodeConversion() {
        final Node node = mapInfo.getNodes()
                .filter(n -> new NodeId(1).equals(n.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(node);
        assertEquals(new NodeId(1), node.getId());
        assertEquals(100, node.getX());
        assertEquals(200, node.getY());
        assertNotNull(node.getAvailableTransports());
    }

    @Test
    void testNodeIncludesAvailableTransports() {
        final Node node = mapInfo.getNodes()
                .filter(n -> new NodeId(1).equals(n.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(node);
        assertTrue(node.getAvailableTransports().contains(TransportType.TAXI));
        assertTrue(node.getAvailableTransports().contains(TransportType.BUS));
    }
}
