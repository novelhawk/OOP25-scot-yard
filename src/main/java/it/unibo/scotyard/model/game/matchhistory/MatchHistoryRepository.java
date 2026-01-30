package it.unibo.scotyard.model.game.matchhistory;

import it.unibo.scotyard.model.game.GameMode;
import java.io.IOException;
import java.util.function.Function;

/**
 * Handle persistence of statistics from previous games.
 */
public interface MatchHistoryRepository {
    /**
     * Loads the current persisted state of match history.
     * @return the current match history state or the default if not present.
     */
    MatchHistory loadOrDefault();

    /**
     * Updates the saved state to include a new win.
     *
     * @param gameMode the active gameMode when the game was won
     * @param hasWon the game winning outcome
     * @throws IOException if an I/O error occurs
     */
    void trackOutcome(GameMode gameMode, boolean hasWon) throws IOException;

    /**
     * Loads, transforms and then saves to disk a MatchHistory
     *
     * @param mutator the mutator to apply
     * @throws IOException if an I/O error occurs
     */
    void update(Function<MatchHistory, MatchHistory> mutator) throws IOException;
}
