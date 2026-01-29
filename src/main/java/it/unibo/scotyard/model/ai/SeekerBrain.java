package it.unibo.scotyard.model.ai;

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
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/*
 * The AI used by Detective and Bobbies
 */
public class SeekerBrain implements PlayerBrain {

    private final Random random;
    private final MapData map;

    public SeekerBrain(final Random random, final MapData mapData) {
        this.random = random;
        this.map = mapData;
    }

    private TicketType convertTransportType(TransportType transportType) {
        switch (transportType) {
            case TAXI:
                return TicketType.TAXI;
            case BUS:
                return TicketType.BUS;
            case UNDERGROUND:
                return TicketType.UNDERGROUND;
            case FERRY:
                return TicketType.BLACK;
            // TODO : gestione caso default
            default:
                return TicketType.TAXI;
        }
    }

    @Override
    public List<GameCommand> playTurn(GameState gameState) {
        GameDifficulty gameDifficulty = gameState.getGameDifficulty();
        Player player = gameState.getCurrentPlayer();
        final NodeId currentPosition = player.getPosition();
        List<Pair<NodeId, TransportType>> possibleDestinations = new ArrayList<>();
        for (MapConnection connection : this.map.getConnectionsFrom(currentPosition)) {
            possibleDestinations.add(new Pair<NodeId, TransportType>(connection.getTo(), connection.getTransport()));
        }
        possibleDestinations =
                gameState.loadPossibleDestinations(possibleDestinations.stream().collect(Collectors.toSet())).stream()
                        .collect(Collectors.toList());

        Pair<NodeId, TransportType> selectedMove;
        NodeId misterXNodeId = gameState.getLastRevealedMisterXPosition();

        switch (gameDifficulty) {
            default:
            case EASY:
                // Selects a random destination among the possible ones
                selectedMove = possibleDestinations.get(random.nextInt(possibleDestinations.size()));
                return List.of(new MoveCommand(selectedMove.getX(), selectedMove.getY()), new EndTurnCommand());
            case MEDIUM:
                misterXNodeId = gameState.getLastRevealedMisterXPosition();
            case DIFFICULT:
                // Selects the destination that has the closer NodeId number to the NodeId of the current position of
                // Mister X
                if (GameDifficulty.DIFFICULT.equals(gameDifficulty)) {
                    misterXNodeId = gameState.getUserPlayer().getPosition();
                }
                selectedMove = possibleDestinations.getFirst();
                for (Pair<NodeId, TransportType> pair : possibleDestinations) {
                    int currentDifference =
                            Math.abs((misterXNodeId.id()) - pair.getX().id());
                    int lowestDifference =
                            Math.abs((misterXNodeId.id()) - selectedMove.getX().id());
                    if (currentDifference <= lowestDifference) {
                        /* If the player is a Detective and the two differences are the same,
                        /* the new closerNode is selected according to the number and type of tickets possessed
                        /* by the player : among the two possible destinations, the one selected is the one for
                        /* which the player has a bigger amount of tickets (according to the TicketType). */
                        if (player instanceof Detective && currentDifference == lowestDifference) {
                            int ticketsPrevious = player.getNumberTickets(convertTransportType(selectedMove.getY()));
                            int ticketsCurrent = player.getNumberTickets(convertTransportType(pair.getY()));
                            if (ticketsPrevious <= ticketsCurrent) {
                                selectedMove = pair;
                            }
                        } else {
                            selectedMove = pair;
                        }
                    }
                }
                return List.of(new MoveCommand(selectedMove.getX(), selectedMove.getY()), new EndTurnCommand());
        }
    }
}
