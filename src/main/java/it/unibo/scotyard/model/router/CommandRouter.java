package it.unibo.scotyard.model.router;

import it.unibo.scotyard.model.command.GameCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Stores a handler for each GameCommand.
 * This implementation supports at most one handler per GameCommand.
 */
public class CommandRouter implements CommandDispatcher, CommandHandlerStore {
    private final Map<Class<?>, Consumer<?>> handlers = new HashMap<>();

    public CommandRouter() {}

    /**
     * Registers a new handler for the GameCommand removing any previous registered handlers.
     *
     * @param type the GameCommand associated with the handler
     * @param handler the registered handler
     * @param <T> the type of the GameCommand associated with the handler
     */
    @Override
    public <T extends GameCommand> void register(final Class<T> type, final Consumer<T> handler) {
        handlers.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GameCommand> void dispatch(final T command) {
        // SAFETY: Type safety is enforced by the register method signature
        final Consumer<T> handler = (Consumer<T>) handlers.get(command.getClass());
        if (handler != null) {
            handler.accept(command);
        }
    }
}
