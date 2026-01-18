package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.game.GameDifficulty;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.map.MapData;
import java.util.List;
import java.util.Random;

/**
 * The AI used by the Runner
 */
public class RunnerBrain implements PlayerBrain {
    private final Random random;
    private final MapData map;

    @SuppressWarnings("unused")
    private final GameDifficulty difficulty;

    public RunnerBrain(final Random random, final MapData map, final GameDifficulty difficulty) {
        this.random = random;
        this.map = map;
        this.difficulty = difficulty;
    }

    @Override
    public List<GameCommand> playTurn(GameState gameState) {
        final List<MoveAction> legalMoves = gameState.getTurnState().getLegalMoves();

        final MoveAction selectedMove = random.ints(0, legalMoves.size())
                .mapToObj(legalMoves::get)
                .findFirst()
                .orElseThrow();

        return List.of(new MoveCommand(selectedMove.destination(), selectedMove.transportType()), new EndTurnCommand());
    }
}
