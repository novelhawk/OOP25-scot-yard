package it.unibo.scotyard.model.router;

import it.unibo.scotyard.model.command.GameCommand;

/**
 * Handles dispatching commands to the Model layer.
 */
// Justification: Not meant to be used as a functional interface
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface CommandDispatcher {
    /**
     * Dispatches a GameCommand to be later handled by a registered handler.
     *
     * @param command the command to dispatch
     * @param <T> the type of the command to dispatch
     */
    <T extends GameCommand> void dispatch(T command);
}
