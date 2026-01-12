package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.game.GameDifficulty;
import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.players.Player;
import java.util.List;
import java.util.Random;

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
    public List<GameCommand> playTurn(Player player) {
        final NodeId currentPosition = player.getPosition();
        final List<MapConnection> neighbours = map.getConnectionsFrom(currentPosition);

        final MapConnection selectedMove = random.ints(0, neighbours.size())
                .mapToObj(neighbours::get)
                .findFirst()
                .orElseThrow();

        return List.of(new MoveCommand(selectedMove.getTo(), selectedMove.getTransport()), new EndTurnCommand());
    }
}
