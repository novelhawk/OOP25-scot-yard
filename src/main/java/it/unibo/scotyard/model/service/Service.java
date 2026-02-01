package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.router.CommandHandlerStore;

/**
 * A class that contains the Handlers for some GameCommands.
 */
// Justification: Not meant to be used as a functional interface
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface Service {

    /**
     * Registers the service's command handlers to the store.
     *
     * @param store the store that contains the handler registrations
     */
    void register(final CommandHandlerStore store);
}
