package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;

public interface Game {

    /**
     * Initialises the game according to the game mode and the level of difficulty chosen.
     * @param gameMode the selected game mode
     * @param levelDifficulty the selected level of difficulty
     */
    void initialize(String gameMode, String levelDifficulty);

    /**
     * Return the number of tickets of a specific type possessed by a specific player.
     * @param player the player (detective, bobby or Mister X)
     * @param ticketType the type of ticket
     */
    int getNumberTickets(Player player, TicketType ticketType);

    /**
     * Returns the current round number.
     * @return the integer which represents the game round
     */
    int getGameRound();

    GameState getGameState();

    void setGameState(GameState state);

}
