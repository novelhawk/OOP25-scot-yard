package it.unibo.scotyard.model.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * reads and parses Scotland Yard map data from JSON files. The map data includes nodes,
 * connections, reveal turns, and initial positions.
 */
public class MapReader {
    private static final String DEFAULT_MAP_PATH = "/it/unibo/scotyard/model/map/ScotlandYardMap.json";
    private final Gson gson;

    /** Creates a new MapReader with a configured JSON parser. */
    public MapReader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
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
     * @throws MapLoadException if the map cannot be loaded or parsed
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
        final JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

        if (jsonObject == null) {
            throw new MapLoadException("Invalid JSON: root object is null");
        }

        try {
            final String name = jsonObject.get("name").getAsString();
            final List<MapNode> nodes = parseNodes(jsonObject.getAsJsonArray("nodes"));
            final List<MapConnection> connections = parseConnections(jsonObject);
            final List<Integer> revealTurns = parseIntegerArray(jsonObject.getAsJsonArray("revealTurns"));
            final List<NodeId> initialPositions =
                    parseIntegerArray(jsonObject.getAsJsonArray("initialPositions")).stream()
                            .map(NodeId::new)
                            .collect(Collectors.toList());

            return new MapData(name, nodes, connections, revealTurns, initialPositions);
        } catch (Exception e) {
            throw new MapLoadException("Error parsing map data", e);
        }
    }

    private List<MapNode> parseNodes(final JsonArray jsonArray) {
        final List<MapNode> nodes = new ArrayList<>();

        // Nodes are arrays [x, y] with implicit IDs (index + 1)
        int id = 1;
        for (final JsonElement element : jsonArray) {
            final JsonArray nodeArray = element.getAsJsonArray();
            final int x = nodeArray.get(0).getAsInt();
            final int y = nodeArray.get(1).getAsInt();

            nodes.add(new MapNode(new NodeId(id), x, y));
            id++;
        }

        return nodes;
    }

    /**
     * Parses connections from JSON where transports are separated by type. Has "taxi", "bus",
     * "underground", "black" arrays with [from, to] pairs.
     *
     * @param jsonObject the root JSON object containing transport arrays
     * @return list of all connections combined from all transport types
     */
    private List<MapConnection> parseConnections(final JsonObject jsonObject) {
        final List<MapConnection> connections = new ArrayList<>();

        // Parse each transport type if present
        if (jsonObject.has("taxi")) {
            connections.addAll(parseTransportConnections(jsonObject.getAsJsonArray("taxi"), TransportType.TAXI));
        }
        if (jsonObject.has("bus")) {
            connections.addAll(parseTransportConnections(jsonObject.getAsJsonArray("bus"), TransportType.BUS));
        }
        if (jsonObject.has("underground")) {
            connections.addAll(
                    parseTransportConnections(jsonObject.getAsJsonArray("underground"), TransportType.UNDERGROUND));
        }
        if (jsonObject.has("black")) {
            connections.addAll(parseTransportConnections(jsonObject.getAsJsonArray("black"), TransportType.FERRY));
        }

        return connections;
    }

    /**
     * Parses connections from an array of [from, to] pairs for a specific transport type.
     *
     * @param jsonArray the array of [from, to] pairs
     * @param transport the transport type for these connections
     * @return list of connections for this transport type
     */
    private List<MapConnection> parseTransportConnections(final JsonArray jsonArray, final TransportType transport) {
        final List<MapConnection> connections = new ArrayList<>();

        for (final JsonElement element : jsonArray) {
            final JsonArray connectionArray = element.getAsJsonArray();
            final NodeId from = new NodeId(connectionArray.get(0).getAsInt());
            final NodeId to = new NodeId(connectionArray.get(1).getAsInt());

            connections.add(new MapConnection(null, from, to, transport));
            connections.add(new MapConnection(null, to, from, transport));
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

    /** Exception thrown when a map file cannot be loaded or parsed. */
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
         * @param cause the underlying cause
         */
        public MapLoadException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
