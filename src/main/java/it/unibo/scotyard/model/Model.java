package it.unibo.scotyard.model;

import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.map.MapData;
import java.util.List;

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

    /**
     * Return list of ids of possible destinations, given the id of the starting position.
     * 
     * @param idStartPosition the id of the starting position
     * @return list of possible destinations as ids
     */
    List<Integer> getPossibleDestinations(int idStartPosition);
}
