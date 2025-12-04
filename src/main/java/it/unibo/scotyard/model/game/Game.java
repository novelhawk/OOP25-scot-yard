package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.players.TicketType;

public interface Game {

    /**
     * Initialises the game according to the game mode and the level of difficulty chosen.
     *
     * @param gameMode the selected game mode
     * @param levelDifficulty the selected level of difficulty
     */
    void initialize(String gameMode, String levelDifficulty);

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
     * Returns the current round number.
     *
     * @return the integer which represents the game round
     */
    int getGameRound();

    GameState getGameState();

    void setGameState(GameState state);
}
