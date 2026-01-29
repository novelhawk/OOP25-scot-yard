package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.entities.ExposedPosition;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.GameStateSubscriber;
import it.unibo.scotyard.model.game.record.JsonRecordRepository;
import it.unibo.scotyard.model.game.record.RecordRepository;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.model.router.CommandDispatcher;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.swing.*;

/**
 * The controller for all game related actions
 *
 */
public abstract class GameControllerImpl implements GameController, GameStateSubscriber {

    protected static final NodeId HIDDEN_POSITION = new NodeId(-1);

    protected final CommandDispatcher dispatcher;
    protected final GameState gameState;
    protected final GameView view;
    protected final Controller mainController;

    private final RecordRepository recordRepository;

    /**
     * Creates the controller
     *
     * @param gameState  the game state
     * @param view       the view
     * @param controller the controller
     */
    public GameControllerImpl(
            final CommandDispatcher dispatcher,
            final GameState gameState,
            final GameView view,
            final Controller controller) {
        this.dispatcher = Objects.requireNonNull(dispatcher, "Dispatcher cannot be null");
        this.gameState = Objects.requireNonNull(gameState, "Game cannot be null");
        this.view = Objects.requireNonNull(view, "GameView cannot be null");
        this.mainController = Objects.requireNonNull(controller, "mainController cannot be null");
        try {
            this.recordRepository = JsonRecordRepository.initialize();
        } catch (final IOException e) {
            throw new RuntimeException("Cannot init record repo", e);
        }
    }

    @Override
    public void initializeGame() {
        this.view.getTrackerPanel().createGrid(gameState.maxRoundCount());
        this.gameState.getRunnerTurnTracker().subscribe(this::syncRunnerTurns);

        this.gameState.subscribe(this);
    }

    private void syncRunnerTurns(final List<List<TransportType>> turns) {
        for (int i = 0; i < turns.size(); i++) {
            this.view.getTrackerPanel().setTransportModes(i, turns.get(i));
        }
    }

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
    public void updateSidebar(final Player currentPlayer) {
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
        final String result = this.gameState.resultGame();

        // recupera dati dal model
        final long gameDuration = this.gameState.getGameDuration();
        final GameMode gameMode = this.gameState.getGameMode();
        final boolean isNewRecord = this.recordRepository.updateIfBetter(gameMode, gameDuration);
        final String currentRecord = recordRepository.getFormattedDuration(gameMode);

        // passa dati alla view
        this.view.displayGameOverWindow(result, gameDuration, gameMode, isNewRecord, currentRecord);
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
    @Override
    public abstract void destinationChosen(NodeId newPositionId);

    /**
     * Sets the selected transport type to reach destination. Used only in
     * DetectiveGameControllerImpl.
     *
     * @param transportType the type of transport selected
     */
    @Override
    public abstract void selectTransport(TransportType transportType);

    @Override
    public void onExposedPosition(ExposedPosition exposedPosition) {
        this.view.getMapPanel().setMisterXPosition(exposedPosition.position());
        SwingUtilities.invokeLater(() -> this.view.getMapPanel().repaint());
    }

    @Override
    public void onRunnerHidden() {
        this.view.getMapPanel().setMisterXPosition(HIDDEN_POSITION);
        SwingUtilities.invokeLater(() -> this.view.getMapPanel().repaint());
    }
}
