package it.unibo.scotyard.controller;

import java.util.Objects;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherControllerImpl;
import it.unibo.scotyard.controller.menu.MainMenuController;
import it.unibo.scotyard.controller.menu.MainMenuControllerImpl;
import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.view.ViewImpl;

/**
 * Main controller coordinating the MVC flow.
 */
public final class ControllerImpl implements Controller {

    private final Model model;
    private final ViewImpl view;
    private Size selectedResolution;

    /**
     * Creates a controller with model and view.
     * 
     * @param model the game model
     * @param view  the game view
     * @throws NullPointerException if any parameter is null
     */
    public ControllerImpl(final Model model, final ViewImpl view) {
        this.model = Objects.requireNonNull(model, "Model cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    @Override
    public void launch() {
        final GameLauncherController launcher = new GameLauncherControllerImpl(
                this.view,
                this::run);
        launcher.run();
    }

    @Override
    public void startGame() {
        // Load map data from model
        this.model.initialize();

        // Initialize view with map data
        this.view.initialize(this.model.getMapData().info());

        // Display game window with selected resolution
        this.view.displayWindow(this.selectedResolution);
    }

    @Override
    public void exit() {
        System.out.println("Uscita in corso...");
        System.exit(0);
    }

    // Callback for resolution selection
    private void run(final Size resolution) {
        this.selectedResolution = Objects.requireNonNull(resolution, "Resolution cannot be null");

        final MainMenuController menuController = new MainMenuControllerImpl(this, this.view);
        menuController.run();
    }
}