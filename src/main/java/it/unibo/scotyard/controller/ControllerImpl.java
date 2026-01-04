package it.unibo.scotyard.controller;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.game.DetectiveGameControllerImpl;
import it.unibo.scotyard.controller.game.GameController;
import it.unibo.scotyard.controller.game.MrXGameControllerImpl;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherControllerImpl;
import it.unibo.scotyard.controller.menu.MainMenuController;
import it.unibo.scotyard.controller.menu.MainMenuControllerImpl;
import it.unibo.scotyard.controller.menu.NewGameMenuController;
import it.unibo.scotyard.controller.menu.NewGameMenuControllerImpl;
import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.ViewImpl;
import it.unibo.scotyard.view.game.GameView;
import java.util.List;
import java.util.Objects;
import javax.swing.JPanel;

/** Main controller coordinating the MVC flow. */
public final class ControllerImpl implements Controller {

    private final Model model;
    private final ViewImpl view;

    /**
     * Creates a controller with model and view.
     *
     * @param model the game model
     * @param view the game view
     * @throws NullPointerException if any parameter is null
     */
    public ControllerImpl(final Model model, final ViewImpl view) {
        this.model = Objects.requireNonNull(model, "Model cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    @Override
    public void launch() {
        final GameLauncherController launcher = new GameLauncherControllerImpl(this.view, this::run);
        launcher.run();
    }

    @Override
    public void displayPanel(final JPanel panel) {
        this.view.displayPanel(panel);
    }

    @Override
    public void loadMainMenu() {
        final MainMenuController menuController = new MainMenuControllerImpl(this, this.view);
        this.displayPanel(menuController.getMainPanel());
    }

    @Override
    public void loadNewGameMenu() {
        final NewGameMenuController menuController = new NewGameMenuControllerImpl(this, this.view);
        this.displayPanel(menuController.getMainPanel());
    }

    @Override
    public void loadGamePanel(GameController gameController) {
        this.displayPanel(gameController.getMainPanel());
        this.view.forceLayoutUpdate(gameController.getMainPanel(), gameController.getMapPanel());
    }

    @Override
    public void startGame(final String gameMode, final String difficultyLevel, final String playerName) {
        // Initialize the game and load map data from model
        this.model.initialize(gameMode, difficultyLevel);

        // Create the GameView and the GameController
        final GameView gameView =
                this.view.createGameView(this.model.getMapData().info());

        final GameController gameController;
        if (this.model.getGameState().getGameMode() == GameMode.MISTER_X) {
            // Modalità Mister X
            gameController =
                    new MrXGameControllerImpl(this.model.getGameState(), this.model.getMapData(), gameView, this);
        } else {
            // Modalità Detective
            gameController = new DetectiveGameControllerImpl(this.model.getGameState(), gameView, this);
        }
        gameController.initializeGame();
        gameView.setObserver(gameController);

        // Load the game panel
        this.loadGamePanel(gameController);
    }

    @Override
    public List<Pair<NodeId, TransportType>> getPossibleDestinations(NodeId initialPosition) {
        return this.model.getPossibleDestinations(initialPosition);
    }

    @Override
    public void exit() {
        System.out.println("Uscita in corso...");
        System.exit(0);
    }

    // Callback for resolution selection
    private void run(final Size resolution) {
        final Size selectedResolution = Objects.requireNonNull(resolution, "Resolution cannot be null");

        this.view.setWindowMainFeatures(selectedResolution);
        this.loadMainMenu();
    }
}
