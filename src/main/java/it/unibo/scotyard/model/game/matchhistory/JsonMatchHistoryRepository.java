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
import java.util.function.Function;

/**
 * An implementation of MatchHistoryRepository that stores the MatchHistory state in a JSON file.
 * This implementation never stores the state in memory and fetches it from disk at every request.
 */
public final class JsonMatchHistoryRepository implements MatchHistoryRepository {

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
        update(MatchHistory.incrementOnce(gameMode, true));
    }

    @Override
    public void trackLose(GameMode gameMode) throws IOException {
        update(MatchHistory.incrementOnce(gameMode, false));
    }

    @Override
    public void update(Function<MatchHistory, MatchHistory> mutator) throws IOException {
        final MatchHistory current = loadOrDefault();
        final MatchHistory updated = mutator.apply(current);
        save(updated);
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
