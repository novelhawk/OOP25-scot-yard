package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.command.game.InitializeGameCommand;
import it.unibo.scotyard.model.handlers.CommandHandlerStore;

public class GameStateService {
    private final Model model;

    public GameStateService(final Model model) {
        this.model = model;
    }

    public void handleInitialize(InitializeGameCommand command) {
        final var initialPositions = model.getMapData().getInitialPositions();
        final var playerPositions = model.getGameState()
                .getSeededRandom()
                .ints(0, initialPositions.size())
                .distinct()
                .limit(3)
                .mapToObj(initialPositions::get)
                .toList();

        // TODO: Create GameState instance here, based on number of players
    }

    public void register(final CommandHandlerStore store) {
        store.register(InitializeGameCommand.class, this::handleInitialize);
    }
}
