package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.List;

public interface Game {

    /**
     * Initialises the game according to the game mode and the level of difficulty chosen.
     *
     * @param gameMode the selected game mode
     * @param levelDifficulty the selected level of difficulty
     * @param initialPositions the list of the possible initial positions of players
     */
    void initialize(String gameMode, String levelDifficulty, List<Integer> initialPositions);

    /**
     * Returns a boolean that indicates if the game is over. In particular, the game is over if the detective or one of
     * the bobbies has captured Mister X (they're in the same position of the map) or if the maximum number of rounds
     * had been reached.
     *
     * @return a boolean which indicates whether the game is over (true) or not
     */
    boolean isGameOver();

    /** This method gets called when the game is over, to get the winner (Detective or Mister X).
     * If detective or bobbies caught Mister X (they're in the same position), then the winner is the detective;
     * else it's Mister X. 
     * 
     * @return GameMode, which indicates whether the winner is Detective or Mister X
     */
    GameMode winner();

    /**
     * Loads into a specific variable the possible destinations
     *
     * @param inputPossibleDestinations the possible destinations loaded from Model
     */
    void loadPossibleDestinations(List<Pair<Integer, TransportType>> inputPossibleDestinations);

    /** @return the list of possible destinations as pairs of integer and transport type */
    List<Pair<Integer, TransportType>> getPossibleDestinations();

    /** Manages the current player. */
    void changeCurrentPlayer();

    /**
     * Return a boolean value which indicates whether the current player can be moved or not; if it's possible, 
     * their position id gets changed and their tickets decrement (according to the type of transport used).
     *
     * @param destinationId the destination id
     * @param transport the transport type to use to reach the destination
     * @return a boolean value which indicates whether the current player can be moved or not
     */
    boolean moveCurrentPlayer(int destinationId, TransportType transport);

    /** Goes to next round (increments the round number). */
    void nextRound();

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
     * @return the number of tickets of a specific tyep of the user player
     */
    int getNumberTicketsUserPlayer(TicketType ticketType);

    /** Return the current player. */
    Player getCurrentPlayer();

    /**
     * Return the current position of the player passed as input.
     *
     * @return the current position of the player passed as input
     */
    int getPositionPlayer(Player player);

    /** Return the number of players */
    int getNumberOfPlayers();

    /**
     * Returns the current round number.
     *
     * @return the integer which represents the game round
     */
    int getGameRound();

    GameState getGameState();

    void setGameState(GameState state);
}
