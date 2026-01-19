package it.unibo.scotyard.model.router;

import it.unibo.scotyard.model.command.GameCommand;
import java.util.function.Consumer;

/**
 * Handles registering command handlers for each GameCommand
 */
// Justification: Not meant to be used as a functional interface
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface CommandHandlerStore {
    /**
     * Registers a new handler for the specified GameCommand
     *
     * @param type the GameCommand associated with the handler
     * @param handler the registered handler
     * @param <T> the type of the GameCommand associated with the handler
     */
    <T extends GameCommand> void register(Class<T> type, Consumer<T> handler);
}
