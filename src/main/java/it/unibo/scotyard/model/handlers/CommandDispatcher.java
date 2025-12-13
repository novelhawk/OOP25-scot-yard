package it.unibo.scotyard.model.handlers;

import it.unibo.scotyard.model.command.GameCommand;

public interface CommandDispatcher {
    <T extends GameCommand> void dispatch(T command);
}
