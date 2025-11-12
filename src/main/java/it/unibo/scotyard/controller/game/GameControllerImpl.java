package it.unibo.scotyard.controller.game;

import javax.swing.JPanel;

import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.players.TicketType;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;

public class GameControllerImpl implements GameController {
    
    private Game game;
    private GameView view;

    public GameControllerImpl(Game gameData, GameView view){
        this.game = gameData;
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
    public GameMode getGameMode(){
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
    public void updateSidebar(){
        this.view.getSidebar().setGameModeLabel(this.getGameMode());
        this.view.getSidebar().updateRoundLabel(this.getNumberRound());
        this.view.getSidebar().updateTaxiTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.TAXI));
        this.view.getSidebar().updateBusTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.BUS));
        this.view.getSidebar().updateUndergroundTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.UNDERGROUND));
        this.view.getSidebar().updateBlackTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.BLACK));
        this.view.getSidebar().updateDoubleMoveTicketsLabel(this.getNumberTicketsUserPlayer(TicketType.DOUBLE_MOVE));
    }

}
