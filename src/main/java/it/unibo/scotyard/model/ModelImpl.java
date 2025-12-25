package it.unibo.scotyard.model;

import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.GameStateImpl;
import it.unibo.scotyard.model.handlers.CommandDispatcher;
import it.unibo.scotyard.model.handlers.CommandRouter;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.MapReader;
import it.unibo.scotyard.model.service.GameStateService;
import it.unibo.scotyard.model.service.RoundCommandService;

/** model. Manages map data loading and game state. */
public final class ModelImpl implements Model {

    private final CommandDispatcher dispatcher;
    private MapData mapData;
    private GameState gameState;
    private boolean initialized;

    /** new model instance. */
    public ModelImpl(final CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void initialize(final String gameMode, final String levelDifficulty) {
        try {
            final MapReader mapReader = new MapReader();
            this.mapData = mapReader.loadDefaultMap();
            this.gameState = new GameStateImpl(gameMode, levelDifficulty);
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
    public GameState getGameState() {
        if (!this.initialized || this.mapData == null) {
            throw new IllegalStateException("Modello non inizializzato. Chiamare initialize() prima.");
        }
        return this.gameState;
    }

    @Override
    public CommandDispatcher getDispatcher() {
        return dispatcher;
    }

    public static Model createDefault() {
        final CommandRouter store = new CommandRouter();
        final Model model = new ModelImpl(store);

        final GameStateService gameStateService = new GameStateService(model);
        gameStateService.register(store);

        final RoundCommandService roundCommandService = new RoundCommandService();
        roundCommandService.register(store);

        return model;
    }
}
