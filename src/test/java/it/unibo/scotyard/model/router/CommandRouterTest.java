package it.unibo.scotyard.model.router;

import static org.mockito.Mockito.*;

import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.StartTurnCommand;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class CommandRouterTest {
    private final CommandRouter commandRouter = new CommandRouter();

    @Test
    void singleItemWorks() {
        final Consumer<EndTurnCommand> handler = mockConsumer();
        commandRouter.register(EndTurnCommand.class, handler);

        final EndTurnCommand command = new EndTurnCommand();
        commandRouter.dispatch(command);

        verify(handler).accept(command);
    }

    @Test
    void commandRegisteredMultipleTimesReplacesPreviousRegistrations() {
        final Consumer<EndTurnCommand> consumer1 = mockConsumer();
        commandRouter.register(EndTurnCommand.class, consumer1);

        final Consumer<EndTurnCommand> consumer2 = mockConsumer();
        commandRouter.register(EndTurnCommand.class, consumer2);

        final EndTurnCommand command = new EndTurnCommand();
        commandRouter.dispatch(command);

        verify(consumer1, never()).accept(any());
        verify(consumer2).accept(command);
    }

    @Test
    void unregisteredCommandDoesNothing() {
        final Consumer<EndTurnCommand> consumer1 = mockConsumer();
        commandRouter.register(EndTurnCommand.class, consumer1);

        commandRouter.dispatch(new StartTurnCommand());

        verify(consumer1, never()).accept(any());
    }

    private <T> Consumer<T> mockConsumer() {
        @SuppressWarnings("unchecked")
        final Consumer<T> handler = mock(Consumer.class);
        return handler;
    }
}
