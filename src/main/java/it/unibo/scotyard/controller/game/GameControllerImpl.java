package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import java.util.Objects;
import javax.swing.JPanel;

/**
 * The controller for all game related actions
 *
 */
public abstract class GameControllerImpl implements GameController {

    protected final GameState gameState;
    protected final GameView view;
    protected final Controller mainController;

    /**
     * Creates the controller
     *
     * @param gameState the game state
     * @param view the view
     * @param controller the controller
     */
    public GameControllerImpl(final GameState gameData, final GameView view, final Controller controller) {
        this.gameState = Objects.requireNonNull(gameData, "Game cannot be null");
        this.view = Objects.requireNonNull(view, "GameView cannot be null");
        this.mainController = Objects.requireNonNull(controller, "mainController cannot be null");
    }

    @Override
    public abstract void initializeGame();

    @Override
    public JPanel getMainPanel() {
        return this.view.getMainPanel();
    }

    @Override
    public MapPanel getMapPanel() {
        return this.view.getMapPanel();
    }

    @Override
    public SidebarPanel getSidebarPanel() {
        return this.view.getSidebar();
    }

    @Override
    public GameMode getGameMode() {
        return this.gameState.getGameMode();
    }

    @Override
    public int getNumberRound() {
        return this.gameState.getGameRound();
    }

    @Override
    public int getNumberTicketsUserPlayer(final TicketType ticketType) {
        return this.gameState.getNumberTicketsUserPlayer(ticketType);
    }

    @Override
    public void updateSidebar(Player currentPlayer) {
        final SidebarPanel sidebar = this.getSidebarPanel();
        sidebar.setGameModeLabel(this.getGameMode());
        sidebar.updateRoundLabel(this.getNumberRound());
        sidebar.updateCurrentPlayerLabel(currentPlayer);
        sidebar.updateTaxiTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.TAXI));
        sidebar.updateBusTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.BUS));
        sidebar.updateUndergroundTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.UNDERGROUND));
        sidebar.updateBlackTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.BLACK));
        sidebar.updateDoubleMoveTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.DOUBLE_MOVE));
    }

    @Override
    public boolean isGameOver() {
        return this.gameState.isGameOver();
    }

    @Override
    public void loadGameOverWindow() {
        this.view.displayGameOverWindow(this.gameState.resultGame());
    }

    @Override
    public void loadMainMenu() {
        this.mainController.loadMainMenu();
    }

    /**
     * Checks if there are multiple transport types to reach destination or not. Used only in
     * DetectiveGameControllerImpl.
     *
     * @param newPositionId the id of the destination
     */
    public abstract void destinationChosen(int newPositionId);

    /**
     * Sets the selcted transport type to reach destination. Used only in DetectiveGameControllerImpl.
     *
     * @param transportType the type of transport selected
     */
    public abstract void selectTransport(TransportType transportType);
}
