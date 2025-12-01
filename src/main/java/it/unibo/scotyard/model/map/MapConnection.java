package it.unibo.scotyard.model.map;

import java.util.Objects;
import java.util.Optional;

/**
 * connection between two nodes on the game map.
 * each connection has a transport type.
 */
public class MapConnection {
    private final Integer id;
    private final int from;
    private final int to;
    private final TransportType transport;

    /**
     * Creates a connection without an ID.
     *
     * @param from      the starting node ID
     * @param to        the destination node ID
     * @param transport the transport type for this connection
     * @throws NullPointerException if transport is null
     */
    public MapConnection(final int from, final int to, final TransportType transport) {
        this(null, from, to, transport);
    }

    /**
     * Creates a connection with full details.
     *
     * @param id        the optional connection ID
     * @param from      the starting node ID
     * @param to        the destination node ID
     * @param transport the transport type for this connection
     * @throws NullPointerException if transport is null
     */
    public MapConnection(final Integer id, final int from, final int to, final TransportType transport) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.transport = Objects.requireNonNull(transport, "Transport type cannot be null");
    }

    /**
     * Returns the optional connection ID.
     *
     * @return an Optional containing the ID if present, empty otherwise
     */
    public Optional<Integer> getId() {
        return Optional.ofNullable(id);
    }

    /**
     * Returns the starting node ID.
     *
     * @return the from node ID
     */
    public int getFrom() {
        return from;
    }

    /**
     * returns the destination node ID.
     *
     * @return the to node ID
     */
    public int getTo() {
        return to;
    }

    /**
     * Returns the transport type for this connection.
     *
     * @return the transport type
     */
    public TransportType getTransport() {
        return transport;
    }

    /**
     * Checks if this connection supports the given transport type.
     *
     * @param transportType the transport type to check
     * @return true if this connection supports the given transport type
     */
    public boolean supportsTransport(final TransportType transportType) {
        return this.transport == transportType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MapConnection that = (MapConnection) o;
        return from == that.from && to == that.to && transport == that.transport;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, transport);
    }

    @Override
    public String toString() {
        return "MapConnection{id=" + id + ", from=" + from + ", to=" + to + ", transport=" + transport + '}';
    }
}
