package it.unibo.scotyard.model.map;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * reads and parses Scotland Yard map data from JSON files.
 * The map data includes nodes, connections, reveal turns, and initial
 * positions.
 */
public class MapReader {
    private static final String DEFAULT_MAP_PATH = "/it/unibo/scotyard/model/map/ScotlandYardMap.json";
    private final Gson gson;

    /**
     * Creates a new MapReader with a configured JSON parser.
     */
    public MapReader() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Loads the default Scotland Yard map from the bundled resource.
     *
     * @return the loaded map data
     * @throws MapLoadException if the map cannot be loaded or parsed
     */
    public MapData loadDefaultMap() throws MapLoadException {
        return loadMap(DEFAULT_MAP_PATH);
    }

    /**
     * Loads a map from the specified resource path.
     *
     * @param resourcePath the classpath resource path to the map JSON file
     * @return the loaded map data
     * @throws MapLoadException     if the map cannot be loaded or parsed
     * @throws NullPointerException if resourcePath is null
     */
    public MapData loadMap(final String resourcePath) throws MapLoadException {
        Objects.requireNonNull(resourcePath, "Resource path cannot be null");

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new MapLoadException("Map file not found: " + resourcePath);
            }

            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                return parseMapData(reader);
            }
        } catch (IOException e) {
            throw new MapLoadException("Error reading map file: " + resourcePath, e);
        }
    }

    private MapData parseMapData(final Reader reader) throws MapLoadException {
        try {
            final JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            if (jsonObject == null) {
                throw new MapLoadException("Invalid JSON: root object is null");
            }

            final String name = jsonObject.get("name").getAsString();
            final List<MapNode> nodes = parseNodes(jsonObject.getAsJsonArray("nodes"));
            final List<MapConnection> connections = parseConnections(jsonObject.getAsJsonArray("connections"));
            final List<Integer> revealTurns = parseIntegerArray(jsonObject.getAsJsonArray("revealTurns"));

            final JsonObject initialPositions = jsonObject.getAsJsonObject("initialPositions");
            final List<Integer> mrXPositions = parseIntegerArray(initialPositions.getAsJsonArray("mrX"));
            final List<Integer> detectivePositions = parseIntegerArray(
                    initialPositions.getAsJsonArray("detective"));

            return new MapData(name, nodes, connections, revealTurns, mrXPositions, detectivePositions);
        } catch (Exception e) {
            throw new MapLoadException("Error parsing map data", e);
        }
    }

    private List<MapNode> parseNodes(final JsonArray jsonArray) {
        final List<MapNode> nodes = new ArrayList<>();

        for (final JsonElement element : jsonArray) {
            final JsonObject nodeObj = element.getAsJsonObject();
            final int id = nodeObj.get("id").getAsInt();
            final int x = nodeObj.get("x").getAsInt();
            final int y = nodeObj.get("y").getAsInt();

            nodes.add(new MapNode(id, x, y));
        }

        return nodes;
    }

    private List<MapConnection> parseConnections(final JsonArray jsonArray) {
        final List<MapConnection> connections = new ArrayList<>();

        for (final JsonElement element : jsonArray) {
            final JsonObject connObj = element.getAsJsonObject();

            final Integer id = connObj.has("id") ? connObj.get("id").getAsInt() : null;
            final int from = connObj.get("from").getAsInt();
            final int to = connObj.get("to").getAsInt();
            final TransportType transport = TransportType.valueOf(connObj.get("transport").getAsString());

            // Parse optional waypoints for visual rendering
            final List<Integer> waypoints = connObj.has("waypoints")
                    ? parseIntegerArray(connObj.get("waypoints").getAsJsonArray())
                    : List.of();

            connections.add(new MapConnection(id, from, to, transport, waypoints));
        }

        return connections;
    }

    private List<Integer> parseIntegerArray(final JsonArray jsonArray) {
        final List<Integer> integers = new ArrayList<>();

        for (final JsonElement element : jsonArray) {
            integers.add(element.getAsInt());
        }

        return integers;
    }

    /**
     * Exception thrown when a map file cannot be loaded or parsed.
     */
    public static class MapLoadException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new MapLoadException with the specified message.
         *
         * @param message the error message
         */
        public MapLoadException(final String message) {
            super(message);
        }

        /**
         * Creates a new MapLoadException with the specified message and cause.
         *
         * @param message the error message
         * @param cause   the underlying cause
         */
        public MapLoadException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

}
