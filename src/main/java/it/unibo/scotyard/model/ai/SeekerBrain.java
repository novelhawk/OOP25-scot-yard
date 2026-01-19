package it.unibo.scotyard.model.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.game.GameDifficulty;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.map.MapConnection;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;

public class SeekerBrain implements PlayerBrain {

    private final Random random;
    private final Model model;
    private final MapData map;
    private final GameDifficulty difficulty;

    public SeekerBrain(final Random random, final Model model, final MapData mapData, final GameDifficulty gameDifficulty){
        this.random = random;
        this.model = model;
        this.map = mapData;
        this.difficulty = gameDifficulty;
    }

    @Override
    public List<GameCommand> playTurn(Player player) {
        GameState gameState = this.model.getGameState();

        final NodeId currentPosition = player.getPosition();
        List<Pair<NodeId,TransportType>> possibleDestinations = new ArrayList<>();
        for(MapConnection connection : this.map.getConnectionsFrom(currentPosition)){
            possibleDestinations.add(new Pair<NodeId,TransportType>(connection.getTo(), connection.getTransport()));
        }
        possibleDestinations = gameState.loadPossibleDestinations(possibleDestinations.stream().collect(Collectors.toSet()))
            .stream().collect(Collectors.toList());
        Pair<NodeId,TransportType> selectedMove = possibleDestinations.get(random.nextInt(possibleDestinations.size()));

        return List.of(new MoveCommand(selectedMove.getX(), selectedMove.getY()), new EndTurnCommand());
    }
    
    
}
