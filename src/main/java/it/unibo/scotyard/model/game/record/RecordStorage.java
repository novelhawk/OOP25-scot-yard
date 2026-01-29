package it.unibo.scotyard.model.game.record;

import it.unibo.scotyard.model.game.GameMode;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class RecordStorage {

    private final Map<GameMode, GameRecord> records;

    /**
     * Creates storage with provided records map.
     */
    public RecordStorage(final Map<GameMode, GameRecord> records) {
        this.records = new EnumMap<>(Objects.requireNonNull(records, "Records map cannot be null"));
    }

    /**
     * Default constructor initializing empty records.
     */
    public RecordStorage() {
        this.records = new EnumMap<>(GameMode.class);
        this.records.put(GameMode.DETECTIVE, new GameRecord());
        this.records.put(GameMode.MISTER_X, new GameRecord());
    }

    /**
     * Gets the record for a specific game mode.
     */
    public GameRecord get(final GameMode mode) {
        return records.getOrDefault(mode, new GameRecord());
    }

    /**
     * Updates the record for a specific game mode.
     */
    public void put(final GameMode mode, final GameRecord record) {
        records.put(
                Objects.requireNonNull(mode, "Game mode cannot be null"),
                Objects.requireNonNull(record, "Record cannot be null"));
    }

    /**
     * Gets the internal map (for serialization).
     */
    public Map<GameMode, GameRecord> getRecords() {
        return new EnumMap<>(records);
    }
}
