package it.unibo.scotyard.model.game;

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

    /** Goes to next round (increments the round number). */
    void continueGame();

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

    /**
     * Return the current position of the player passed as input.
     *
     * @return the current position of the player passed as input
     */
    int getPositionPlayer(Player player);

    /**
     * Returns the current round number.
     *
     * @return the integer which represents the game round
     */
    int getGameRound();

    GameState getGameState();

    void setGameState(GameState state);
}
