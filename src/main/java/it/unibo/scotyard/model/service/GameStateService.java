package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.command.game.InitializeGameCommand;
import it.unibo.scotyard.model.router.CommandHandlerStore;

/**
 * The service responsible for handling the commands regarding changes in the
 * game state.
 *
 */
public class GameStateService {
    private final Model model;

    /**
     * Creates a new GameState service.
     *
     * @param model the model
     */
    public GameStateService(final Model model) {
        this.model = model;
    }

    /**
     * Handles the {@code InitializeGameCommand}.
     *
     * @param command a initialize game command.
     */
    public void handleInitialize(final InitializeGameCommand command) {
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

    /**
     * Registers the service's command handlers to the store.
     *
     * @param store the store that contains the handler registrations
     */
    public void register(final CommandHandlerStore store) {
        store.register(InitializeGameCommand.class, this::handleInitialize);
    }
}
