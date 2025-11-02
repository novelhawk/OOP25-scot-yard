package it.unibo.scotyard.controller.menu;

import java.util.Objects;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.view.View;

public class MainMenuControllerImpl implements MainMenuController{

    private final Controller controller;
    private final View view;

    /**
     * Creates a start menu controller.
     * 
     * @param controller the main controller
     * @param view       the view component
     * @throws NullPointerException if any parameter is null
     */
    public MainMenuControllerImpl(final Controller controller, final View view) {
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    @Override
    public void run() {
        this.view.displayMainMenu(this);
    }

    @Override
    public void newGame() {
        // TO DO
        this.view.displayNewGameMenu(null);
    }

    @Override
    public void exit() {
        this.controller.exit();
    }
    
}
