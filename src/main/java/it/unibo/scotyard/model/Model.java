package it.unibo.scotyard.model;

import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.router.CommandDispatcher;
import java.util.List;
import java.util.Random;

/** Main model interface for game data management. */
public interface Model {

    /**
     * Initializes the model by loading required data.
     *
     * @param gameMode        the selected game mode
     * @param levelDifficulty the selected level of difficulty
     * @throws IllegalStateException if initialization fails
     */
    void initialize(String gameMode, String levelDifficulty);

    /**
     * @return the seeded shared random instance used by all game logic
     */
    Random getSeededRandom();

    /**
     * Returns the loaded map data.
     *
     * @return the map data
     * @throws IllegalStateException if model not initialized
     */
    MapData getMapData();

    /**
     * Sets the active game state.
     */
    void setGameState(GameState gameState);

    /**
     * Return the data of the game created.
     *
     * @return the game data
     * @throws IllegalStateException if model not initialized
     */
    GameState getGameState();

    /**
     * Return list of initial positions of players, taken from MapData.
     *
     * @return list of integers representing the possible initial positions of
     *         players
     */
    List<NodeId> getInitialPositions();

    /**
     * Return list of pairs of integer (representing the id) and TransportType of
     * possible destinations, given the id of
     * the starting position.
     *
     * @param idStartPosition the id of the starting position
     * @return list of possible destinations as pairs of integer and TransportType
     */
    List<Pair<NodeId, TransportType>> getPossibleDestinations(NodeId idStartPosition);

    /**
     * Get the global command dispatcher.
     *
     * @return the global command dispatcher
     */
    CommandDispatcher getDispatcher();
}
