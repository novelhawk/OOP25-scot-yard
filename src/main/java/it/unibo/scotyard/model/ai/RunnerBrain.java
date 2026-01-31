package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.players.Player;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The AI used by the Runner
 */
public class RunnerBrain implements PlayerBrain {

    private final MapData mapData;

    public RunnerBrain(final MapData mapData) {
        this.mapData = mapData;
    }

    @Override
    public List<GameCommand> playTurn(GameState gameState) {
        return switch (gameState.getGameDifficulty()) {
            case EASY -> movingRandomly(gameState);
            case MEDIUM, DIFFICULT -> moveFurthestAway(gameState);
        };
    }

    private List<GameCommand> movingRandomly(GameState gameState) {
        final Random random = gameState.getSeededRandom();
        final List<MoveAction> legalMoves = gameState.getTurnState().getLegalMoves();

        final MoveAction selectedMove = random.ints(0, legalMoves.size())
                .mapToObj(legalMoves::get)
                .findFirst()
                .orElseThrow();

        return List.of(MoveCommand.fromMoveAction(selectedMove), new EndTurnCommand());
    }

    private List<GameCommand> moveFurthestAway(final GameState gameState) {
        final List<MoveAction> legalMoves = gameState.getTurnState().getLegalMoves();
        final LinkedList<NodeId> seekersPositions = gameState
                .getPlayers()
                .getSeekers()
                .map(Player::getPosition)
                .collect(Collectors.toCollection(LinkedList::new));

        final int[] distanceMap = seekerMinimumDistance(seekersPositions);
        final MoveAction move = legalMoves.stream()
                .max(Comparator.comparingInt(it -> distanceMap[it.destination().id()]))
                .orElseThrow();

        return List.of(MoveCommand.fromMoveAction(move), new EndTurnCommand());
    }

    public int[] seekerMinimumDistance(final LinkedList<NodeId> seekerPositions) {
        final boolean[] visited = new boolean[mapData.getNodeCount() + 1];
        final int[] distance = new int[mapData.getNodeCount() + 1];

        Queue<NodeId> queue = new LinkedList<>(seekerPositions);
        while (!queue.isEmpty()) {
            final NodeId current = queue.remove();

            for (final MapConnection con : mapData.getConnectionsFrom(current)) {
                final NodeId node = con.getTo();
                if (!visited[node.id()]) {
                    visited[node.id()] = true;
                    distance[node.id()] = distance[current.id()] + 1;
                    queue.add(node);
                }
            }
        }

        return distance;
    }
}
