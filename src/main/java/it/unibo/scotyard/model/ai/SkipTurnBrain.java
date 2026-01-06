package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.players.Player;
import java.util.List;

public class SkipTurnBrain implements PlayerBrain {
    @Override
    public List<GameCommand> playTurn(Player player) {
        return List.of(new EndTurnCommand());
    }
}
