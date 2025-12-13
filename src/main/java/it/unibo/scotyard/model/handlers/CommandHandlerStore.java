package it.unibo.scotyard.model.handlers;

import it.unibo.scotyard.model.command.GameCommand;
import java.util.function.Consumer;

public interface CommandHandlerStore {
    <T extends GameCommand> void register(Class<T> type, Consumer<T> handler);
}
