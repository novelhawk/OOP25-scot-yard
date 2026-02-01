package it.unibo.scotyard.model.game.record;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.scotyard.model.game.GameMode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonRecordRepositoryTest {
    private JsonRecordRepository repository;

    @BeforeEach
    void resetRepository() {
        repository = assertDoesNotThrow(JsonRecordRepository::initialize);
        assertDoesNotThrow(repository::resetAllRecords);
    }

    @Test
    void testInitializeSucceeds() {
        assertDoesNotThrow(JsonRecordRepository::initialize);
    }

    @Test
    void testFindByModeReturnsEmptyWhenNoRecord() {
        assertTrue(repository.findByMode(GameMode.DETECTIVE).isEmpty());
        assertTrue(repository.findByMode(GameMode.MISTER_X).isEmpty());
    }

    @Test
    void testUpdateIfBetterFirstRecord() {
        assertTrue(repository.updateIfBetter(GameMode.DETECTIVE, 5000));

        final Optional<GameRecord> record = repository.findByMode(GameMode.DETECTIVE);
        assertTrue(record.isPresent());
        assertEquals(5000, record.get().getDurationMillis());
    }

    @Test
    void testUpdateIfBetterReplacesWhenBetter() {
        repository.updateIfBetter(GameMode.DETECTIVE, 3000);
        assertTrue(repository.updateIfBetter(GameMode.DETECTIVE, 5000));

        assertEquals(5000, repository.findByMode(GameMode.DETECTIVE).get().getDurationMillis());
    }

    @Test
    void testUpdateIfBetterDoesNotReplaceWhenWorse() {
        repository.updateIfBetter(GameMode.DETECTIVE, 8000);
        assertFalse(repository.updateIfBetter(GameMode.DETECTIVE, 5000));

        assertEquals(8000, repository.findByMode(GameMode.DETECTIVE).get().getDurationMillis());
    }

    @Test
    void testUpdateIfBetterInvalidDurations() {
        assertFalse(repository.updateIfBetter(GameMode.DETECTIVE, 0));
        assertFalse(repository.updateIfBetter(GameMode.DETECTIVE, -1000));
        assertTrue(repository.findByMode(GameMode.DETECTIVE).isEmpty());
    }

    @Test
    void testDifferentModesHaveSeparateRecords() {
        repository.updateIfBetter(GameMode.DETECTIVE, 3000);
        repository.updateIfBetter(GameMode.MISTER_X, 7000);

        assertEquals(3000, repository.findByMode(GameMode.DETECTIVE).get().getDurationMillis());
        assertEquals(7000, repository.findByMode(GameMode.MISTER_X).get().getDurationMillis());
    }

    @Test
    void testResetAllRecords() {
        repository.updateIfBetter(GameMode.DETECTIVE, 5000);
        repository.updateIfBetter(GameMode.MISTER_X, 7000);

        assertDoesNotThrow(repository::resetAllRecords);

        assertTrue(repository.findByMode(GameMode.DETECTIVE).isEmpty());
        assertTrue(repository.findByMode(GameMode.MISTER_X).isEmpty());
    }

    @Test
    void testPersistenceAcrossInstances() {
        repository.updateIfBetter(GameMode.DETECTIVE, 12_345);

        final JsonRecordRepository newRepository = assertDoesNotThrow(JsonRecordRepository::initialize);

        assertEquals(12_345, newRepository.findByMode(GameMode.DETECTIVE).get().getDurationMillis());
    }
}
