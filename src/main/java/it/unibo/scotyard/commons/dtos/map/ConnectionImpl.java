package it.unibo.scotyard.commons.dtos.map;

import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.util.Objects;

/**
 * An immutable connection.
 *
 */
public record ConnectionImpl(NodeId from, NodeId to, TransportType transport) implements Connection {

    /**
     * Validation and defensive copy.
     */
    public ConnectionImpl {
        Objects.requireNonNull(transport, "Transport type cannot be null");
    }

    @Override
    public NodeId getFrom() {
        return this.from;
    }

    @Override
    public NodeId getTo() {
        return this.to;
    }

    @Override
    public TransportType getTransport() {
        return this.transport;
    }
}
