package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.command.round.EndRoundCommand;
import it.unibo.scotyard.model.command.turn.*;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.GameStateSubscriber;
import it.unibo.scotyard.model.game.TurnState;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.router.CommandDispatcher;
import it.unibo.scotyard.model.router.CommandHandlerStore;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The service responsible for handling the commands regarding a game round.
 *
 */
public class TurnService {
    private final Model model;

    public TurnService(final Model model) {
        this.model = Objects.requireNonNull(model, "model cannot be null");
    }

    /**
     * Handles the {@code StartTurnCommand}
     *
     * @param command a start turn command
     */
    public void handleStartTurn(final StartTurnCommand command) {
        final CommandDispatcher dispatcher = this.model.getDispatcher();
        final GameState gameState = this.model.getGameState();
        final Player player = gameState.getCurrentPlayer();
        gameState.resetTurn();

        final List<MoveAction> legalMoves = gameState.computeValidMoves(this.model.getMapData(), player, List.of());
        gameState.getTurnState().setLegalMoves(legalMoves);

        gameState.notifySubscribers(GameStateSubscriber::onTurnStart);

        player.getBrain().map(it -> it.playTurn(gameState)).stream()
                .flatMap(List::stream)
                .forEach(dispatcher::dispatch);
    }

    /**
     * Handles the {@code MoveCommand}.
     *
     * @param command a move command.
     */
    public void handleMove(final MoveCommand command) {
        final GameState gameState = this.model.getGameState();
        final TurnState turnState = gameState.getTurnState();
        final Player player = gameState.getCurrentPlayer();

        if (turnState.getRemainingMoves() > 0) {
            final List<MoveAction> validMoves =
                    gameState.computeValidMoves(this.model.getMapData(), player, turnState.getPositionHistory());
            turnState.setLegalMoves(validMoves);
        }

        if (gameState.isMovableCurrentPlayer(command.targetNode(), command.transportType())) {
            gameState.getTurnState().addMove(new MoveAction(command.targetNode(), command.transportType()));
            gameState.moveCurrentPlayer(command.targetNode(), command.transportType());
        }
    }

    /**
     * Handles the {@code UseDoubleMoveCommand}.
     *
     * @param command a use double move command.
     */
    public void handleDoubleMove(final UseDoubleMoveCommand command) {
        this.model.getGameState().getTurnState().doubleMove();
    }

    /**
     * Handles the {@code PassCommand}.
     *
     * @param command a pass command.
     */
    public void handleEndTurn(final EndTurnCommand command) {
        final CommandDispatcher dispatcher = this.model.getDispatcher();
        final GameState gameState = this.model.getGameState();
        final TurnState turnState = gameState.getTurnState();

        // TODO: merge TurnState into GameState (update ticket counts and player positions)
        if (gameState.getCurrentPlayer() instanceof MisterX) {
            final List<TransportType> usedTransports =
                    turnState.getMoves().stream().map(MoveAction::transportType).collect(Collectors.toList());

            gameState.getRunnerTurnTracker().addTurn(usedTransports);

            final boolean shouldReveal = model.getMapData().isRevealTurn(gameState.getGameRound());
            if (shouldReveal) {
                gameState.exposeRunnerPosition();
            }
        }

        gameState.notifySubscribers(GameStateSubscriber::onTurnEnd);

        if (gameState.changeCurrentPlayer()) {
            dispatcher.dispatch(new EndRoundCommand());
        }
    }

    /**
     * Handles the {@code ResetCommand}.
     *
     * @param command a reset command.
     */
    public void handleReset(final ResetCommand command) {
        this.model.getGameState().resetTurn();
    }

    /**
     * Registers the service's command handlers to the store.
     *
     * @param store the store that contains the handler registrations
     */
    public void register(final CommandHandlerStore store) {
        store.register(MoveCommand.class, this::handleMove);
        store.register(StartTurnCommand.class, this::handleStartTurn);
        store.register(UseDoubleMoveCommand.class, this::handleDoubleMove);
        store.register(EndTurnCommand.class, this::handleEndTurn);
        store.register(ResetCommand.class, this::handleReset);
    }
}
