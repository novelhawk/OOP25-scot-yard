package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameImpl implements Game {

    private static final int FINAL_ROUND_NUMBER = 32;
    private static final int MISTER_X_ROUND_INDEX = -2;
    private static final int DETECTIVE_ROUND_INDEX = -1;

    private GameState gameState;
    private GameMode gameMode;
    private GameDifficulty gameDifficulty;
    private Player userPlayer;
    private Player computerPlayer;
    private Player currentPlayer;
    private List<Player> additionalPlayers;
    private int playersNumber;
    private List<Integer> initialPositions;
    private List<Integer> possibleDestinations;
    private int indexCurrentPlayer; // It is used to keep track of the current player
    private int round;

    public GameImpl(String gameMode, String levelOfDifficulty, List<Integer> initialPositions) {
        this.additionalPlayers = new ArrayList<>();
        this.round = 0;
        this.playersNumber = 0;
        this.initialize(gameMode, levelOfDifficulty, initialPositions);
    }

    @Override
    public void initialize(String gameMode, String levelDifficulty, List<Integer> initialPositions) {
        this.gameMode = setGameMode(gameMode);
        this.gameDifficulty = setGameDifficulty(levelDifficulty);
        this.loadInitialPositions(initialPositions);
        this.setPlayers();
        this.setIA();
        this.round++;
        this.indexCurrentPlayer = MISTER_X_ROUND_INDEX;

        this.printTest();
    }

    private GameMode setGameMode(String inputGameMode) {
        switch (inputGameMode) {
            case "Detective":
                return GameMode.DETECTIVE;
            case "Mister X":
                return GameMode.MISTER_X;
            default:
                return GameMode.DETECTIVE;
        }
    }

    private GameDifficulty setGameDifficulty(String inputGameDifficulty) {
        switch (inputGameDifficulty) {
            case "Facile":
                return GameDifficulty.EASY;
            case "Media":
                return GameDifficulty.MEDIUM;
            case "Difficle":
                return GameDifficulty.DIFFICULT;
            default:
                return GameDifficulty.EASY;
        }
    }

    private void loadInitialPositions(List<Integer> inputList) {
        this.initialPositions = new ArrayList<Integer>(inputList);
    }

    private int getRandomInitialPosition() {
        Random rand = new Random();
        int indexList = rand.nextInt(this.initialPositions.size());
        int result = this.initialPositions.remove(indexList);
        return result;
    }

    private void setPlayers() {
        if (GameMode.DETECTIVE.equals((this.gameMode))) {
            this.userPlayer = new Detective();
            this.computerPlayer = new MisterX();
            this.currentPlayer = this.computerPlayer;
        }
        if (GameMode.MISTER_X.equals((this.gameMode))) {
            this.userPlayer = new MisterX();
            this.computerPlayer = new Detective();
            this.currentPlayer = this.userPlayer;
        }
        this.playersNumber++;
        this.playersNumber++;
        this.userPlayer.setInitialPosition(this.getRandomInitialPosition());
        this.computerPlayer.setInitialPosition(this.getRandomInitialPosition());
        int index = 0;
        switch (this.gameDifficulty) {
            case GameDifficulty.MEDIUM:
            case GameDifficulty.DIFFICULT:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setInitialPosition(this.getRandomInitialPosition());
                this.playersNumber++;
                index++;
            case GameDifficulty.EASY:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setInitialPosition(this.getRandomInitialPosition());
                index++;
                this.playersNumber++;
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setInitialPosition(this.getRandomInitialPosition());
                index++;
                this.playersNumber++;
        }
    }

    private void setIA() {
        // TODO : set IA according to the level of difficulty.
        /*
         * EASY : easy IA (random)
         * MEDIUM : medium IA
         * DIFFICULT : difficult IA
         */
    }

    private void printTest() {
        System.out.println("User : " + this.getPositionPlayer(this.userPlayer));
        System.out.println("IA : " + this.getPositionPlayer(this.computerPlayer));
        for (Player additional : this.additionalPlayers) {
            System.out.println("Bobby : " + this.getPositionPlayer(additional));
        }

        System.out.println("GAMEOVER?");
        System.out.println(this.isGameOver());
    }

    @Override
    public boolean isGameOver() {
        if (this.userPlayer.getCurrentPositionId() == this.computerPlayer.getCurrentPositionId()
                || this.round > FINAL_ROUND_NUMBER) {
            return true;
        }
        for (Player bobby : this.additionalPlayers) {
            if (this.computerPlayer.getCurrentPositionId() == (bobby.getCurrentPositionId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadPossibleDestinations(List<Integer> inputPossibleDestinations){
        this.possibleDestinations = inputPossibleDestinations;

        System.out.println(this.possibleDestinations);
    }

    @Override
    public void continueGame() {
        this.indexCurrentPlayer++;
        if(this.indexCurrentPlayer<this.additionalPlayers.size()){
            if(this.indexCurrentPlayer>=0){
                this.currentPlayer = additionalPlayers.get(this.indexCurrentPlayer);
            } else{
                if(this.indexCurrentPlayer==DETECTIVE_ROUND_INDEX){
                    if(this.gameMode.equals(GameMode.DETECTIVE)){
                            this.currentPlayer = this.userPlayer;
                        } else{
                            this.currentPlayer = this.computerPlayer;
                        }
                }
            }
        } else {
            this.indexCurrentPlayer = MISTER_X_ROUND_INDEX;
            if(this.gameMode.equals(GameMode.DETECTIVE)){
                this.currentPlayer = this.computerPlayer;
            } else{
                this.currentPlayer = this.userPlayer;
            }
        }
    }

    @Override
    public void nextRound(){
        this.round++;
    }

    @Override
    public GameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public int getNumberTicketsUserPlayer(TicketType ticketType) {
        return this.getNumberTickets(this.userPlayer, ticketType);
    }

    private int getNumberTickets(Player player, TicketType ticketType) {
        return player.getNumberTickets(ticketType);
    }

    @Override
    public int getGameRound() {
        return this.round;
    }

    @Override
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    @Override
    public int getPositionPlayer(Player player) {
        return player.getCurrentPositionId();
    }

    @Override
    public int getNumberOfPlayers(){
        return this.playersNumber;
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
