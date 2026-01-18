package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.game.GameState;
import java.util.List;

/**
 * The AI brain of a Player.
 */
@FunctionalInterface
public interface PlayerBrain {
    /**
     * Creates the list of moves made by the AI player.
     *
     * @param gameState the current game state
     * @return the list of actions performed by the AI
     */
    List<GameCommand> playTurn(GameState gameState);
}
