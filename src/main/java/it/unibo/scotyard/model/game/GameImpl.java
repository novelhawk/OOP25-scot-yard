package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameImpl implements Game {

    private static final int FINAL_ROUND_NUMBER = 32;
    private static final int MISTER_X_ROUND_INDEX = -2;
    private static final int DETECTIVE_ROUND_INDEX = -1;

    private GameState gameState;
    private GameMode gameMode;
    private GameDifficulty gameDifficulty;

    private Player userPlayer;
    private Player computerPlayer;
    private List<Player> additionalPlayers; // bobbies
    private int playersNumber;
    private Player currentPlayer;

    private List<Integer> initialPositions;
    private Set<Pair<Integer, TransportType>> possibleDestinations; // They refer to the current player
    private List<TransportType> availableTransports;
    private int indexCurrentPlayer; // It is used to keep track of the current player

    private int round;

    public GameImpl(String gameMode, String levelOfDifficulty, List<Integer> initialPositions) {
        this.additionalPlayers = new ArrayList<>();
        this.round = 0;
        this.playersNumber = 0;
        this.availableTransports = new ArrayList<TransportType>();
        this.possibleDestinations = new HashSet<Pair<Integer,TransportType>>();
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
        this.setGameState(GameState.PLAYING);
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
        this.userPlayer.setPosition(this.getRandomInitialPosition());
        this.computerPlayer.setPosition(this.getRandomInitialPosition());
        int index = 0;
        switch (this.gameDifficulty) {
            case GameDifficulty.MEDIUM:
            case GameDifficulty.DIFFICULT:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setPosition(this.getRandomInitialPosition());
                this.playersNumber++;
                index++;
                this.additionalPlayers.get(index - 1).setName("Bobby" + index);
            case GameDifficulty.EASY:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setPosition(this.getRandomInitialPosition());
                index++;
                this.additionalPlayers.get(index - 1).setName("Bobby" + index);
                this.playersNumber++;
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setPosition(this.getRandomInitialPosition());
                index++;
                this.additionalPlayers.get(index - 1).setName("Bobby" + index);
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

    @Override
    public boolean isGameOver() {
        if (this.userPlayer.getCurrentPositionId() == this.computerPlayer.getCurrentPositionId()
                || this.round > FINAL_ROUND_NUMBER) {
            this.setGameState(GameState.PAUSE);
            return true;
        }
        for (Player bobby : this.additionalPlayers) {
            if (this.computerPlayer.getCurrentPositionId() == (bobby.getCurrentPositionId())
                    && GameMode.MISTER_X.equals(this.gameMode)) {
                this.setGameState(GameState.PAUSE);
                return true;
            }
        }
        if(this.possibleDestinations.isEmpty() && this.getGameRound()>1){
            return true;
        }
        return false;
    }

    @Override
    public String resultGame() {
        for (Player bobby : this.additionalPlayers) {
            if (this.computerPlayer.getCurrentPositionId() == (bobby.getCurrentPositionId())
                    && GameMode.MISTER_X.equals(this.gameMode)) {
                return new String("Sconfitta");
            }
        }
        if (this.userPlayer.getCurrentPositionId() == this.computerPlayer.getCurrentPositionId()) {
            return new String("Sconfitta");
        }
        return new String("Vittoria");
    }

    @Override
    public void loadPossibleDestinations(Set<Pair<Integer, TransportType>> inputPossibleDestinations) {
        this.possibleDestinations = inputPossibleDestinations;

        // Removal of destinations that can be reached by ferry, if player is not Mister X
        if ((GameMode.DETECTIVE.equals(this.gameMode) && this.currentPlayer != this.computerPlayer)
                || (GameMode.MISTER_X.equals(this.gameMode) && this.currentPlayer != this.userPlayer)) {
            this.possibleDestinations.removeIf(item -> TransportType.FERRY.equals(item.getY()));
        }

        /*  Removal of destinations in which other players are present :
         * - Mister X can't go where the detective and bobbies are
         * - Detective can't go where other bobbies are
         * - Bobbies can't go where detective is
         */
        for (Pair<Integer, TransportType> destination : this.possibleDestinations) {
            int pos = destination.getX();
            if (GameMode.MISTER_X.equals(this.gameMode) && pos == this.computerPlayer.getCurrentPositionId()) {
                this.possibleDestinations.remove(destination);
            }
            for (Player bobby : this.additionalPlayers) {
                if (bobby.getCurrentPositionId() == pos
                        || (GameMode.DETECTIVE.equals(this.gameMode)
                                && this.currentPlayer.equals(bobby)
                                && pos == this.userPlayer.getCurrentPositionId())) {
                    this.possibleDestinations.remove(destination);
                }
            }
            // Removal of destinations for which current player has 0 tickets
            if(this.getCurrentPlayer().getNumberTickets(this.getTicketType(destination.getY()))==0){
                this.possibleDestinations.remove(destination);
            }
        }

        // Test
        // TODO: eliminare (una volta finita gestione turni)
        System.out.println(this.currentPlayer);
        for (Pair<Integer, TransportType> item : this.possibleDestinations) {
            System.out.print(item.getX());
            switch (item.getY()) {
                case TransportType.UNDERGROUND:
                    System.out.print(" U, ");
                    break;
                case TransportType.TAXI:
                    System.out.print(" T, ");
                    break;
                case TransportType.BUS:
                    System.out.print(" B, ");
                    break;
                case TransportType.FERRY:
                    System.out.print(" F, ");
                    break;
            }
        }
        System.out.println("Fine");
    }

    @Override
    public Set<Pair<Integer, TransportType>> getPossibleDestinations() {
        return this.possibleDestinations;
    }

    @Override
    public void changeCurrentPlayer() {
        this.indexCurrentPlayer++;
        if (this.indexCurrentPlayer < this.additionalPlayers.size()) {
            if (this.indexCurrentPlayer >= 0) {
                this.currentPlayer = additionalPlayers.get(this.indexCurrentPlayer);
            } else {
                if (this.indexCurrentPlayer == DETECTIVE_ROUND_INDEX) {
                    if (GameMode.DETECTIVE.equals(this.gameMode)) {
                        this.currentPlayer = this.userPlayer;
                    } else {
                        this.currentPlayer = this.computerPlayer;
                    }
                }
            }
        } else {
            this.indexCurrentPlayer = MISTER_X_ROUND_INDEX;
            if (GameMode.DETECTIVE.equals(this.gameMode)) {
                this.currentPlayer = this.computerPlayer;
            } else {
                this.currentPlayer = this.userPlayer;
            }
        }
    }

    private void loadAvailableTransports(int destinationId) {
        this.availableTransports.clear();
        for (Pair<Integer, TransportType> item : this.possibleDestinations) {
            if (item.getX() == destinationId) {
                this.availableTransports.add(item.getY());
            }
        }
    }

    @Override
    public boolean areMultipleTransportsAvailable(int destinationId) {
        this.loadAvailableTransports(destinationId);
        if (this.availableTransports.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<TransportType> getAvailableTransports(int destinationId) {
        return this.availableTransports;
    }

    private void setPositionPlayer(Player player, int newPositionId) {
        player.setPosition(newPositionId);
    }

    private TicketType getTicketType(TransportType transport) {
        switch (transport) {
            case TAXI:
                return TicketType.TAXI;
            case BUS:
                return TicketType.BUS;
            case UNDERGROUND:
                return TicketType.UNDERGROUND;
            case FERRY:
                return TicketType.BLACK;
        }
        return null; // TODEFINE
    }

    @Override
    public boolean moveCurrentPlayer(int destinationId, TransportType transport) {
        if (this.possibleDestinations.contains(new Pair<>(destinationId, transport))) {
            this.setPositionPlayer(this.currentPlayer, destinationId);
            this.currentPlayer.useTicket(this.getTicketType(transport));
            return true;
        } else {
            return false;
        }
    }

    private void incrementsRound() {
        this.round++;
    }

    @Override
    public void nextRound() {
        if (GameMode.DETECTIVE.equals(this.gameMode)) {
            if (this.currentPlayer.equals(this.computerPlayer)) {
                this.incrementsRound();
            }
        } else {
            if (this.currentPlayer.equals(this.userPlayer)) {
                this.incrementsRound();
            }
        }
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
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public int getPositionPlayer(Player player) {
        return player.getCurrentPositionId();
    }

    @Override
    public int getNumberOfPlayers() {
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
