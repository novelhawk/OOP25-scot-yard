package it.unibo.scotyard.model;

import java.util.List;

import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.map.MapData;

/** Main model interface for game data management. */
public interface Model {

    /**
     * Initializes the model by loading required data.
     *
     * @throws IllegalStateException if initialization fails
     * @param gameMode the selected game mode
     * @param levelDifficulty the selected level of difficulty
     * @param initialPositions the list of the possible initial positions of players
     */
    void initialize(String gameMode, String levelDifficulty);

    /**
     * Returns the loaded map data.
     *
     * @return the map data
     * @throws IllegalStateException if model not initialized
     */
    MapData getMapData();

    /**
     * Return the data of the game created.
     *
     * @return the game data
     * @throwsIllegalStateException if model not initialized
     */
    Game getGameData();

    /**
     * Return list of initial positions of players, taken from MapData.
     * 
     * @return list of integers representing the possible initial positions of players
     */
    List<Integer> getInitialPositions();
}
