package it.unibo.scotyard.model;

import it.unibo.scotyard.model.map.MapData;

/**
 * Main model interface for game data management.
 */
public interface Model {

    /**
     * Initializes the model by loading required data.
     * 
     * @throws IllegalStateException if initialization fails
     */
    void initialize();

    /**
     * Returns the loaded map data.
     * 
     * @return the map data
     * @throws IllegalStateException if model not initialized
     */
    MapData getMapData();
}