package it.unibo.scotyard.model.map;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.dtos.map.MapInfoImpl;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * complete map data including nodes, connections, and game configuration. This class provides a rich query API for
 * accessing and navigating the map structure. All collections are immutable and defensively copied.
 */
public final class MapData {

    private final String name;
    private final List<MapNode> nodes;
    private final List<MapConnection> connections;
    private final List<Integer> revealTurns;
    private final List<Integer> initialPositions;

    /**
     * Creates a new MapData with the specified configuration. All collection parameters are defensively copied to
     * ensure immutability.
     *
     * @param name the name of the map
     * @param nodes the list of nodes on the map
     * @param connections the list of connections between nodes
     * @param revealTurns the turns on which Mr. X's position is revealed
     * @param initialPositions the pool of possible starting positions for all players
     * @throws NullPointerException if any parameter is null
     */
    public MapData(
            final String name,
            final List<MapNode> nodes,
            final List<MapConnection> connections,
            final List<Integer> revealTurns,
            final List<Integer> initialPositions) {
        this.name = Objects.requireNonNull(name, "Map name non può essere null");
        this.nodes = List.copyOf(Objects.requireNonNull(nodes, "Nodes non può essere null"));
        this.connections = List.copyOf(Objects.requireNonNull(connections, "Connections non può essere null"));
        this.revealTurns = List.copyOf(Objects.requireNonNull(revealTurns, "Reveal turns non può essere null"));
        this.initialPositions =
                List.copyOf(Objects.requireNonNull(initialPositions, "Initial positions non può essere null"));
    }

    /**
     * Returns the name of this map.
     *
     * @return the map name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable list of all nodes on this map.
     *
     * @return the list of nodes
     */
    public List<MapNode> getNodes() {
        return nodes;
    }

    /**
     * Returns an unmodifiable list of all connections on this map.
     *
     * @return the list of connections
     */
    public List<MapConnection> getConnections() {
        return connections;
    }

    /**
     * Returns the turns on which Mr. X's position is revealed.
     *
     * @return an unmodifiable list of reveal turn numbers
     */
    public List<Integer> getRevealTurns() {
        return revealTurns;
    }

    /**
     * Returns possible starting positions for all players. Players should randomly select positions from this list
     * without replacement.
     *
     * @return an unmodifiable list of node IDs
     */
    public List<Integer> getInitialPositions() {
        return initialPositions;
    }

    /**
     * Finds a node by its unique identifier.
     *
     * @param nodeId the node ID to search for
     * @return an Optional containing the node if found, empty otherwise
     */
    public Optional<MapNode> getNodeById(final NodeId nodeId) {
        return nodes.stream().filter(node -> node.getId() == nodeId).findFirst();
    }

    /**
     * Returns all connections originating from the specified node.
     *
     * @param nodeId the starting node ID
     * @return a list of connections from this node
     */
    public List<MapConnection> getConnectionsFrom(final NodeId nodeId) {
        return connections.stream().filter(conn -> conn.getFrom() == nodeId).collect(Collectors.toList());
    }

    /**
     * Returns all connections from the specified node using the specified transport type.
     *
     * @param nodeId the starting node ID
     * @param transport the transport type to filter by
     * @return a list of connections from this node with the specified transport
     */
    public List<MapConnection> getConnectionsFrom(final NodeId nodeId, final TransportType transport) {
        return connections.stream()
                .filter(conn -> conn.getFrom() == nodeId && conn.supportsTransport(transport))
                .collect(Collectors.toList());
    }

    /**
     * Returns the IDs of all nodes reachable from the specified node.
     *
     * @param nodeId the starting node ID
     * @return a set of neighboring node IDs
     */
    public Set<NodeId> getNeighbors(final NodeId nodeId) {
        return connections.stream()
                .filter(conn -> conn.getFrom() == nodeId)
                .map(MapConnection::getTo)
                .collect(Collectors.toSet());
    }

    /**
     * Checks if the specified turn is a reveal turn for Mr. X.
     *
     * @param turn the turn number to check
     * @return true if Mr. X's position is revealed on this turn
     */
    public boolean isRevealTurn(final int turn) {
        return revealTurns.contains(turn);
    }

    /**
     * Returns the total number of nodes on this map.
     *
     * @return the node count
     */
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * Returns the total number of connections on this map.
     *
     * @return the connection count
     */
    public int getConnectionCount() {
        return connections.size();
    }

    /**
     * Converts this MapData to a MapInfo DTO for use in the view layer.
     *
     * @return a MapInfo DTO wrapping this map data
     */
    public MapInfo info() {
        return new MapInfoImpl(this);
    }

    @Override
    public String toString() {
        return "MapData{"
                + "name='" + name + '\''
                + ", nodes=" + nodes.size()
                + ", connections=" + connections.size()
                + ", revealTurns=" + revealTurns
                + '}';
    }
}
