package it.unibo.scotyard.model.game.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.unibo.scotyard.model.game.GameMode;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class RecordStorageTest {

    @Test
    void defaultConstructorInitializesEmptyRecords() {
        final RecordStorage storage = new RecordStorage();

        final GameRecord detectiveRecord = storage.get(GameMode.DETECTIVE);
        final GameRecord mrXRecord = storage.get(GameMode.MISTER_X);

        assertNotNull(detectiveRecord);
        assertNotNull(mrXRecord);
        assertFalse(detectiveRecord.isValid(), "Default detective record should be invalid");
        assertFalse(mrXRecord.isValid(), "Default Mr. X record should be invalid");
    }

    @Test
    void constructorWithMapInitializesCorrectly() {
        final Map<GameMode, GameRecord> records = new EnumMap<>(GameMode.class);
        records.put(GameMode.DETECTIVE, new GameRecord(5000, "2024-01-01"));
        records.put(GameMode.MISTER_X, new GameRecord(6000, "2024-01-02"));

        final RecordStorage storage = new RecordStorage(records);

        assertEquals(5000, storage.get(GameMode.DETECTIVE).getDurationMillis());
        assertEquals(6000, storage.get(GameMode.MISTER_X).getDurationMillis());
    }

    @Test
    void constructorWithNullMapThrows() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new RecordStorage(null);
                },
                "Null map should throw exception");
    }

    @Test
    void putUpdatesRecord() {
        final RecordStorage storage = new RecordStorage();
        final GameRecord newRecord = new GameRecord(8000, "2024-01-03");

        storage.put(GameMode.DETECTIVE, newRecord);

        assertEquals(newRecord, storage.get(GameMode.DETECTIVE));
        assertEquals(8000, storage.get(GameMode.DETECTIVE).getDurationMillis());
    }

    @Test
    void putWithNullModeThrows() {
        final RecordStorage storage = new RecordStorage();
        final GameRecord record = new GameRecord(1000, "2024-01-01");

        assertThrows(
                NullPointerException.class,
                () -> {
                    storage.put(null, record);
                },
                "Null game mode should throw exception");
    }

    @Test
    void putWithNullRecordThrows() {
        final RecordStorage storage = new RecordStorage();

        assertThrows(
                NullPointerException.class,
                () -> {
                    storage.put(GameMode.DETECTIVE, null);
                },
                "Null record should throw exception");
    }

    @Test
    void getReturnsDefaultForNonexistentMode() {
        final RecordStorage storage = new RecordStorage(new EnumMap<>(GameMode.class));

        final GameRecord record = storage.get(GameMode.DETECTIVE);

        assertNotNull(record);
        assertFalse(record.isValid(), "Non-existent mode should return invalid default record");
    }

    @Test
    void getRecordsReturnsImmutableCopy() {
        final RecordStorage storage = new RecordStorage();
        final GameRecord originalRecord = new GameRecord(5000, "2024-01-01");
        storage.put(GameMode.DETECTIVE, originalRecord);

        final Map<GameMode, GameRecord> records = storage.getRecords();
        records.put(GameMode.DETECTIVE, new GameRecord(9999, "2024-12-31"));

        assertEquals(
                5000,
                storage.get(GameMode.DETECTIVE).getDurationMillis(),
                "Modifying returned map should not affect internal storage");
    }

}
