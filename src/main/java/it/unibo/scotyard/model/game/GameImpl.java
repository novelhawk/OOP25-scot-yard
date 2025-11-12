package it.unibo.scotyard.model.game;

import java.util.ArrayList;
import java.util.List;

import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;

public class GameImpl implements Game {

    private GameState gameState;
    private GameMode gameMode;
    private GameDifficulty gameDifficulty;
    private Player userPlayer;
    private Player computerPlayer;
    private List<Player> additionalPlayers;

    private int round;


    public GameImpl(String gameMode, String levelOfDifficulty) {
        this.initialize(gameMode, levelOfDifficulty);
        this.additionalPlayers = new ArrayList<>();
        this.round = 0;
    }

    @Override
    public void initialize(String gameMode, String levelDifficulty){
        this.gameMode = setGameMode(gameMode);
        this.gameDifficulty = setGameDifficulty(levelDifficulty);
        this.setPlayers();
        this.setIA();
    }

    private GameMode setGameMode(String inputGameMode){
        switch(inputGameMode) {
            case "Detective" : 
                return GameMode.DETECTIVE;
            case "Mister X" :
                return GameMode.MISTER_X;
            default : 
                return GameMode.DETECTIVE; //TOMODIFY
        }
    }

    private GameDifficulty setGameDifficulty(String inputGameDifficulty){
        switch(inputGameDifficulty){
            case "Facile" : 
                return GameDifficulty.EASY;
            case "Media" :
                return GameDifficulty.MEDIUM;
            case "Difficle" : 
                return GameDifficulty.DIFFICULT;
            default :
                return GameDifficulty.EASY; //TOMODIFY
        }
    }

    private void setPlayers(){
        if((this.gameMode).equals(GameMode.DETECTIVE)){
            this.userPlayer = new Detective();
            this.computerPlayer = new MisterX();
        } 
        if((this.gameMode).equals(GameMode.MISTER_X)){
            this.userPlayer = new MisterX();
            this.computerPlayer = new Detective();
        }
        switch(this.gameDifficulty){
            case GameDifficulty.MEDIUM :
            case GameDifficulty.DIFFICULT : 
                this.additionalPlayers.add(new Bobby());
            case GameDifficulty.EASY : 
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.add(new Bobby());        
        }
    }

    private void setIA(){
        //TODO : set IA according to the level of difficulty.
        /*
         * EASY : easy IA
         * MEDIUM : easy IA
         * DIFFICULT : difficult IA
         */
    }

    @Override
    public int getNumberTickets(Player player, TicketType ticketType){
        return player.getNumberTickets(ticketType);
    }

    @Override
    public int getGameRound(){
        return this.round;
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void setGameState(GameState state) {
        this.gameState = state;
    }

}
