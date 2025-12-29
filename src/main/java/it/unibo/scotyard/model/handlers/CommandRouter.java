package it.unibo.scotyard.model.handlers;

import it.unibo.scotyard.model.command.GameCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CommandRouter implements CommandDispatcher, CommandHandlerStore {
    private final Map<Class<?>, Consumer<?>> handlers = new HashMap<>();

    @Override
    public <T extends GameCommand> void register(final Class<T> type, final Consumer<T> handler) {
        handlers.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GameCommand> void dispatch(final T command) {
        // SAFETY: Type safety is enforced by the register method signature
        final Consumer<T> handler = (Consumer<T>) handlers.get(command.getClass());
        Objects.requireNonNull(handler, "No handler found for command " + command.getClass());
        handler.accept(command);
    }
}
