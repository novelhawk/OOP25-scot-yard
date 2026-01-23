package it.unibo.scotyard.view.resources;

import it.unibo.scotyard.model.map.TransportType;
import javax.swing.*;

/**
 * Handles loading, caching and parsing of icon resources
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface IconRegistry {
    /**
     * Gets the icon corresponding to the supplied TransportType
     *
     * @param transportType the transport type of the icon
     * @return the icon of the supplied transport type
     */
    ImageIcon getTransportIcon(TransportType transportType);
}
