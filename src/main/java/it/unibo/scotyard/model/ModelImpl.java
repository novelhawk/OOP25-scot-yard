package it.unibo.scotyard.model;

import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.GameStateImpl;
import it.unibo.scotyard.model.handlers.CommandDispatcher;
import it.unibo.scotyard.model.handlers.CommandRouter;
import it.unibo.scotyard.model.map.*;
import it.unibo.scotyard.model.service.GameStateService;
import it.unibo.scotyard.model.service.RoundCommandService;
import java.util.ArrayList;
import java.util.List;

/** model. Manages map data loading and game state. */
public final class ModelImpl implements Model {

    private final CommandDispatcher dispatcher;
    private MapData mapData;
    private GameState gameState;
    private boolean initialized;

    /**
     * The model layer state.
     *
     * @param dispatcher the command dispatcher
     */
    public ModelImpl(final CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void initialize(final String gameMode, final String levelDifficulty) {
        try {
            final MapReader mapReader = new MapReader();
            this.mapData = mapReader.loadDefaultMap();
            this.gameState = new GameStateImpl(gameMode, levelDifficulty, this.getInitialPositions());
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

    /**
     * Creates the {@code Model} with the default command listeners.
     *
     * @return the model instance
     */
    public static Model createDefault() {
        final CommandRouter store = new CommandRouter();
        final Model model = new ModelImpl(store);

        final GameStateService gameStateService = new GameStateService(model);
        gameStateService.register(store);

        final RoundCommandService roundCommandService = new RoundCommandService();
        roundCommandService.register(store);

        return model;
    }

    @Override
    public List<NodeId> getInitialPositions() {
        return this.mapData.getInitialPositions();
    }

    @Override
    public List<Pair<NodeId, TransportType>> getPossibleDestinations(NodeId idStartPosition) {
        List<Pair<NodeId, TransportType>> resultList = new ArrayList<>();
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
