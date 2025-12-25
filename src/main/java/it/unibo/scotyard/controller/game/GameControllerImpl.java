package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import javax.swing.JPanel;

/**
 * The controller for all game related actions
 *
 */
public final class GameControllerImpl implements GameController {

    private final GameState gameState;
    private final GameView view;

    /**
     * Creates the controller
     *
     * @param gameState the game state
     * @param view the view
     */
    public GameControllerImpl(final GameState gameState, final GameView view) {
        this.gameState = gameState;
        this.view = view;
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
    public void updateSidebar() {
        final SidebarPanel sidebar = this.getSidebarPanel();
        sidebar.setGameModeLabel(this.getGameMode());
        sidebar.updateRoundLabel(this.getNumberRound());
        sidebar.updateTaxiTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.TAXI));
        sidebar.updateBusTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.BUS));
        sidebar.updateUndergroundTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.UNDERGROUND));
        sidebar.updateBlackTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.BLACK));
        sidebar.updateDoubleMoveTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.DOUBLE_MOVE));
    }
}
