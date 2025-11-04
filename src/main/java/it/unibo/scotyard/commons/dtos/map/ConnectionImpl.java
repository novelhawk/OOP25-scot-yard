package it.unibo.scotyard.commons.dtos.map;

import java.util.List;
import java.util.Objects;

import it.unibo.scotyard.model.map.TransportType;

/**
 * Immutable implementation
 */
public record ConnectionImpl(
        int from,
        int to,
        TransportType transport,
        List<Integer> waypoints) implements Connection {

    // validation and defensive copy
    public ConnectionImpl {
        Objects.requireNonNull(transport, "Transport type cannot be null");
        waypoints = waypoints != null ? List.copyOf(waypoints) : List.of();
    }

    /**
     * constructor without waypoints.
     */
    public ConnectionImpl(final int from, final int to, final TransportType transport) {
        this(from, to, transport, List.of());
    }

    @Override
    public int getFrom() {
        return this.from;
    }

    @Override
    public int getTo() {
        return this.to;
    }

    @Override
    public TransportType getTransport() {
        return this.transport;
    }

    @Override
    public List<Integer> getWaypoints() {
        return this.waypoints; // already immutable
    }
}