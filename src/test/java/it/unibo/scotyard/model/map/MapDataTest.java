package it.unibo.scotyard.model.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapDataTest {

    private MapData mapData;
    private List<MapNode> testNodes;
    private List<MapConnection> testConnections;

    @BeforeEach
    void setup() {
        testNodes = List.of(
                new MapNode(new NodeId(1), 100, 200),
                new MapNode(new NodeId(2), 300, 400),
                new MapNode(new NodeId(3), 500, 600));

        testConnections = List.of(
                new MapConnection(null, new NodeId(1), new NodeId(2), TransportType.TAXI),
                new MapConnection(null, new NodeId(2), new NodeId(1), TransportType.TAXI),
                new MapConnection(null, new NodeId(2), new NodeId(3), TransportType.BUS));

        mapData = new MapData("Test", testNodes, testConnections, List.of(3, 8), List.of(new NodeId(1), new NodeId(2)));
    }

    @Test
    void testConstructorNullValidation() {
        assertThrows(
                NullPointerException.class,
                () -> new MapData(null, testNodes, testConnections, List.of(3), List.of(new NodeId(1))));
        assertThrows(
                NullPointerException.class,
                () -> new MapData("Test", null, testConnections, List.of(3), List.of(new NodeId(1))));
        assertThrows(
                NullPointerException.class,
                () -> new MapData("Test", testNodes, null, List.of(3), List.of(new NodeId(1))));
        assertThrows(
                NullPointerException.class,
                () -> new MapData("Test", testNodes, testConnections, null, List.of(new NodeId(1))));
        assertThrows(
                NullPointerException.class, () -> new MapData("Test", testNodes, testConnections, List.of(3), null));
    }

    @Test
    void testGettersReturnCorrectData() {
        assertEquals("Test", mapData.getName());
        assertEquals(3, mapData.getNodeCount());
        assertEquals(3, mapData.getConnectionCount());
        assertEquals(testNodes, mapData.getNodes());
        assertEquals(testConnections, mapData.getConnections());
        assertTrue(mapData.getRevealTurns().contains(3));
        assertTrue(mapData.getRevealTurns().contains(8));
    }

    @Test
    void testGetNodeById() {
        assertTrue(mapData.getNodeById(new NodeId(1)).isPresent());
        assertTrue(mapData.getNodeById(new NodeId(99)).isEmpty());
        assertEquals(new NodeId(1), mapData.getNodeById(new NodeId(1)).get().getId());
    }

    @Test
    void testGetConnectionsFrom() {
        final List<MapConnection> connsFrom1 = mapData.getConnectionsFrom(new NodeId(1));
        assertEquals(1, connsFrom1.size());
        assertEquals(new NodeId(2), connsFrom1.get(0).getTo());

        final List<MapConnection> taxiConns = mapData.getConnectionsFrom(new NodeId(1), TransportType.TAXI);
        assertEquals(1, taxiConns.size());

        final List<MapConnection> busConns = mapData.getConnectionsFrom(new NodeId(1), TransportType.BUS);
        assertEquals(0, busConns.size());
    }

    @Test
    void testGetNeighbors() {
        assertEquals(1, mapData.getNeighbors(new NodeId(1)).size());
        assertTrue(mapData.getNeighbors(new NodeId(1)).contains(new NodeId(2)));
        assertTrue(mapData.getNeighbors(new NodeId(99)).isEmpty());
    }

    @Test
    void testIsRevealTurn() {
        assertTrue(mapData.isRevealTurn(3));
        assertTrue(mapData.isRevealTurn(8));
        assertFalse(mapData.isRevealTurn(5));
        assertFalse(mapData.isRevealTurn(10));
    }

    @Test
    void testReturnedListsAreImmutable() {
        assertThrows(
                UnsupportedOperationException.class, () -> mapData.getNodes().clear());
        assertThrows(UnsupportedOperationException.class, () -> mapData.getConnections()
                .clear());
        assertThrows(UnsupportedOperationException.class, () -> mapData.getRevealTurns()
                .clear());
    }
}
