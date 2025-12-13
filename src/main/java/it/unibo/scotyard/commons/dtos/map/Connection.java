package it.unibo.scotyard.commons.dtos.map;

import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;

/** Represents a connection between two nodes. */
public interface Connection {

    /** @return source node id */
    NodeId getFrom();

    /** @return destination node id */
    NodeId getTo();

    /** @return transport type for this connection */
    TransportType getTransport();
}
