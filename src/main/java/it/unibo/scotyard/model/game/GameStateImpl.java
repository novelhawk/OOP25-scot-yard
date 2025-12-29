package it.unibo.scotyard.model.game;

import it.unibo.scotyard.commons.Constants;
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

/**
 * The game state.
 *
 */
public final class GameStateImpl implements GameState {

    private static final int MISTER_X_ROUND_INDEX = -2;
    private static final int DETECTIVE_ROUND_INDEX = -1;

    private final Random random;
    private GameStatus gameStatus;
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

    /**
     * Creates a new game state.
     *
     * @param gameMode the game mode
     * @param difficultyLevel the difficulty level
     * @param initialPositions the initial positions
     */
    public GameStateImpl(String gameMode, String difficultyLevel, List<Integer> initialPositions) {
        // TODO: seed
        this.random = new Random(0);
        this.additionalPlayers = new ArrayList<>();
        this.round = 0;
        this.playersNumber = 0;
        this.availableTransports = new ArrayList<TransportType>();
        this.possibleDestinations = new HashSet<Pair<Integer, TransportType>>();
        this.initialize(gameMode, difficultyLevel, initialPositions);
    }

    @Override
    public void initialize(final String gameMode, final String levelDifficulty, final List<Integer> initialPositions) {
        this.gameMode = setGameMode(gameMode);
        this.gameDifficulty = setGameDifficulty(levelDifficulty);
        this.loadInitialPositions(initialPositions);
        this.setPlayers();
        this.setIA();
        this.round++;
        this.indexCurrentPlayer = MISTER_X_ROUND_INDEX;
        this.setGameStatus(GameStatus.PLAYING);
    }

    @Override
    public Random getSeededRandom() {
        return this.random;
    }

    private GameMode setGameMode(final String inputGameMode) {
        switch (inputGameMode) {
            case "Detective":
                return GameMode.DETECTIVE;
            case "Mister X":
                return GameMode.MISTER_X;
            default:
                return GameMode.DETECTIVE;
        }
    }

    private GameDifficulty setGameDifficulty(final String difficultyLevel) {
        switch (difficultyLevel) {
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
        this.userPlayer.setCurrentPosition(this.getRandomInitialPosition());
        this.computerPlayer.setCurrentPosition(this.getRandomInitialPosition());
        int index = 0;
        switch (this.gameDifficulty) {
            case MEDIUM:
            case DIFFICULT:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setCurrentPosition(this.getRandomInitialPosition());
                index++;
                this.additionalPlayers.get(index - 1).setName("Bobby" + index);
                this.playersNumber++;
            case EASY:
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setCurrentPosition(this.getRandomInitialPosition());
                index++;
                this.additionalPlayers.get(index - 1).setName("Bobby" + index);
                this.playersNumber++;
                this.additionalPlayers.add(new Bobby());
                this.additionalPlayers.get(index).setCurrentPosition(this.getRandomInitialPosition());
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
                || this.round > Constants.FINAL_ROUND_NUMBER) {
            this.setGameState(GameStatus.PAUSE);
            return true;
        }
        for (Player bobby : this.additionalPlayers) {
            if ((this.userPlayer.getCurrentPositionId() == (bobby.getCurrentPositionId())
                            && GameMode.MISTER_X.equals(this.gameMode))
                    || (this.computerPlayer.getCurrentPositionId() == (bobby.getCurrentPositionId())
                            && GameMode.DETECTIVE.equals(this.gameMode))) {
                this.setGameState(GameStatus.PAUSE);
                return true;
            }
        }
        if (GameMode.DETECTIVE.equals(this.gameMode)) {
            if (this.possibleDestinations.isEmpty() && this.getGameRound() > 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String resultGame() {
        String victoryString = new String("Vittoria");
        String lossString = new String("Sconfitta");
        for (Player bobby : this.additionalPlayers) {
            if (this.userPlayer.getCurrentPositionId() == (bobby.getCurrentPositionId())) {
                if (GameMode.MISTER_X.equals(this.gameMode)) {
                    return lossString;
                } else {
                    return victoryString;
                }
            } else {
                if (GameMode.DETECTIVE.equals(this.gameMode)
                        && this.computerPlayer.getCurrentPositionId() == bobby.getCurrentPositionId()) {
                    return victoryString;
                }
            }
        }
        if (this.userPlayer.getCurrentPositionId() == this.computerPlayer.getCurrentPositionId()) {
            if (GameMode.MISTER_X.equals(this.gameMode)) {
                return lossString;
            } else {
                return victoryString;
            }
        } else {
            if (GameMode.DETECTIVE.equals(this.gameMode)) {
                return lossString;
            } else {
                return victoryString;
            }
        }
    }

    @Override
    public Set<Pair<Integer, TransportType>> loadPossibleDestinations(
            Set<Pair<Integer, TransportType>> inputPossibleDestinations) {
        this.possibleDestinations.clear();

        /*
         * Updating the variable possibleDestinations with the possible destinations
         * given as input,
         * with the removal of destinations in which other players are present :
         * - Mister X can't go where the detective and bobbies are
         * - Detective can't go where other bobbies are
         * - Bobbies can't go where detective is
         */
        for (Pair<Integer, TransportType> destination : inputPossibleDestinations) {
            int pos = destination.getX();
            this.possibleDestinations.add(destination);
            /* Mister X can't go where detective is. */
            if (GameMode.MISTER_X.equals(this.gameMode)
                    && this.currentPlayer.equals(this.userPlayer)
                    && pos == this.computerPlayer.getCurrentPositionId()) {
                this.possibleDestinations.remove(destination);
            }
            /* Mister X can't go where detective is. */
            if (GameMode.DETECTIVE.equals(this.gameMode)
                    && this.currentPlayer.equals(this.computerPlayer)
                    && pos == this.userPlayer.getCurrentPositionId()) {
                this.possibleDestinations.remove(destination);
            }
            /*
             * No player can go where other bobbies are.
             * Bobbies can't go where detective is.
             */
            for (Player bobby : this.additionalPlayers) {
                if (bobby.getCurrentPositionId() == pos
                        || (GameMode.DETECTIVE.equals(this.gameMode)
                                && this.currentPlayer.equals(bobby)
                                && pos == this.userPlayer.getCurrentPositionId())) {
                    this.possibleDestinations.remove(destination);
                }
            }
            // Removal of destinations that can be reached by ferry, if player is not Mister
            // X
            if ((GameMode.DETECTIVE.equals(this.gameMode) && this.currentPlayer != this.computerPlayer)
                    || (GameMode.MISTER_X.equals(this.gameMode) && this.currentPlayer != this.userPlayer)) {
                this.possibleDestinations.removeIf(item -> TransportType.FERRY.equals(item.getY()));
            }
        }

        return this.possibleDestinations;
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

    @Override
    public boolean moveCurrentPlayer(int destinationId, TransportType transport) {
        if (this.possibleDestinations.contains(new Pair<>(destinationId, transport))) {
            this.setPositionPlayer(this.currentPlayer, destinationId);
            this.currentPlayer.useTicket(Player.getTicketTypeForTransport(transport));
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
    public boolean hideMisterX() {
        if (GameMode.DETECTIVE.equals(this.gameMode)) {
            if (Constants.REVEAL_TURNS_MISTER_X.contains(this.getGameRound())
                    && this.currentPlayer.equals(this.computerPlayer)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public GameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public int getNumberTicketsUserPlayer(final TicketType ticketType) {
        return this.getNumberTickets(this.userPlayer, ticketType);
    }

    private int getNumberTickets(final Player player, final TicketType ticketType) {
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
    public Player getUserPlayer() {
        return this.userPlayer;
    }

    @Override
    public Player getComputerPlayer() {
        return this.computerPlayer;
    }

    @Override
    public int getNumberOfPlayers() {
        return this.playersNumber;
    }

    @Override
    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    @Override
    public void setGameStatus(final GameStatus state) {
        this.gameStatus = state;
    }

    @Override
    public List<Player> getBobbies() {
        return new ArrayList<>(additionalPlayers);
    }

    @Override
    public Player getDetective() {
        // In MISTER_X mode, Detective is the computer player
        // In DETECTIVE mode, Detective is the user player
        if (gameMode == GameMode.MISTER_X) {
            return computerPlayer;
        } else {
            return userPlayer;
        }
    }
}
