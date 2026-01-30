package it.unibo.scotyard.model.game.matchhistory;

import it.unibo.scotyard.model.game.GameMode;
import java.io.IOException;

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
     * @throws IOException if an I/O error occurs
     */
    void trackWin(GameMode gameMode) throws IOException;

    /**
     * Updates the saved state to include a new lose.
     *
     * @param gameMode the active gameMode when the game was lost
     * @throws IOException if an I/O error occurs
     */
    void trackLose(GameMode gameMode) throws IOException;
}
