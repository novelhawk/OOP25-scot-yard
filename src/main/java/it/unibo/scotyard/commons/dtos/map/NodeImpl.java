package it.unibo.scotyard.commons.dtos.map;

import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable implementation of Node.
 *
 * @param id the node id
 * @param x the node x coordinate
 * @param y the node y coordinate
 * @param availableTransports the available transports
 */
public record NodeImpl(NodeId id, int x, int y, Set<TransportType> availableTransports) implements Node {

    /**
     * Constructor with validation.
     */
    public NodeImpl {
        Objects.requireNonNull(availableTransports, "Available transports cannot be null");
        availableTransports = Set.copyOf(availableTransports); // Defensive copy
    }

    @Override
    public NodeId getId() {
        return this.id;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public Set<TransportType> getAvailableTransports() {
        return this.availableTransports; // immutable
    }
}
