package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;

import java.util.List;

import javax.swing.JPanel;

public class GameControllerImpl implements GameController {

    private Game game;
    private GameView view;

    private Controller mainController;

    public GameControllerImpl(Game gameData, GameView view, Controller mainController) {
        this.game = gameData;
        this.view = view;

        this.mainController = mainController;
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
    public void updateSidebar() {
        SidebarPanel sidebar = this.getSidebarPanel();
        sidebar.setGameModeLabel(this.getGameMode());
        sidebar.updateRoundLabel(this.getNumberRound());
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
        this.view.displayGameOverWindow();
    }

    @Override
    public void loadMainMenu() {
        this.mainController.loadMainMenu();
    }

    @Override
    public void manageGameRound() {
        System.out.println("Round : " + this.game.getGameRound());
        for(int i=0; i<this.game.getNumberOfPlayers(); i++){
            this.game.loadPossibleDestinations(this.mainController.getPossibleDestinations(this.game.getPositionPlayer(this.game.getCurrentPlayer())));
            this.game.continueGame();
        }
        this.game.nextRound();
    }
}
