package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;

import java.util.HashSet;

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
        this.view.displayGameOverWindow(this.game.winner());
    }

    @Override
    public void loadMainMenu() {
        this.mainController.loadMainMenu();
    }

    // TODO : this method gets called by View
    @Override
    public void movePlayer(int newPositionId) {
        TransportType transport;
        if(this.game.areMultipleTransportsAvailable(newPositionId)){
            System.out.println(this.game.getAvailableTransports(newPositionId));
            this.view.loadTransportSelectionWindow(new HashSet<>(this.game.getAvailableTransports(newPositionId)));
            while(!this.view.isTransportTypeSelected()){transport = TransportType.TAXI;}
            transport = this.view.getSelectedTransportType();

        } else{
            System.out.println(this.game.getAvailableTransports(newPositionId));
            transport = this.game.getAvailableTransports(newPositionId).getFirst();
        }
        if(this.game.moveCurrentPlayer(newPositionId, transport)){
            // TODO : update view
            this.game.changeCurrentPlayer();
            this.game.nextRound();
            this.manageGameRound();
        } 
        
    }

    @Override
    public void manageGameRound() {
        if(this.game.isGameOver()){
            this.loadGameOverWindow();
        } else{
            System.out.println("Round : " + this.game.getGameRound());
            this.updateSidebar(this.game.getCurrentPlayer());
            this.game.loadPossibleDestinations(new HashSet<>(this.mainController.getPossibleDestinations(
                this.game.getPositionPlayer(this.game.getCurrentPlayer()))));
        }        
    }
}
