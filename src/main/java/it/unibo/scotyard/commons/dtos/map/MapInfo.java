package it.unibo.scotyard.commons.dtos.map;

import java.util.stream.Stream;

/**
 * Provides read-only access to map information.
 */
public interface MapInfo {

    /**
     * @return map name
     */
    String getName();

    /**
     * @return stream of all nodes
     */
    Stream<Node> getNodes();

    /**
     * @return stream of all connections
     */
    Stream<Connection> getConnections();
}