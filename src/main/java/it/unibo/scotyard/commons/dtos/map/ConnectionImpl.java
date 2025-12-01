package it.unibo.scotyard.commons.dtos.map;

import java.util.Objects;

import it.unibo.scotyard.model.map.TransportType;

/**
 * Immutable implementation
 */
public record ConnectionImpl(
        int from,
        int to,
        TransportType transport) implements Connection {

    // validation and defensive copy
    public ConnectionImpl {
        Objects.requireNonNull(transport, "Transport type cannot be null");
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
}