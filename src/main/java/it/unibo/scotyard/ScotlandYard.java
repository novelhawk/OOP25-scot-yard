package it.unibo.scotyard;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.controller.ControllerImpl;
import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.ModelImpl;
import it.unibo.scotyard.view.ViewImpl;

/** Main application entry point for Scotland Yard game. */
public final class ScotlandYard {

    private ScotlandYard() {
        throw new AssertionError("Non-instantiable application class");
    }

    /**
     * Application main method.
     *
     * @param args command line arguments (unused)
     */
    public static void main(final String[] args) {
        // Initialize MVC components
        final Model model = new ModelImpl();
        final ViewImpl view = new ViewImpl();
        final Controller controller = new ControllerImpl(model, view);

        // Launch application
        controller.launch();
    }
}
