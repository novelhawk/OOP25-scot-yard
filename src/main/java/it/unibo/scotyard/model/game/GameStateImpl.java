package it.unibo.scotyard.model.game;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.entities.RunnerTurnTrackerImpl;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The game state.
 *
 */
public final class GameStateImpl implements GameState {

    private static final int NOT_REVEALED_YET = -1;

    private GameStatus gameStatus;
    private GameMode gameMode;
    private GameDifficulty gameDifficulty;

    /**
     * It is used to keep track of the current player in the turn order.
     */
    private int indexCurrentPlayer;

    private final Players players;

    private final Set<Pair<NodeId, TransportType>> possibleDestinations; // They refer to the current player
    private final List<TransportType> availableTransports;

    private TurnState turnState;
    private final RunnerTurnTrackerImpl runnerTurnTracker;

    private int round;

    private NodeId lastRevealedMisterXPosition;

    /**
     * Creates a new game state.
     *
     * @param gameMode the game mode
     * @param gameDifficulty the difficulty level
     * @param players the involved players
     */
    public GameStateImpl(GameMode gameMode, GameDifficulty gameDifficulty, Players players) {
        this.round = 1;
        this.availableTransports = new ArrayList<>();
        this.possibleDestinations = new HashSet<>();
        this.runnerTurnTracker = new RunnerTurnTrackerImpl();
        this.turnState = new TurnState();
        this.players = players;
        this.gameMode = gameMode;
        this.gameDifficulty = gameDifficulty;
        this.gameStatus = GameStatus.PLAYING;
        lastRevealedMisterXPosition = new NodeId(NOT_REVEALED_YET);
    }

    @Override
    public boolean isGameOver() {
        final NodeId runnerPosition = this.players.getMisterX().getPosition();
        final boolean found =
                this.players.getSeekers().anyMatch(it -> it.getPosition().equals(runnerPosition));

        if (found || this.round > Constants.FINAL_ROUND_NUMBER) {
            this.setGameStatus(GameStatus.PAUSE);
            return true;
        }

        if (GameMode.DETECTIVE.equals(this.gameMode)) {
            return this.getGameRound() > 1 && this.possibleDestinations.isEmpty();
        }

        return found;
    }

    @Override
    public String resultGame() {
        final String victoryString = "Vittoria";
        final String lossString = "Sconfitta";

        final NodeId runnerPosition = this.players.getMisterX().getPosition();
        final boolean found =
                this.players.getSeekers().anyMatch(it -> it.getPosition().equals(runnerPosition));

        if (found) {
            if (this.gameMode == GameMode.MISTER_X) {
                return lossString;
            } else {
                return victoryString;
            }
        } else {
            if (this.gameMode == GameMode.MISTER_X) {
                return victoryString;
            } else {
                return lossString;
            }
        }
    }

    @Override
    public Set<Pair<NodeId, TransportType>> loadPossibleDestinations(
            Set<Pair<NodeId, TransportType>> inputPossibleDestinations) {
        this.possibleDestinations.clear();

        /*
         * Updating the variable possibleDestinations with the possible destinations
         * given as input,
         * with the removal of destinations in which other players are present :
         * - Mister X can't go where the detective and bobbies are
         * - Detective can't go where other bobbies are
         * - Bobbies can't go where detective is
         */
        for (Pair<NodeId, TransportType> destination : inputPossibleDestinations) {
            final NodeId pos = destination.getX();
            final TransportType transport = destination.getY();
            this.possibleDestinations.add(destination);
            /* Mister X can't go where detective is. */
            if (this.gameMode == GameMode.MISTER_X
                    && this.getCurrentPlayer().equals(this.players.getUserPlayer())
                    && pos == this.players.getComputerPlayer().getPosition()) {
                this.possibleDestinations.remove(destination);
            }
            /* Mister X can't go where detective is. */
            if (this.gameMode == GameMode.DETECTIVE
                    && this.getCurrentPlayer().equals(this.players.getComputerPlayer())
                    && pos == this.players.getUserPlayer().getPosition()) {
                this.possibleDestinations.remove(destination);
            }
            /*
             * No player can go where other bobbies are.
             * Bobbies can't go where detective is.
             */
            for (Player bobby : this.players.getBobbies()) {
                if (bobby.getPosition().equals(pos)
                        || (this.gameMode == GameMode.DETECTIVE
                                && this.getCurrentPlayer().equals(bobby)
                                && pos == this.players.getUserPlayer().getPosition())) {
                    this.possibleDestinations.remove(destination);
                }
            }
            // Removal of destinations that can be reached by ferry, if player isn't Mister X
            if ((GameMode.DETECTIVE.equals(this.gameMode)
                            && this.getCurrentPlayer() != this.players.getComputerPlayer())
                    || (GameMode.MISTER_X.equals(this.gameMode)
                            && this.getCurrentPlayer() != this.players.getUserPlayer())) {
                this.possibleDestinations.removeIf(item -> TransportType.FERRY.equals(item.getY()));
            }
            // Removal of destinations for which current player has no tickets
            if (this.getCurrentPlayer().getNumberTickets(Player.getTicketTypeForTransport(transport)) == 0) {
                this.possibleDestinations.remove(destination);
            }
        }

        return this.possibleDestinations;
    }

    @Override
    public Set<Pair<NodeId, TransportType>> getPossibleDestinations() {
        return this.possibleDestinations;
    }

    @Override
    public void changeCurrentPlayer() {
        indexCurrentPlayer = (indexCurrentPlayer + 1) % players.getPlayersCount();
    }

    private void loadAvailableTransports(NodeId destinationId) {
        this.availableTransports.clear();
        for (Pair<NodeId, TransportType> item : this.possibleDestinations) {
            if (item.getX().equals(destinationId)) {
                this.availableTransports.add(item.getY());
            }
        }
    }

    @Override
    public boolean areMultipleTransportsAvailable(NodeId destinationId) {
        this.loadAvailableTransports(destinationId);
        return this.availableTransports.size() > 1;
    }

    @Override
    public List<TransportType> getAvailableTransports(NodeId destinationId) {
        return this.availableTransports;
    }

    @Override
    public boolean isMovableCurrentPlayer(NodeId destinationId, TransportType transport) {
        if (this.possibleDestinations.contains(new Pair<>(destinationId, transport))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void moveCurrentPlayer(NodeId destinationId, TransportType transport) {
        this.getCurrentPlayer().setPosition(destinationId);
        this.getCurrentPlayer().useTicket(Player.getTicketTypeForTransport(transport));
    }

    private void incrementsRound() {
        this.round++;
    }

    @Override
    public void nextRound() {
        if (this.gameMode == GameMode.DETECTIVE) {
            if (this.getCurrentPlayer().equals(this.players.getComputerPlayer())) {
                this.incrementsRound();
            }
        } else {
            if (this.getCurrentPlayer().equals(this.players.getUserPlayer())) {
                this.incrementsRound();
            }
        }
    }

    @Override
    public boolean hideMisterX() {
        if (this.gameMode == GameMode.DETECTIVE) {
            return !Constants.REVEAL_TURNS_MISTER_X.contains(this.getGameRound());
        } else {
            return false;
        }
    }

    private void setLastRevealedMisterXPosition() {
        boolean reveal = Constants.REVEAL_TURNS_MISTER_X.contains(this.getGameRound());
        if (reveal) {
            if (this.gameMode == GameMode.MISTER_X) {
                this.lastRevealedMisterXPosition = this.getUserPlayer().getPosition();
            } else {
                this.lastRevealedMisterXPosition = this.getComputerPlayer().getPosition();
            }
        }
    }

    @Override
    public NodeId getLastRevealedMisterXPosition() {
        this.setLastRevealedMisterXPosition();
        return this.lastRevealedMisterXPosition;
    }

    @Override
    public GameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public int getNumberTicketsUserPlayer(final TicketType ticketType) {
        return this.getNumberTickets(this.players.getUserPlayer(), ticketType);
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
        return this.players.getTurnOrder().get(indexCurrentPlayer);
    }

    @Override
    public NodeId getPositionPlayer(Player player) {
        return player.getPosition();
    }

    @Override
    public Player getUserPlayer() {
        return this.players.getUserPlayer();
    }

    @Override
    public Player getComputerPlayer() {
        return this.players.getComputerPlayer();
    }

    @Override
    public int getNumberOfPlayers() {
        return this.players.getPlayersCount();
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
    public GameDifficulty getGameDifficulty() {
        return this.gameDifficulty;
    }

    @Override
    public List<Bobby> getBobbies() {
        return this.players.getBobbies();
    }

    @Override
    public Player getDetective() {
        return this.players.getDetective();
    }

    @Override
    public void resetTurn() {
        this.turnState = new TurnState();
    }

    @Override
    public TurnState getTurnState() {
        return this.turnState;
    }

    @Override
    public RunnerTurnTrackerImpl getRunnerTurnTracker() {
        return runnerTurnTracker;
    }
}
