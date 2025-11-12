package it.unibo.scotyard.model;

import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.game.GameImpl;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.MapReader;

/**
 * model.
 * Manages map data loading and game state.
 */
public final class ModelImpl implements Model {

    private MapData mapData;
    private Game game;
    private boolean initialized;

    /**
     * new model instance.
     */
    public ModelImpl() {
        this.initialized = false;
    }

    @Override
    public void initialize(String gameMode, String levelDifficulty) {
        try {
            final MapReader mapReader = new MapReader();
            this.mapData = mapReader.loadDefaultMap();
            this.game = new GameImpl(gameMode, levelDifficulty);
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
}