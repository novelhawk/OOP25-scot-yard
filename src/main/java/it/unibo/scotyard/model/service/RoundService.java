package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.command.round.EndRoundCommand;
import it.unibo.scotyard.model.command.round.StartRoundCommand;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.router.CommandDispatcher;
import it.unibo.scotyard.model.router.CommandHandlerStore;
import java.util.Objects;

public class RoundService {
    private final Model model;

    public RoundService(final Model model) {
        this.model = Objects.requireNonNull(model, "model cannot be null");
    }

    /**
     * Handles the {@code StartRoundCommand}.
     *
     * @param command a start round command.
     */
    public void handleStartRound(final StartRoundCommand command) {}

    /**
     * Handles the {@code EndRoundCommand}.
     *
     * @param command a end round command.
     */
    public void handleEndRound(final EndRoundCommand command) {
        final CommandDispatcher dispatcher = model.getDispatcher();
        final GameState gameState = model.getGameState();

        if (gameState.isRunnerExposed() && gameState.getGameMode() != GameMode.MISTER_X) {
            gameState.hideRunnerPosition();
        }

        gameState.nextRound();
        dispatcher.dispatch(new StartRoundCommand());
    }

    /**
     * Registers the service's command handlers to the store.
     *
     * @param store the store that contains the handler registrations
     */
    public void register(final CommandHandlerStore store) {
        store.register(StartRoundCommand.class, this::handleStartRound);
        store.register(EndRoundCommand.class, this::handleEndRound);
    }
}
