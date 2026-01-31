package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.game.GameState;
import java.util.List;
import java.util.Random;

/**
 * The AI used by the Runner
 */
public class RunnerBrain implements PlayerBrain {

    public RunnerBrain() {}

    @Override
    public List<GameCommand> playTurn(GameState gameState) {
        final Random random = gameState.getSeededRandom();
        final List<MoveAction> legalMoves = gameState.getTurnState().getLegalMoves();

        final MoveAction selectedMove = random.ints(0, legalMoves.size())
                .mapToObj(legalMoves::get)
                .findFirst()
                .orElseThrow();

        return List.of(new MoveCommand(selectedMove.destination(), selectedMove.transportType()), new EndTurnCommand());
    }
}
