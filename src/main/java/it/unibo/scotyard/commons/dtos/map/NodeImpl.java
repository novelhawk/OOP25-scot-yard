package it.unibo.scotyard.commons.dtos.map;

import it.unibo.scotyard.model.map.TransportType;
import java.util.Objects;
import java.util.Set;

/** immutable implementation of Node */
public record NodeImpl(int id, int x, int y, Set<TransportType> availableTransports) implements Node {

    // constructor with validation
    public NodeImpl {
        Objects.requireNonNull(availableTransports, "Available transports cannot be null");
        availableTransports = Set.copyOf(availableTransports); // Defensive copy
    }

    @Override
    public int getId() {
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
