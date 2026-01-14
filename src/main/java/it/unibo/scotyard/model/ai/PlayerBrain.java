package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.players.Player;
import java.util.List;

/**
 * The AI brain of a Player.
 */
@FunctionalInterface
public interface PlayerBrain {
    List<GameCommand> playTurn(Player player);
}
