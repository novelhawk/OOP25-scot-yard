package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.players.TicketType;
import java.util.Random;

/**
 * The game state.
 *
 */
public interface GameState {

    /**
     * Initialises the game according to the game mode and the level of difficulty chosen.
     *
     * @param gameMode the selected game mode
     * @param levelDifficulty the selected level of difficulty
     */
    void initialize(String gameMode, String levelDifficulty);

    /**
     * @return the seeded shared random instance used by all game logic
     */
    Random getSeededRandom();

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
    int getNumberTicketsUserPlayer(final TicketType ticketType);

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
}
