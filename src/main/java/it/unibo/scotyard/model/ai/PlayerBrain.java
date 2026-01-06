package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.players.Player;
import java.util.List;

public interface PlayerBrain {
    List<GameCommand> playTurn(Player player);
}
