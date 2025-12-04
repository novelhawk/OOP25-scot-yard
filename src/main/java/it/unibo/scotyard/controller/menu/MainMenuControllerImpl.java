package it.unibo.scotyard.controller.menu;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.view.View;
import it.unibo.scotyard.view.menu.MainMenuView;
import it.unibo.scotyard.view.menu.MainMenuViewImpl;
import java.util.Objects;
import javax.swing.JPanel;

public class MainMenuControllerImpl implements MainMenuController {

    private final Controller controller;
    private final View view;

    /**
     * Creates a start menu controller.
     *
     * @param controller the main controller
     * @param view the view component
     * @throws NullPointerException if any parameter is null
     */
    public MainMenuControllerImpl(final Controller controller, final View view) {
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    @Override
    public JPanel getMainPanel() {
        final MainMenuView menuView = new MainMenuViewImpl(this, this.view.getMaxResolution());
        return menuView.getMainPanel();
    }

    @Override
    public void newGameMenu() {
        this.controller.loadNewGameMenu();
    }

    @Override
    public void exit() {
        this.controller.exit();
    }
}
