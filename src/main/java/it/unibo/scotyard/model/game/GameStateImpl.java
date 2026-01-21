package it.unibo.scotyard.model.game;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.entities.ExposedPosition;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.entities.RunnerTurnTrackerImpl;
import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The game state.
 *
 */
public final class GameStateImpl implements GameState {

    private static final int ROUND_COUNT = 24;

    private final Random random;
    private final List<GameStateSubscriber> subscribers = new ArrayList<>();
    private GameStatus gameStatus;
    private final GameMode gameMode;

    /**
     * It is used to keep track of the current player in the turn order.
     */
    private int indexCurrentPlayer;

    private final Players players;

    private final Set<Pair<NodeId, TransportType>> possibleDestinations; // They refer to the current player
    private final List<TransportType> availableTransports;

    private TurnState turnState;
    private final RunnerTurnTrackerImpl runnerTurnTracker;
    private boolean runnerExposed = false;

    private int round = 1;

    /**
     * Creates a new game state.
     *
     * @param random the seeded random instance used the active match
     * @param gameMode the game mode
     * @param players the involved players
     */
    public GameStateImpl(Random random, GameMode gameMode, Players players) {
        this.random = random;
        this.gameMode = gameMode;
        this.players = players;
        this.availableTransports = new ArrayList<>();
        this.possibleDestinations = new HashSet<>();
        this.runnerTurnTracker = new RunnerTurnTrackerImpl();
        this.gameStatus = GameStatus.PLAYING;
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

        if (this.gameMode == GameMode.DETECTIVE) {
            return this.possibleDestinations.isEmpty() && this.getGameRound() > 1;
        }

        return false;
    }

    @Override
    public Random getSeededRandom() {
        return random;
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
            // Removal of destinations that can be reached by ferry, if player is not Mister
            // X
            if ((GameMode.DETECTIVE.equals(this.gameMode)
                            && this.getCurrentPlayer() != this.players.getComputerPlayer())
                    || (GameMode.MISTER_X.equals(this.gameMode)
                            && this.getCurrentPlayer() != this.players.getUserPlayer())) {
                this.possibleDestinations.removeIf(item -> TransportType.FERRY.equals(item.getY()));
            }
        }

        return this.possibleDestinations;
    }

    @Override
    public Set<Pair<NodeId, TransportType>> getPossibleDestinations() {
        return this.possibleDestinations;
    }

    @Override
    public boolean changeCurrentPlayer() {
        indexCurrentPlayer = (indexCurrentPlayer + 1) % players.getPlayersCount();
        return indexCurrentPlayer == 0;
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
    public boolean moveCurrentPlayer(NodeId destinationId, TransportType transport) {
        if (this.possibleDestinations.contains(new Pair<>(destinationId, transport))) {
            this.getCurrentPlayer().setPosition(destinationId);
            this.getCurrentPlayer().useTicket(Player.getTicketTypeForTransport(transport));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void nextRound() {
        this.round++;
    }

    @Override
    public boolean hideMisterX() {
        if (this.gameMode == GameMode.DETECTIVE) {
            return !Constants.REVEAL_TURNS_MISTER_X.contains(this.getGameRound())
                    || !this.getCurrentPlayer().equals(this.players.getComputerPlayer());
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
    public Players getPlayers() {
        return players;
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
    public List<Bobby> getBobbies() {
        return this.players.getBobbies();
    }

    @Override
    public Player getDetective() {
        return this.players.getDetective();
    }

    @Override
    public void resetTurn() {
        final Player player = getCurrentPlayer();
        this.turnState = new TurnState(player.getPosition());
    }

    @Override
    public TurnState getTurnState() {
        return this.turnState;
    }

    @Override
    public RunnerTurnTrackerImpl getRunnerTurnTracker() {
        return runnerTurnTracker;
    }

    @Override
    public List<MoveAction> computeValidMoves(MapData mapData, Player player, List<NodeId> excludedNodes) {
        final NodeId startingPosition = player.getPosition();
        final List<MapConnection> connections = mapData.getConnectionsFrom(startingPosition);
        final Set<NodeId> invalidPositions =
                players.getSeekers().map(Player::getPosition).collect(Collectors.toUnmodifiableSet());

        return connections.stream()
                .filter(it -> !invalidPositions.contains(it.getTo())
                        && !excludedNodes.contains(it.getTo())
                        && player.hasTransportModeTicket(it.getTransport()))
                .map(it -> new MoveAction(it.getTo(), it.getTransport()))
                .toList();
    }

    @Override
    public void exposeRunnerPosition() {
        final NodeId position = players.getMisterX().getPosition();
        final ExposedPosition exposed = new ExposedPosition(position, round);
        runnerExposed = true;
        notifySubscribers(it -> it.onExposedPosition(exposed));
    }

    @Override
    public void hideRunnerPosition() {
        runnerExposed = false;
        notifySubscribers(GameStateSubscriber::onRunnerHidden);
    }

    @Override
    public boolean isRunnerExposed() {
        return runnerExposed;
    }

    @Override
    public int maxRoundCount() {
        return ROUND_COUNT;
    }

    @Override
    public void subscribe(GameStateSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifySubscribers(Consumer<GameStateSubscriber> action) {
        for (final GameStateSubscriber subscriber : subscribers) {
            action.accept(subscriber);
        }
    }
}
