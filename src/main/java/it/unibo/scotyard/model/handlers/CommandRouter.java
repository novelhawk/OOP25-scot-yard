package it.unibo.scotyard.model.handlers;

import it.unibo.scotyard.model.command.GameCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CommandRouter implements CommandDispatcher, CommandHandlerStore {
    private final Map<Class<?>, Consumer<?>> handlers = new HashMap<>();

    public <T extends GameCommand> void register(Class<T> type, Consumer<T> handler) {
        handlers.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends GameCommand> void dispatch(T command) {
        // SAFETY: Type safety is enforced by the register method signature
        Consumer<T> handler = (Consumer<T>) handlers.get(command.getClass());
        Objects.requireNonNull(handler, "No handler found for command " + command.getClass());
        handler.accept(command);
    }
}
