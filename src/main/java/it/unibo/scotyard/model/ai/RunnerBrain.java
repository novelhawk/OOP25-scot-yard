package it.unibo.scotyard.model.ai;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.entities.MoveAction;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.function.ToIntFunction;

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
            case MEDIUM -> moveFurthestAway(gameState, 0.6f);
            case DIFFICULT -> moveFurthestAway(gameState, 0.2f);
        };
    }

    /**
     * An AI that plays a random valid move each turn.
     *
     * @param gameState the game state
     * @return the actions performed by the AI
     */
    private List<GameCommand> movingRandomly(GameState gameState) {
        final Random random = gameState.getSeededRandom();
        final List<MoveAction> legalMoves = gameState.getTurnState().getLegalMoves();

        final MoveAction selectedMove = random.ints(0, legalMoves.size())
                .mapToObj(legalMoves::get)
                .findFirst()
                .orElseThrow();

        return List.of(MoveCommand.fromMoveAction(selectedMove), new EndTurnCommand());
    }

    /**
     * A Runner AI that prioritizes nodes further away from the seekers.
     *
     * @param gameState the game state
     * @param error the percentage of randomness in the moves,
     *              with one being only random moves and zero
     *              being the move furthest away from the seekers
     * @return the actions performed by the AI
     */
    private List<GameCommand> moveFurthestAway(final GameState gameState, final float error) {
        final Random random = gameState.getSeededRandom();
        final List<MoveAction> legalMoves = gameState.getTurnState().getLegalMoves();
        final List<NodeId> seekersPositions =
                gameState.getPlayers().getSeekers().map(Player::getPosition).toList();

        final int[] distanceMap = seekerMinimumDistance(seekersPositions);
        final ToIntFunction<MoveAction> scoringFunction =
                it -> distanceMap[it.destination().id()];

        final List<MoveAction> sortedMoves = legalMoves.stream()
                .sorted(Comparator.comparingInt(scoringFunction).reversed())
                .toList();

        final int acceptedRange = Math.max(1, Math.round(sortedMoves.size() * error));
        final int moveQuality = random.nextInt(acceptedRange);
        final MoveAction selectedMove = sortedMoves.get(moveQuality);

        return List.of(MoveCommand.fromMoveAction(selectedMove), new EndTurnCommand());
    }

    /**
     * Uses Multi-Source BFS to calculate the minimum number of hops
     * needed to reach each graph node from any seeker position.
     *
     * @param seekerPositions the position of all seeker players
     * @return an array where the i-th index contains the distance between the seekers and the node with id i
     */
    public int[] seekerMinimumDistance(final Collection<NodeId> seekerPositions) {
        // We allocate for one more node to allow direct indexing with the 1-based node ids
        final boolean[] visited = new boolean[mapData.getNodeCount() + 1];
        final int[] distance = new int[mapData.getNodeCount() + 1];

        // High value for unreachable nodes
        Arrays.fill(distance, 200);

        // Set to zero the distances to the seekers current position
        for (final NodeId node : seekerPositions) {
            distance[node.id()] = 0;
        }

        Queue<NodeId> queue = new LinkedList<>(seekerPositions);
        while (!queue.isEmpty()) {
            final NodeId current = queue.remove();

            for (final MapConnection con : mapData.getConnectionsFrom(current)) {
                if (con.getTransport() == TransportType.FERRY) {
                    // Seekers cannot take ferries, we cannot mark the node as visited
                    // in case there is an alternative connection with another ticket type
                    continue;
                }

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
