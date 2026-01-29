package it.unibo.scotyard.model.game.record;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.model.game.GameMode;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonRecordRepository implements RecordRepository {
    private static final Logger LOGGER = Logger.getLogger(JsonRecordRepository.class.getName());
    private static final String RECORDS_FILENAME = "records.json";

    private final String filePath;
    private final Gson gson;

    /**
     * Private constructr for factory mathod
     */
    private JsonRecordRepository(final String filePath, final Gson gson) {
        this.filePath = filePath;
        this.gson = gson;
    }

    /**
     * Factory method to create and initialize a repository.
     *
     * @return initialized repository instance
     * @throws IOException if initialization fails
     */
    public static JsonRecordRepository initialize() throws IOException {
        final String fullPath = Constants.DATA_GAME_FOLDER + File.separator + RECORDS_FILENAME;
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Create data directory if needed
        final Path directory = Paths.get(Constants.DATA_GAME_FOLDER);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
            LOGGER.log(Level.INFO, "Created data directory: {0}", directory);
        }

        // Create records file with empty storage if needed
        final Path file = Paths.get(fullPath);
        if (!Files.exists(file)) {
            final String initialContent = gson.toJson(new RecordStorage());
            Files.writeString(file, initialContent, StandardCharsets.UTF_8);
            LOGGER.log(Level.INFO, "Created records file: {0}", file);
        }

        return new JsonRecordRepository(fullPath, gson);
    }

    @Override
    public boolean updateIfBetter(final GameMode mode, final long durationMillis) {
        if (durationMillis <= 0) {
            return false;
        }

        final RecordStorage storage = loadStorage();
        final GameRecord currentRecord = storage.get(mode);

        // Check if new duration is better
        if (durationMillis > currentRecord.getDurationMillis()) {
            final GameRecord newRecord = new GameRecord(durationMillis, new Date().toString());
            storage.put(mode, newRecord);
            saveStorage(storage);

            LOGGER.log(Level.INFO, "New record for {0}: {1} ms", new Object[] {mode, durationMillis});
            return true;
        }

        return false;
    }

    @Override
    public Optional<GameRecord> findByMode(final GameMode mode) {
        final RecordStorage storage = loadStorage();
        final GameRecord record = storage.get(mode);

        if (record.isValid()) {
            return Optional.of(record);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String getFormattedDuration(final GameMode mode) {
        return findByMode(mode)
                .map(record -> formatDuration(record.getDurationMillis()))
                .orElse("Nessun record");
    }

    /**
     * Formats a duration in milliseconds as HH:mm:ss.
     */
    public static String formatDuration(final long millis) {
        if (millis <= 0) {
            return "00:00:00";
        }

        final long seconds = millis / 1000;
        final long hours = seconds / 3600;
        final long minutes = (seconds % 3600) / 60;
        final long secs = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * Loads record storage from file.
     */
    private RecordStorage loadStorage() {
        try {
            final String content = Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
            final RecordStorage storage = gson.fromJson(content, RecordStorage.class);
            if (storage != null) {
                return storage;
            } else {
                return new RecordStorage();
            }
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load records, using defaults", e);
            return new RecordStorage();
        }
    }

    /**
     * Saves record storage to file.
     */
    private void saveStorage(final RecordStorage storage) {
        try {
            final String json = gson.toJson(storage);
            Files.writeString(Path.of(filePath), json, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save records", e);
        }
    }

    @Override
    public void resetAllRecords() {
        final RecordStorage emptyStorage = new RecordStorage();
        saveStorage(emptyStorage);
        LOGGER.log(Level.INFO, "All records have been reset");
    }
}
