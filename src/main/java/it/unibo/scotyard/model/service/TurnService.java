package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.command.turn.ResetCommand;
import it.unibo.scotyard.model.command.turn.UseDoubleMoveCommand;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.TurnState;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.MisterX;
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
     * Handles the {@code MoveCommand}.
     *
     * @param command a move command.
     */
    public void handleMove(final MoveCommand command) {
        this.model.getGameState().getTurnState().addMove(new MoveAction(command.targetNode(), command.transportType()));
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
        final GameState gameState = this.model.getGameState();
        final TurnState turnState = gameState.getTurnState();

        // TODO: merge TurnState into GameState (update ticket counts and player positions)
        if (gameState.getCurrentPlayer() instanceof MisterX) {
            final List<TransportType> usedTransports =
                    turnState.getMoves().stream().map(MoveAction::transportType).collect(Collectors.toList());

            gameState.getRunnerTurnTracker().addTurn(usedTransports);
        }

        // TODO: Change current player here
        gameState.resetTurn();
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
        store.register(UseDoubleMoveCommand.class, this::handleDoubleMove);
        store.register(EndTurnCommand.class, this::handleEndTurn);
        store.register(ResetCommand.class, this::handleReset);
    }
}
