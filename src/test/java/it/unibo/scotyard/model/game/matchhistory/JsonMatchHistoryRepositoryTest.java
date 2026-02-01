package it.unibo.scotyard.model.game.matchhistory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unibo.scotyard.model.game.GameMode;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JsonMatchHistoryRepositoryTest {
    private JsonMatchHistoryRepository matchHistoryRepository;
    private final boolean WON = true;
    private final boolean LOST = false;

    @BeforeEach
    void resetInitially() {
        matchHistoryRepository = assertDoesNotThrow(JsonMatchHistoryRepository::initialize);
        assertDoesNotThrow(matchHistoryRepository::resetTracking);
    }

    @Test
    void loadDefaultStartsAtZero() {
        final MatchHistory matchHistory = matchHistoryRepository.loadOrDefault();
        assertEquals(0, matchHistory.runnerWins(), "Stats should initialize at zero");
        assertEquals(0, matchHistory.runnerLoses(), "Stats should initialize at zero");
        assertEquals(0, matchHistory.seekerWins(), "Stats should initialize at zero");
        assertEquals(0, matchHistory.seekerLoses(), "Stats should initialize at zero");
    }

    @Test
    void loadUpdatedAfterIncrease() {
        // Arrange
        assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.MISTER_X, WON));

        // Act
        final MatchHistory actual = matchHistoryRepository.loadOrDefault();

        // Assert
        assertEquals(1, actual.runnerWins());
        assertEquals(0, actual.runnerLoses());
        assertEquals(0, actual.seekerWins());
        assertEquals(0, actual.seekerLoses());
    }

    @Test
    void previouslyLoadedImmutable() {
        // Arrange
        final MatchHistory initial = matchHistoryRepository.loadOrDefault();

        assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.MISTER_X, WON));

        // Act
        final MatchHistory actual = matchHistoryRepository.loadOrDefault();

        // Assert
        assertEquals(0, initial.runnerWins());
        assertEquals(1, actual.runnerWins());
    }

    @ParameterizedTest
    @MethodSource("incrementSequences")
    void increaseWorksProperly(int runnerWins, int runnerLoses, int seekerWins, int seekerLoses) {
        // Act
        for (int i = 0; i < runnerWins; i++) {
            assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.MISTER_X, WON));
        }

        for (int i = 0; i < runnerLoses; i++) {
            assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.MISTER_X, LOST));
        }

        for (int i = 0; i < seekerWins; i++) {
            assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.DETECTIVE, WON));
        }

        for (int i = 0; i < seekerLoses; i++) {
            assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.DETECTIVE, LOST));
        }

        // Assert
        final MatchHistory actual = matchHistoryRepository.loadOrDefault();
        assertEquals(runnerWins, actual.runnerWins());
        assertEquals(runnerLoses, actual.runnerLoses());
        assertEquals(seekerWins, actual.seekerWins());
        assertEquals(seekerLoses, actual.seekerLoses());
    }

    @Test
    void resetInitializesStatistics() {
        // Arrange
        assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.MISTER_X, WON));
        assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.DETECTIVE, LOST));

        // Act
        assertDoesNotThrow(matchHistoryRepository::resetTracking);

        // Assert
        final MatchHistory actual = matchHistoryRepository.loadOrDefault();
        assertEquals(0, actual.runnerWins());
        assertEquals(0, actual.runnerLoses());
        assertEquals(0, actual.seekerWins());
        assertEquals(0, actual.seekerLoses());
    }

    @Test
    void updateAddTwoEverywhereWorks() {
        // Arrange
        assertDoesNotThrow(() -> matchHistoryRepository.trackOutcome(GameMode.MISTER_X, WON));
        final Function<MatchHistory, MatchHistory> addTwoEverywhere = it -> new MatchHistoryImpl(
                it.runnerWins() + 2, it.runnerLoses() + 2, it.seekerWins() + 2, it.seekerLoses() + 2);

        // Act
        assertDoesNotThrow(() -> matchHistoryRepository.update(addTwoEverywhere));

        // Assert
        final MatchHistory actual = matchHistoryRepository.loadOrDefault();
        assertEquals(3, actual.runnerWins());
        assertEquals(2, actual.runnerLoses());
        assertEquals(2, actual.seekerWins());
        assertEquals(2, actual.seekerLoses());
    }

    private static Stream<Arguments> incrementSequences() {
        // Cartesian product of 4 numbers in range [0, 2]
        return IntStream.iterate(1, i -> i + 1)
                .mapToObj(it -> Arguments.of(it / 27 % 3, it / 9 % 3, it / 3 % 3, it % 3))
                .limit(3 * 3 * 3 * 3 - 1);
    }
}
