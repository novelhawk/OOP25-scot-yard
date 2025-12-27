package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import java.util.Objects;
import javax.swing.JPanel;

public abstract class GameControllerImpl implements GameController {

    protected final GameState game;
    protected final GameView view;
    protected final Controller mainController;

    public GameControllerImpl(final GameState gameData, final GameView view, final Controller controller) {
        this.game = Objects.requireNonNull(gameData, "Game cannot be null");
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
        return this.game.getGameMode();
    }

    @Override
    public int getNumberRound() {
        return this.game.getGameRound();
    }

    @Override
    public int getNumberTicketsUserPlayer(TicketType ticketType) {
        return this.game.getNumberTicketsUserPlayer(ticketType);
    }

    @Override
    public void updateSidebar(Player currentPlayer) {
        SidebarPanel sidebar = this.getSidebarPanel();
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
        return this.game.isGameOver();
    }

    @Override
    public void loadGameOverWindow() {
        this.view.displayGameOverWindow(this.game.resultGame());
    }

    @Override
    public void loadMainMenu() {
        this.mainController.loadMainMenu();
    }

    /**
     * Checks if there are multiple transport types to reach destination or not.
     * Used only in
     * DetectiveGameControllerImpl.
     *
     * @param newPositionId the id of the destination
     */
    public abstract void destinationChosen(int newPositionId);

    /**
     * Sets the selcted transport type to reach destination. Used only in
     * DetectiveGameControllerImpl.
     *
     * @param transportType the type of transport selected
     */
    public abstract void selectTransport(TransportType transportType);
}
