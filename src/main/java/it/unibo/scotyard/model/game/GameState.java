package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.entities.RunnerTurnTracker;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The game state.
 *
 */
public interface GameState {

    /**
     * Returns a boolean that indicates if the game is over. In particular, the game is over if the detective or one of
     * the bobbies has captured Mister X (they're in the same position of the map) or if the maximum number of rounds
     * had been reached.
     *
     * @return a boolean which indicates whether the game is over (true) or not
     */
    boolean isGameOver();

    /**
     * Gets the Random instance shared for the current game session.
     *
     * @return the seeded shared random instance used by all game logic
     */
    Random getSeededRandom();

    /**
     * This method gets called when the game is over, to get the result : the user player has won or not.
     *
     * @return String, which indicates whether the user player has won or not
     */
    String resultGame();

    /**
     * Loads into a specific variable the possible destinations, eventually removing some of them, according to specific
     * conditions.
     *
     * @param inputPossibleDestinations the possible destinations loaded from Model
     * @return the updated set of possible destinations
     */
    Set<Pair<NodeId, TransportType>> loadPossibleDestinations(
            Set<Pair<NodeId, TransportType>> inputPossibleDestinations);

    /**
     * @return the set of possible destinations as pairs of integer and transport type
     */
    Set<Pair<NodeId, TransportType>> getPossibleDestinations();

    /** Manages the current player. */
    void changeCurrentPlayer();

    /**
     * Return a boolean value which indicates whether there are multiple transports available for the destination id
     * given or not.
     *
     * @param destinationId the id of the destination
     * @return a boolean value which indicates whether there are multiple transports available for the destination given
     *     or not
     */
    boolean areMultipleTransportsAvailable(NodeId destinationId);

    /**
     * Return a list of the transport types that can be used to reach the destination given.
     *
     * @param destinationId the id of the destination
     * @return a list of transport types that can be used to reach destination
     */
    List<TransportType> getAvailableTransports(NodeId destinationId);

    /**
     * Return a boolean value which indicates whether the current player can be moved or not; if it's possible, their
     * position id gets changed and their tickets decrement (according to the type of transport used).
     *
     * @param destinationId the destination id
     * @param transport the transport type to use to reach the destination
     * @return a boolean value which indicates whether the current player can be moved or not
     */
    boolean moveCurrentPlayer(NodeId destinationId, TransportType transport);

    /** Goes to next round by incrementing the round number, if the current player is the last bobby. */
    void nextRound();

    /**
     * Return a boolean inidicating if Mister X must be hidden on the map. In particular, Mister X must be hidden if the
     * game mode is Detective and if the current game round number correspond to one of the reveal turns for Mister X
     * (present in the class Constants in folder commons).
     *
     * @return a boolean indicating whether Mister X must be hidden or not on the map
     */
    boolean hideMisterX();

    /**
     * Return the current game mode.
     *
     * @return the game mode
     */
    GameMode getGameMode();

    /**
     * Return the number of tickets of a specific type possessed by the user player.
     *
     * @param ticketType the type of ticket
     * @return the number of tickets of type {@code ticketType}
     */
    int getNumberTicketsUserPlayer(TicketType ticketType);

    /** Return the current player. */
    Player getCurrentPlayer();

    /**
     * Return the current position of the player passed as input.
     *
     * @return the current position of the player passed as input
     */
    NodeId getPositionPlayer(Player player);

    /**
     * Return user player.
     *
     * @return user player
     */
    Player getUserPlayer();

    /**
     * Return computer player (controlled by IA).
     *
     * @return computer player
     */
    Player getComputerPlayer();

    /** Return the number of players */
    int getNumberOfPlayers();

    /**
     * Returns the current round number.
     *
     * @return the integer which represents the game round
     */
    int getGameRound();

    /**
     * Gets the current game status.
     */
    GameStatus getGameStatus();

    /**
     * Sets the current game status.
     *
     * @param state the updated game status
     */
    void setGameStatus(GameStatus state);

    /**
     * Gets all Bobby players (additional detectives).
     *
     * @return list of Bobby players (can be empty)
     */
    List<Bobby> getBobbies();

    /**
     * Gets the Detective player (computer or user depending on mode).
     *
     * @return the Detective player
     */
    Player getDetective();

    /**
     * Resets the turn state to the start of the turn.
     */
    void resetTurn();

    /**
     * Gets the current turn's state, which will be merged with the game state at the end of the turn.
     *
     * @return the current turn's state
     */
    TurnState getTurnState();

    /**
     * Gets the runner turn tracker, which tracks the tickets used by MisterX during the game.
     *
     * @return the runner turn tracker
     */
    RunnerTurnTracker getRunnerTurnTracker();
}
