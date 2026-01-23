package it.unibo.scotyard.model.service;

import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.ai.RunnerBrain;
import it.unibo.scotyard.model.ai.SeekerBrain;
import it.unibo.scotyard.model.command.game.InitializeGameCommand;
import it.unibo.scotyard.model.game.GameDifficulty;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.game.GameStateImpl;
import it.unibo.scotyard.model.game.Players;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.router.CommandHandlerStore;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The service responsible for handling the commands regarding changes in the
 * game state.
 *
 */
public class GameStateService {
    private final Model model;

    /**
     * Creates a new GameState service.
     *
     * @param model the model
     */
    public GameStateService(final Model model) {
        this.model = model;
    }

    /**
     * Handles the {@code InitializeGameCommand}.
     *
     * @param command an initialize game command.
     */
    public void handleInitialize(final InitializeGameCommand command) {
        final Random random = new Random(command.seed());
        final List<NodeId> initialPositions = model.getMapData().getInitialPositions();
        final Iterator<NodeId> shuffledInitialPositions =
                shuffleInitialPositions(random, initialPositions).iterator();
        final int additionalPlayers = getAdditionalSeekersCount(command.gameMode(), command.difficulty());

        final MisterX misterX =
                createMisterX(random, command.gameMode(), command.difficulty(), shuffledInitialPositions.next());
        final Detective detective = createDetective(command.gameMode(), shuffledInitialPositions.next());

        final List<Bobby> bobbies = Stream.generate(shuffledInitialPositions::next)
                .limit(additionalPlayers)
                .map(position -> createBobby(command.gameMode(), position))
                .collect(Collectors.toList());

        for (int i = 0; i < bobbies.size(); i++) {
            Bobby bobby = bobbies.get(i);
            bobby.setName("Bobby" + (i + 1));
        }

        final Players players = new Players(command.gameMode(), misterX, detective, bobbies);

        final GameStateImpl gameState = new GameStateImpl(random, command.gameMode(), players, command.difficulty());
        this.model.setGameState(gameState);
    }

    /**
     * Registers the service's command handlers to the store.
     *
     * @param store the store that contains the handler registrations
     */
    public void register(final CommandHandlerStore store) {
        store.register(InitializeGameCommand.class, this::handleInitialize);
    }

    private MisterX createMisterX(Random random, GameMode gameMode, GameDifficulty difficulty, NodeId initialPosition) {
        return switch (gameMode) {
            case GameMode.DETECTIVE -> {
                final RunnerBrain runnerBrain = new RunnerBrain(random, this.model.getMapData(), difficulty);
                yield new MisterX(initialPosition, runnerBrain);
            }
            case GameMode.MISTER_X -> new MisterX(initialPosition);
        };
    }

    private Detective createDetective(GameMode gameMode, NodeId initialPosition) {
        return switch (gameMode) {
            case GameMode.DETECTIVE -> new Detective(initialPosition);
            case GameMode.MISTER_X -> {
                final SeekerBrain detectiveBrain =
                        new SeekerBrain(this.model.getSeededRandom(), this.model.getMapData());
                yield new Detective(initialPosition, detectiveBrain);
            }
        };
    }

    private Bobby createBobby(GameMode gameMode, NodeId initialPosition) {
        return switch (gameMode) {
            case GameMode.DETECTIVE -> new Bobby(initialPosition);
            case GameMode.MISTER_X -> {
                final SeekerBrain bobbyBrain =
                        new SeekerBrain(this.model.getSeededRandom(), this.model.getMapData());
                yield new Bobby(initialPosition, bobbyBrain);
            }
        };
    }

    private List<NodeId> shuffleInitialPositions(final Random random, final List<NodeId> initialPositions) {
        final List<NodeId> copy = new ArrayList<>(initialPositions);
        Collections.shuffle(copy, random);
        return copy;
    }

    private int getAdditionalSeekersCount(GameMode gameMode, GameDifficulty difficulty) {
        final int seekers =
                switch (difficulty) {
                    case GameDifficulty.EASY -> 0;
                    case GameDifficulty.MEDIUM -> 1;
                    case GameDifficulty.DIFFICULT -> 2;
                };

        if (gameMode == GameMode.DETECTIVE) {
            return 2 - seekers;
        } else {
            return seekers + 1;
        }
    }
}
