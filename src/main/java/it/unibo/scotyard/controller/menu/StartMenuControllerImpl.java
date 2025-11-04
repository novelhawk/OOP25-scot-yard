package it.unibo.scotyard.controller.menu;

import java.util.Objects;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.view.View;

/**
 * start menu controller.
 */
public final class StartMenuControllerImpl implements StartMenuController {

    private final Controller controller;
    private final View view;

    /**
     * Creates a start menu controller.
     * 
     * @param controller the main controller
     * @param view       the view component
     * @throws NullPointerException if any parameter is null
     */
    public StartMenuControllerImpl(final Controller controller, final View view) {
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    @Override
    public void run() {
        this.view.displayMenu(this);
    }

    @Override
    public void play() {
        this.controller.startGame();
    }

    @Override
    public void exit() {
        this.controller.exit();
    }
}