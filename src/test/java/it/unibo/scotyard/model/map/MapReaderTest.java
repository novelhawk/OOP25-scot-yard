package it.unibo.scotyard.model.map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.scotyard.model.map.MapReader.MapLoadException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapReaderTest {
    private static String MAP_TEST = "/it/unibo/scotyard/map/mapTest.json";
    private static String MAP_INVALID = "/it/unibo/scotyard/map/invalidMap.json";

    private MapReader mapReader;

    @BeforeEach
    void setup() {
        mapReader = new MapReader();
    }

    @Test
    void loadDefaultMapSucceeds() {
        assertDoesNotThrow(() -> {
            final MapData mapData = mapReader.loadDefaultMap();
            assertNotNull(mapData);
            assertTrue(mapData.getNodeCount() > 0, "Default map should have nodes");
            assertTrue(mapData.getConnectionCount() > 0, "Default map should have connections");
        });
    }

    @Test
    void loadTestMapParsesCorrectly() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        assertNotNull(mapData);
        assertEquals("Test Map", mapData.getName());
        assertEquals(4, mapData.getNodeCount(), "Should have 4 nodes");
    }

    @Test
    void loadTestMapParsesNodesCorrectly() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final Optional<MapNode> node1 = mapData.getNodeById(new NodeId(1));
        final Optional<MapNode> node2 = mapData.getNodeById(new NodeId(2));

        assertTrue(node1.isPresent());
        assertTrue(node2.isPresent());
        assertEquals(new NodeId(1), node1.get().getId());
        assertEquals(100, node1.get().getX());
        assertEquals(200, node1.get().getY());
        assertEquals(new NodeId(2), node2.get().getId());
        assertEquals(300, node2.get().getX());
        assertEquals(400, node2.get().getY());
    }

    @Test
    void loadTestMapParsesConnectionsCorrectly() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final List<MapConnection> connectionsFrom1 = mapData.getConnectionsFrom(new NodeId(1));
        final List<MapConnection> connectionsFrom2 = mapData.getConnectionsFrom(new NodeId(2));

        assertFalse(connectionsFrom1.isEmpty(), "Node 1 should have connections");
        assertFalse(connectionsFrom2.isEmpty(), "Node 2 should have connections");

        assertTrue(
                connectionsFrom1.stream()
                        .anyMatch(conn ->
                                new NodeId(2).equals(conn.getTo()) && conn.supportsTransport(TransportType.TAXI)),
                "Should have taxi connection from 1 to 2");

        assertTrue(
                connectionsFrom2.stream()
                        .anyMatch(conn ->
                                new NodeId(1).equals(conn.getTo()) && conn.supportsTransport(TransportType.TAXI)),
                "Should have taxi connection from 2 to 1 (bidirectional)");
    }

    @Test
    void loadTestMapParsesBusConnections() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final List<MapConnection> busConnections = mapData.getConnectionsFrom(new NodeId(1), TransportType.BUS);

        assertFalse(busConnections.isEmpty());
        assertTrue(
                busConnections.stream().anyMatch(conn -> new NodeId(3).equals(conn.getTo())),
                "Should have bus connection from 1 to 3");
    }

    @Test
    void loadTestMapParsesUndergroundConnections() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final List<MapConnection> undergroundConnections =
                mapData.getConnectionsFrom(new NodeId(2), TransportType.UNDERGROUND);

        assertFalse(undergroundConnections.isEmpty());
        assertTrue(
                undergroundConnections.stream().anyMatch(conn -> new NodeId(4).equals(conn.getTo())),
                "Should have underground connection from 2 to 4");
    }

    @Test
    void loadTestMapParsesBlackConnectionsAsFerry() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final List<MapConnection> ferryConnections = mapData.getConnectionsFrom(new NodeId(3), TransportType.FERRY);

        assertFalse(ferryConnections.isEmpty());
        assertTrue(
                ferryConnections.stream().anyMatch(conn -> new NodeId(4).equals(conn.getTo())),
                "Should have ferry (black) connection from 3 to 4");
    }

    @Test
    void loadTestMapParsesRevealTurns() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final List<Integer> revealTurns = mapData.getRevealTurns();
        assertEquals(3, revealTurns.size());
        assertTrue(revealTurns.contains(3));
        assertTrue(revealTurns.contains(8));
        assertTrue(revealTurns.contains(13));
        assertTrue(mapData.isRevealTurn(3));
        assertTrue(mapData.isRevealTurn(8));
        assertFalse(mapData.isRevealTurn(5));
    }

    @Test
    void loadTestMapParsesInitialPositions() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final List<NodeId> initialPositions = mapData.getInitialPositions();
        assertEquals(4, initialPositions.size());
        assertTrue(initialPositions.contains(new NodeId(1)));
        assertTrue(initialPositions.contains(new NodeId(2)));
        assertTrue(initialPositions.contains(new NodeId(3)));
        assertTrue(initialPositions.contains(new NodeId(4)));
    }

    @Test
    void loadMapWithNullPathThrows() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    mapReader.loadMap(null);
                },
                "Null resource path should throw NullPointerException");
    }

    @Test
    void loadNonExistentMapThrows() {
        final MapLoadException exception = assertThrows(MapLoadException.class, () -> {
            mapReader.loadMap("/nonexistent/map.json");
        });

        assertTrue(exception.getMessage().contains("not found"), "Exception should indicate file not found");
    }

    @Test
    void loadInvalidMapThrows() {
        assertThrows(
                MapLoadException.class,
                () -> {
                    mapReader.loadMap(MAP_INVALID);
                },
                "Invalid JSON should throw MapLoadException");
    }

    @Test
    void connectionsBidirectional() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final NodeId node1 = new NodeId(1);
        final NodeId node2 = new NodeId(2);

        final Set<NodeId> neighborsFrom1 = mapData.getNeighbors(node1);
        final Set<NodeId> neighborsFrom2 = mapData.getNeighbors(node2);

        if (neighborsFrom1.contains(node2)) {
            assertTrue(
                    neighborsFrom2.contains(node1),
                    "Connections should be bidirectional: if 1->2 exists, 2->1 should exist");
        }
    }

    @Test
    void mapDataIsImmutable() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);
        final List<MapNode> nodes = mapData.getNodes();

        assertThrows(
                UnsupportedOperationException.class,
                () -> {
                    nodes.clear();
                },
                "Returned nodes list should be immutable");
    }

    @Test
    void multipleTransportTypesOnSameRoute() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        final List<MapConnection> allConnections = mapData.getConnectionsFrom(new NodeId(1));
        final long taxiCount = allConnections.stream()
                .filter(conn -> conn.supportsTransport(TransportType.TAXI))
                .count();
        final long busCount = allConnections.stream()
                .filter(conn -> conn.supportsTransport(TransportType.BUS))
                .count();

        assertTrue(taxiCount > 0, "Should have taxi connections");
        assertTrue(busCount > 0, "Should have bus connections");
    }

    @Test
    void connectionCountMatchesDefinedConnections() throws MapLoadException {
        final MapData mapData = mapReader.loadMap(MAP_TEST);

        assertEquals(10, mapData.getConnectionCount(), "Should have correct number of bidirectional connections");
    }
}
