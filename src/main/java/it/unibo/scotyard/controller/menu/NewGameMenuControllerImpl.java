package it.unibo.scotyard.controller.menu;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.view.View;
import it.unibo.scotyard.view.menu.NewGameMenuView;
import it.unibo.scotyard.view.menu.NewGameMenuViewImpl;
import java.util.Objects;
import javax.swing.JPanel;

/** start menu controller. */
public final class NewGameMenuControllerImpl implements NewGameMenuController {

    private final Controller controller;
    private final View view;

    /**
     * Creates a start menu controller.
     *
     * @param controller the main controller
     * @param view the view component
     * @throws NullPointerException if any parameter is null
     */
    public NewGameMenuControllerImpl(final Controller controller, final View view) {
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    @Override
    public JPanel getMainPanel() {
        final NewGameMenuView menuView = new NewGameMenuViewImpl(this, this.view.getMaxResolution());
        return menuView.getMainPanel();
    }

    @Override
    public void play(String gameMode, String difficultyLevel, String playerName) {
        this.controller.startGame(gameMode, difficultyLevel, playerName);
    }

    @Override
    public void exit() {
        this.controller.exit();
    }

    @Override
    public void mainMenu() {
        this.controller.loadMainMenu();
    }
}
