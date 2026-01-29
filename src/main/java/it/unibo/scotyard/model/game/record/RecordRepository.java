package it.unibo.scotyard.model.game.record;

import it.unibo.scotyard.model.game.GameMode;
import java.util.Optional;

public interface RecordRepository {

    /**
     * update the record if the new duration is better.
     *
     * @param mode           the game mode
     * @param durationMillis the new duration to check
     * @return true if a new record was set, false otherwise
     */
    boolean updateIfBetter(GameMode mode, long durationMillis);

    /**
     * Finds the record for a specific game mode.
     *
     * @param mode the game mode
     * @return Optional containing the record if valid, empty otherwise
     */
    Optional<GameRecord> findByMode(GameMode mode);

    /**
     * Gets the formatted duration string for a game mode.
     *
     * @param mode the game mode
     * @return formatted duration as HH:mm:ss or "Nessun record"
     */
    String getFormattedDuration(GameMode mode);

    /**
     * Resets all game records to empty values.
     */
    void resetAllRecords();
}
