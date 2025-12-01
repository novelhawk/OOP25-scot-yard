package it.unibo.scotyard.commons.dtos.map;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.MapNode;
import it.unibo.scotyard.model.map.TransportType;

/**
 * adapts MapData to the DTO interface.
 */
public final class MapInfoImpl implements MapInfo {

    private final MapData mapData;

    /**
     * Creates a MapInfo from MapData.
     * 
     * @param mapData the map data source
     * @throws NullPointerException if mapData is null
     */
    public MapInfoImpl(final MapData mapData) {
        this.mapData = Objects.requireNonNull(mapData, "MapData cannot be null");
    }

    @Override
    public String getName() {
        return this.mapData.getName();
    }

    @Override
    public Stream<Node> getNodes() {
        return this.mapData.getNodes()
                .stream()
                .map(this::createNodeInfo);
    }

    @Override
    public Stream<Connection> getConnections() {
        return this.mapData.getConnections()
                .stream()
                .map(conn -> new ConnectionImpl(
                        conn.getFrom(),
                        conn.getTo(),
                        conn.getTransport()));
    }

    // extract available transports for a node
    private Node createNodeInfo(final MapNode node) {
        final Set<TransportType> transports = this.mapData.getConnections()
                .stream()
                .filter(c -> c.getFrom() == node.getId() || c.getTo() == node.getId())
                .map(c -> c.getTransport())
                .collect(Collectors.toSet());

        return new NodeImpl(node.getId(), node.getX(), node.getY(), transports);
    }
}