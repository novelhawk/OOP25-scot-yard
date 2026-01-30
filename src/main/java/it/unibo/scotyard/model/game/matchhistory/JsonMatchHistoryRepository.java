package it.unibo.scotyard.model.game.matchhistory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.model.game.GameMode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An implementation of MatchHistoryRepository that stores the MatchHistory state in a JSON file.
 * This implementation never stores the state in memory and fetches it from disk at every request.
 */
public class JsonMatchHistoryRepository implements MatchHistoryRepository {

    private static final String FILE_NAME = "matchhistory.json";
    private final Path filePath;
    private final Gson gson;

    private JsonMatchHistoryRepository(final Path filePath, final Gson gson) {
        this.filePath = filePath;
        this.gson = gson;
    }

    @Override
    public MatchHistory loadOrDefault() {
        try {
            return load();
        } catch (IOException | JsonSyntaxException e) {
            return MatchHistory.getDefault();
        }
    }

    @Override
    public void trackWin(GameMode gameMode) throws IOException {
        update(incrementOnce(gameMode, true));
    }

    @Override
    public void trackLose(GameMode gameMode) throws IOException {
        update(incrementOnce(gameMode, false));
    }

    /**
     * Loads the MatchHistory from disk
     *
     * @return the loaded MatchHistory
     * @throws IOException if an I/O error occurs
     */
    private MatchHistory load() throws IOException {
        final String json = Files.readString(this.filePath, StandardCharsets.UTF_8);
        return gson.fromJson(json, MatchHistory.class);
    }

    /**
     * Loads, transforms and then saves to disk a MatchHistory
     *
     * @param mutator the mutator to apply
     * @throws IOException if an I/O error occurs
     */
    private void update(Function<MatchHistory, MatchHistory> mutator) throws IOException {
        final MatchHistory current = loadOrDefault();
        final MatchHistory updated = mutator.apply(current);
        save(updated);
    }

    /**
     * Saves the provided MatchHistory to disk
     *
     * @param matchHistory the MatchHistory to save
     * @throws IOException if an I/O error occurs
     */
    private void save(MatchHistory matchHistory) throws IOException {
        final String json = gson.toJson(matchHistory);
        Files.writeString(this.filePath, json, StandardCharsets.UTF_8);
    }

    /**
     * Returns a MatchHistory mutator that increments the component matching the supplied GameMode and win state by one
     *
     * @param gameMode the GameMode to match
     * @param hasWon the win status to match
     * @return a MatchHistory mutator that increments the matching component by one
     */
    private static Function<MatchHistory, MatchHistory> incrementOnce(GameMode gameMode, boolean hasWon) {
        final var componentMatcher = componentMatcher(gameMode, hasWon, it -> it + 1);
        return mergeComponentMutators(
                componentMatcher.apply(GameMode.MISTER_X, true),
                componentMatcher.apply(GameMode.MISTER_X, false),
                componentMatcher.apply(GameMode.DETECTIVE, true),
                componentMatcher.apply(GameMode.DETECTIVE, false));
    }

    /**
     * Creates a component matcher that when the GameMode and win status matches the one
     * provided returns the mutator supplied, otherwise returns the identity function
     *
     * @param gameMode the GameMode to match
     * @param hasWon the win status to match
     * @param mutator the mutator to apply when the gameMode and hasWon match
     * @return a component matcher that returns the mutator to apply based on the component
     */
    private static BiFunction<GameMode, Boolean, Function<Integer, Integer>> componentMatcher(
            GameMode gameMode, boolean hasWon, Function<Integer, Integer> mutator) {
        return (componentGameMode, componentHasWon) -> {
            if (componentGameMode == gameMode && componentHasWon == hasWon) {
                return mutator;
            } else {
                return Function.identity();
            }
        };
    }

    /**
     * Joins together the mutator of each component of a MatchHistory into a single mutator of the whole MatchHistory.
     *
     * @param runnerWinsMutator the runner wins component mutator
     * @param runnerLosesMutator the runner loses component mutator
     * @param seekerWinsMutator the seeker wins component mutator
     * @param seekerLosesMutator the seeker loses component mutator
     * @return the MatchHistory mutator that applies the respective mutator to each of its components.
     */
    private static Function<MatchHistory, MatchHistory> mergeComponentMutators(
            Function<Integer, Integer> runnerWinsMutator,
            Function<Integer, Integer> runnerLosesMutator,
            Function<Integer, Integer> seekerWinsMutator,
            Function<Integer, Integer> seekerLosesMutator) {
        return it -> new MatchHistory(
                runnerWinsMutator.apply(it.runnerWins()),
                runnerLosesMutator.apply(it.runnerLoses()),
                seekerWinsMutator.apply(it.seekerWins()),
                seekerLosesMutator.apply(it.seekerLoses()));
    }

    /**
     * Factory method to create the JsonMatchHistoryRepository
     *
     * @return an initialized JsonMatchHistoryRepository
     * @throws IOException if an I/O error occurs
     */
    public static MatchHistoryRepository initialize() throws IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final Path directory = Path.of(Constants.DATA_GAME_FOLDER);
        final Path fullPath = directory.resolve(FILE_NAME);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        return new JsonMatchHistoryRepository(fullPath, gson);
    }
}
