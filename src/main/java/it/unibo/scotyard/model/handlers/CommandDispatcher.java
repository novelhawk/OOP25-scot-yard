package it.unibo.scotyard.model.handlers;

import it.unibo.scotyard.model.command.GameCommand;

// Justification: Not meant to be used as a functional interface
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface CommandDispatcher {
    <T extends GameCommand> void dispatch(T command);
}
