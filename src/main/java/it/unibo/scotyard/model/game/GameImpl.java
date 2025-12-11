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

    private GameState gameState;
    private GameMode gameMode;
    private GameDifficulty gameDifficulty;
    private Player userPlayer;
    private Player computerPlayer;
    private List<Player> additionalPlayers;
    List<Integer> initialPositions;

    private int round;

    public GameImpl(String gameMode, String levelOfDifficulty, List<Integer> initialPositions) {
        this.additionalPlayers = new ArrayList<>();
        this.round = 0;
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
        }
        if (GameMode.MISTER_X.equals((this.gameMode))) {
            this.userPlayer = new MisterX();
            this.computerPlayer = new Detective();
        }
        this.userPlayer.setInitialPosition(this.getRandomInitialPosition());
        this.computerPlayer.setInitialPosition(this.getRandomInitialPosition());
        int index = 0;
        switch (this.gameDifficulty) {
            case GameDifficulty.MEDIUM:
            case GameDifficulty.DIFFICULT:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setInitialPosition(this.getRandomInitialPosition());
                index++;
            case GameDifficulty.EASY:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setInitialPosition(this.getRandomInitialPosition());
                index++;
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setInitialPosition(this.getRandomInitialPosition());
                index++;
        }
    }

    private void setIA() {
        // TODO : set IA according to the level of difficulty.
        /*
         * EASY : easy IA
         * MEDIUM : easy IA
         * DIFFICULT : difficult IA
         */
    }

    public void printTest() {
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
    public void continueGame() {
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
    public int getPositionPlayer(Player player) {
        return player.getCurrentPositionId();
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
