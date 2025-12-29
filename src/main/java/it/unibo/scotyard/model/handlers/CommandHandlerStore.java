package it.unibo.scotyard.model.handlers;

import it.unibo.scotyard.model.command.GameCommand;
import java.util.function.Consumer;

// Justification: Not meant to be used as a functional interface
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface CommandHandlerStore {
    <T extends GameCommand> void register(Class<T> type, Consumer<T> handler);
}
