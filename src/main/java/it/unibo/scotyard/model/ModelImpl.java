package it.unibo.scotyard.model;

import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.game.GameImpl;
import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.MapReader;
import it.unibo.scotyard.model.map.TransportType;
import java.util.ArrayList;
import java.util.List;

/** model. Manages map data loading and game state. */
public final class ModelImpl implements Model {

    private MapData mapData;
    private Game game;
    private boolean initialized;

    /** new model instance. */
    public ModelImpl() {
        this.initialized = false;
    }

    @Override
    public void initialize(String gameMode, String levelDifficulty) {
        try {
            final MapReader mapReader = new MapReader();
            this.mapData = mapReader.loadDefaultMap();
            this.game = new GameImpl(gameMode, levelDifficulty, this.getInitialPositions());
            this.initialized = true;
        } catch (final MapReader.MapLoadException e) {
            System.err.println("Errore caricamento mappa: " + e.getMessage());
            throw new IllegalStateException("Impossibile inizializzare il modello", e);
        }
    }

    @Override
    public MapData getMapData() {
        if (!this.initialized || this.mapData == null) {
            throw new IllegalStateException("Modello non inizializzato. Chiamare initialize() prima.");
        }
        return this.mapData;
    }

    @Override
    public Game getGameData() {
        if (!this.initialized || this.mapData == null) {
            throw new IllegalStateException("Modello non inizializzato. Chiamare initialize() prima.");
        }
        return this.game;
    }

    @Override
    public List<Integer> getInitialPositions() {
        return this.mapData.getInitialPositions();
    }

    @Override
    public List<Pair<Integer, TransportType>> getPossibleDestinations(int idStartPosition) {
        List<Pair<Integer, TransportType>> resultList = new ArrayList<Pair<Integer, TransportType>>();
        List<MapConnection> connections = this.getMapData().getConnections();
        for (MapConnection connection : connections) {
            if (connection.getFrom() == idStartPosition) {
                resultList.add(new Pair<>(connection.getTo(), connection.getTransport()));
            }
            if (connection.getTo() == idStartPosition) {
                resultList.add(new Pair<>(connection.getFrom(), connection.getTransport()));
            }
        }
        return resultList;
    }
}
