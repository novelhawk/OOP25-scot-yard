package it.unibo.scotyard.model.game;

import java.util.List;

import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;

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
     * Return the current game mode.
     *
     * @return the game mode
     */
    GameMode getGameMode();

    /**
     * Return the number of tickets of a specific type possessed by the user player.
     *
     * @param ticketType the type of ticket
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
