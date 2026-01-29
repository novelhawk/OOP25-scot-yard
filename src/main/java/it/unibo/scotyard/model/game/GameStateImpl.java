package it.unibo.scotyard.model.game;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.entities.ExposedPosition;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.entities.RunnerTurnTrackerImpl;
import it.unibo.scotyard.model.inventory.Inventory;
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

    private static final int NOT_REVEALED_YET = -1;
    private static final int FINAL_ROUND_COUNT = 24;

    private final Random random;
    private final List<GameStateSubscriber> subscribers = new ArrayList<>();
    private GameStatus gameStatus;
    private final GameMode gameMode;
    private final GameDifficulty gameDifficulty;

    /**
     * It is used to keep track of the current player in the turn order.
     */
    private int indexCurrentPlayer;

    private final Players players;

    // They refer to the current player
    private final Set<Pair<NodeId, TransportType>> possibleDestinations;
    private final List<TransportType> availableTransports;

    private TurnState turnState;
    private final RunnerTurnTrackerImpl runnerTurnTracker;
    private boolean runnerExposed = false;

    private int round = 1;

    private NodeId lastRevealedMisterXPosition;

    /**
     * Creates a new game state.
     *
     * @param random the seeded random instance used the active match
     * @param gameMode the game mode
     * @param players the involved players
     */
    public GameStateImpl(Random random, GameMode gameMode, Players players, GameDifficulty gameDifficulty) {
        this.random = random;
        this.gameMode = gameMode;
        this.players = players;
        this.gameDifficulty = gameDifficulty;
        this.availableTransports = new ArrayList<>();
        this.possibleDestinations = new HashSet<>();
        this.runnerTurnTracker = new RunnerTurnTrackerImpl();
        this.gameStatus = GameStatus.PLAYING;
        lastRevealedMisterXPosition = new NodeId(NOT_REVEALED_YET);
    }

    @Override
    public boolean isGameOver() {
        final NodeId runnerPosition = this.players.getMisterX().getPosition();
        final boolean found =
                this.players.getSeekers().anyMatch(it -> it.getPosition().equals(runnerPosition));
        boolean isOver = false;

        if (GameMode.DETECTIVE.equals(this.gameMode)) {
            isOver = this.possibleDestinations.isEmpty();
        } else {
            if (this.getCurrentPlayer() != this.players.getMisterX()) {
                isOver = this.possibleDestinations.isEmpty();
            }
        }

        if (found || this.round > FINAL_ROUND_COUNT) {
            isOver = true;
        }

        if (isOver) {
            this.setGameStatus(GameStatus.PAUSE);
        }
        return isOver;
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
                return lossString + " : sei stato catturato!";
            } else {
                return victoryString + " : Mister X catturato!";
            }
        } else {
            if (this.possibleDestinations.isEmpty()) {
                if (GameMode.DETECTIVE.equals(this.gameMode)) {
                    return lossString + ": biglietti esauriti per le fermate raggiungibili";
                } else {
                    if (this.getCurrentPlayer().equals(this.players.getMisterX())) {
                        return lossString + "  : non puoi muoverti";
                    } else {
                        return victoryString + " : gli avversari hanno finito i biglietti per muoversi";
                    }
                }
            } else {
                if (this.round >= FINAL_ROUND_COUNT) {
                    if (this.gameMode == GameMode.MISTER_X) return victoryString + " : sei riuscito a fuggire";
                } else {
                    return lossString + " : Mister X non è stato catturato in tempo";
                }
            }
        }
        return lossString;
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
                    && pos.equals(this.players.getComputerPlayer().getPosition())) {
                this.possibleDestinations.remove(destination);
            }
            /* Mister X can't go where detective is. */
            if (this.gameMode == GameMode.DETECTIVE
                    && this.getCurrentPlayer().equals(this.players.getComputerPlayer())
                    && pos.equals(this.players.getUserPlayer().getPosition())) {
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
                                && pos.equals(this.players.getUserPlayer().getPosition()))) {
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
            if (this.getCurrentPlayer().getNumberTickets(Inventory.getTicketTypeForTransport(transport)) == 0) {
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
        this.getCurrentPlayer().useTicket(Inventory.getTicketTypeForTransport(transport));
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
    public GameDifficulty getGameDifficulty() {
        return this.gameDifficulty;
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
        return FINAL_ROUND_COUNT;
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
