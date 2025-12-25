package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.command.round.MoveCommand;
import it.unibo.scotyard.model.command.round.PassCommand;
import it.unibo.scotyard.model.command.round.ResetCommand;
import it.unibo.scotyard.model.handlers.CommandHandlerStore;

/**
 * The service responsible for handling the commands regarding a game round.
 *
 */
public class RoundCommandService {
    /**
     * Handles the {@code MoveCommand}.
     *
     * @param command a move command.
     */
    public void handleMove(final MoveCommand command) {}

    /**
     * Handles the {@code PassCommand}.
     *
     * @param command a pass command.
     */
    public void handlePass(final PassCommand command) {}

    /**
     * Handles the {@code ResetCommand}.
     *
     * @param command a reset command.
     */
    public void handleReset(final ResetCommand command) {}

    /**
     * Registers the service's command handlers to the store.
     *
     * @param store the store that contains the handler registrations
     */
    public void register(final CommandHandlerStore store) {
        store.register(MoveCommand.class, this::handleMove);
        store.register(PassCommand.class, this::handlePass);
        store.register(ResetCommand.class, this::handleReset);
    }
}
