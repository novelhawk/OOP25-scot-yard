package it.unibo.scotyard.commons.dtos.map;

import it.unibo.scotyard.model.map.TransportType;
import java.util.Set;

/** Represents a map node with position and available transports. */
public interface Node {

    /** @return unique node identifier */
    int getId();

    /** @return x coordinate */
    int getX();

    /** @return y coordinate */
    int getY();

    /** @return unmodifiable set of available transport types */
    Set<TransportType> getAvailableTransports();
}
