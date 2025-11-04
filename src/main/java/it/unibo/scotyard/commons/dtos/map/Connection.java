package it.unibo.scotyard.commons.dtos.map;

import java.util.List;

import it.unibo.scotyard.model.map.TransportType;

/**
 * Represents a connection between two nodes.
 */
public interface Connection {

    /**
     * @return source node id
     */
    int getFrom();

    /**
     * @return destination node id
     */
    int getTo();

    /**
     * @return transport type for this connection
     */
    TransportType getTransport();

    /**
     * @return unmodifiable list of intermediate waypoint node ids
     */
    List<Integer> getWaypoints();
}