package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.game.GameState;
import java.util.List;

/**
 * An AI that does nothing but skip the turn
 */
public class SkipTurnBrain implements PlayerBrain {
    @Override
    public List<GameCommand> playTurn(GameState gameState) {
        return List.of(new EndTurnCommand());
    }
}
