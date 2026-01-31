package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.entities.RunnerTurnTracker;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The game state.
 *
 */
public interface GameState {

    /**
     * Returns a boolean that indicates if the game is over. In particular, the game
     * is over if the detective or one of
     * the bobbies has captured Mister X (they're in the same position of the map)
     * or if the maximum number of rounds
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
     * This method gets called when the game is over, to get the result : the user
     * player has won or not.
     *
     * @return String, which indicates whether the user player has won or not
     */
    String resultGame();

    /**
     * Loads into a specific variable the possible destinations, eventually removing
     * some of them, according to specific
     * conditions.
     *
     * @param inputPossibleDestinations the possible destinations loaded from Model
     * @return the updated set of possible destinations
     */
    Set<Pair<NodeId, TransportType>> loadPossibleDestinations(
            Set<Pair<NodeId, TransportType>> inputPossibleDestinations);

    /**
     * @return the set of possible destinations as pairs of integer and transport
     *         type
     */
    Set<Pair<NodeId, TransportType>> getPossibleDestinations();

    /**
     * Returns whether the current turn is the last before the end of the round.
     *
     * @return whether the current turn is the last before the end of the round
     */
    boolean isRoundLastTurn();

    /**
     * Changes the turn to the next player.
     */
    void changeCurrentPlayer();

    /**
     * Return a boolean value which indicates whether there are multiple transports
     * available for the destination id
     * given or not.
     *
     * @param destinationId the id of the destination
     * @return a boolean value which indicates whether there are multiple transports
     *         available for the destination given
     *         or not
     */
    boolean areMultipleTransportsAvailable(NodeId destinationId);

    /**
     * Return a list of the transport types that can be used to reach the
     * destination given.
     *
     * @param destinationId the id of the destination
     * @return a list of transport types that can be used to reach destination
     */
    List<TransportType> getAvailableTransports(NodeId destinationId);

    /**
     * Return a boolean value which indicates whether the current player can be
     * moved or not.
     * This method need to be called before moveCurrentPlayer(), to check if it is
     * possible
     * to move the current player into the desired destination.
     *
     * @param destinationId
     * @param transport
     * @return a boolean value which indicates whether the current player can be
     *         moved or not
     */
    boolean isMovableCurrentPlayer(NodeId destinationId, TransportType transport);

    /**
     * Moves the current player into the desination given in input, using the
     * transport given in input.
     * The tickets of current player decrement (according to the type of transport
     * used).
     *
     * @param destinationId the destination id
     * @param transport     the transport type to use to reach the destination
     */
    void moveCurrentPlayer(NodeId destinationId, TransportType transport);

    /**
     * Goes to next round by incrementing the round number, if the current player is
     * the last bobby.
     */
    void nextRound();

    /**
     * Return the NodeId of the last revealed Mister X position.
     *
     * @return the node id of the last revealed Mister X position
     */
    NodeId getLastRevealedMisterXPosition();

    /**
     * Return the game difficulty.
     *
     * @return the game difficulty
     */
    GameDifficulty getGameDifficulty();

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
     * Get the players object containing all the players playing the match.
     *
     * @return the players container
     */
    Players getPlayers();

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
     * Gets the current turn's state, which will be merged with the game state at
     * the end of the turn.
     *
     * @return the current turn's state
     */
    TurnState getTurnState();

    /**
     * Gets the runner turn tracker, which tracks the tickets used by MisterX during
     * the game.
     *
     * @return the runner turn tracker
     */
    RunnerTurnTracker getRunnerTurnTracker();

    /**
     * Computes the legal moves of the supplied player.
     *
     * @param mapData       the map data
     * @param player        the player to move
     * @param excludedNodes additional nodes to exclude even if valid
     * @return the legal moves of the supplied player
     */
    List<MoveAction> computeValidMoves(MapData mapData, Player player, List<NodeId> excludedNodes);

    /**
     * Exposes Mister X position for all seekers to see.
     */
    void exposeRunnerPosition();

    /**
     * Current game max round count.
     *
     * @return the max round count of the current game
     */
    int maxRoundCount();

    /**
     * Adds a subscriber to the GameState events.
     *
     * @param subscriber the subscriber
     */
    void subscribe(GameStateSubscriber subscriber);

    /**
     * Notifies all subscribers with the provided action.
     *
     * @param action the action to invoke on all subscribers
     */
    void notifySubscribers(Consumer<GameStateSubscriber> action);

    /**
     * Gets whether Mister X is currently exposed to every player.
     *
     * @return whether Mister X is currently exposed to every player
     */
    boolean isRunnerExposed();

    /**
     * Hides Mister X position from detectives.
     */
    void hideRunnerPosition();

    /**
     * get the timestamp of game init
     */
    long getGameStartTime();

    /**
     * get the timestamp of game end
     */
    long getGameEndTime();

    /**
     * get the game duration in (ms)
     */
    long getGameDuration();

    /**
     * get the duration in HH:mm:ss
     */
    String getFormattedDuration();
}
