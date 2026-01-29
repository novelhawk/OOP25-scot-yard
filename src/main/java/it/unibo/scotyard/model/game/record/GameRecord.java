package it.unibo.scotyard.model.game.record;

import java.util.Objects;

public final class GameRecord {

    private final long durationMillis;
    private final String timestamp;

    /**
     * Creates a new game record.
     *
     * @param durationMillis the game duration in milliseconds
     * @param timestamp      the date/time when record was set
     */
    public GameRecord(final long durationMillis, final String timestamp) {
        if (durationMillis < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        this.durationMillis = durationMillis;
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    /**
     * Default constructor for Gson.
     */
    public GameRecord() {
        this(0, "");
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Checks if this record is valid.
     */
    public boolean isValid() {
        return durationMillis > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GameRecord that = (GameRecord) o;
        return durationMillis == that.durationMillis && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(durationMillis, timestamp);
    }
}
