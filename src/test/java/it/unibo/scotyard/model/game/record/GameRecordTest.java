package it.unibo.scotyard.model.game.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GameRecordTest {

    @Test
    void createValidRecordSucceeds() {
        final GameRecord record = new GameRecord(5000, "2024-01-01");

        assertEquals(5000, record.getDurationMillis());
        assertEquals("2024-01-01", record.getTimestamp());
        assertTrue(record.isValid());
    }

    @Test
    void createRecordWithZeroDurationIsInvalid() {
        final GameRecord record = new GameRecord(0, "2024-01-01");

        assertEquals(0, record.getDurationMillis());
        assertFalse(record.isValid(), "Record with 0 duration should be invalid");
    }

    @Test
    void createRecordWithNegativeDurationThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new GameRecord(-100, "2024-01-01");
                },
                "Negative duration should throw exception");
    }

    @Test
    void createRecordWithNullTimestampThrows() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new GameRecord(1000, null);
                },
                "Null timestamp should throw exception");
    }
}