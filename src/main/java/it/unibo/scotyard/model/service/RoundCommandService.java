package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.command.round.MoveCommand;
import it.unibo.scotyard.model.command.round.PassCommand;
import it.unibo.scotyard.model.command.round.ResetCommand;
import it.unibo.scotyard.model.handlers.CommandHandlerStore;

public class RoundCommandService {
    public void handleMove(MoveCommand command) {}

    public void handlePass(PassCommand command) {}

    public void handleReset(ResetCommand command) {}

    public void register(final CommandHandlerStore store) {
        store.register(MoveCommand.class, this::handleMove);
        store.register(PassCommand.class, this::handlePass);
        store.register(ResetCommand.class, this::handleReset);
    }
}
