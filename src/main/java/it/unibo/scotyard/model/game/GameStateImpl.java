package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameStateImpl implements GameState {

    private final Random random;
    private GameStatus gameStatus;
    private GameMode gameMode;
    private GameDifficulty gameDifficulty;
    private Player userPlayer;
    private Player computerPlayer;
    private List<Player> additionalPlayers;

    private int round;

    public GameStateImpl(String gameMode, String levelOfDifficulty) {
        // TODO: seed
        this.random = new Random(0);
        this.additionalPlayers = new ArrayList<>();
        this.round = 0;
        this.initialize(gameMode, levelOfDifficulty);
    }

    @Override
    public void initialize(String gameMode, String levelDifficulty) {
        this.gameMode = setGameMode(gameMode);
        this.gameDifficulty = setGameDifficulty(levelDifficulty);
        this.setPlayers();
        this.setIA();
        this.round++;
    }

    @Override
    public Random getSeededRandom() {
        return this.random;
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

    private void setPlayers() {
        if (GameMode.DETECTIVE.equals((this.gameMode))) {
            this.userPlayer = new Detective();
            this.computerPlayer = new MisterX();
        }
        if (GameMode.MISTER_X.equals((this.gameMode))) {
            this.userPlayer = new MisterX();
            this.computerPlayer = new Detective();
        }
        switch (this.gameDifficulty) {
            case GameDifficulty.MEDIUM:
            case GameDifficulty.DIFFICULT:
                this.additionalPlayers.add(new Bobby());
            case GameDifficulty.EASY:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.add(new Bobby());
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
    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    @Override
    public void setGameStatus(GameStatus state) {
        this.gameStatus = state;
    }
}
