package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.router.CommandDispatcher;
import it.unibo.scotyard.view.game.GameView;
import java.util.HashSet;
import java.util.Set;

public final class DetectiveGameControllerImpl extends GameControllerImpl {

    private static final NodeId NOT_VISIBLE_ON_MAP = new NodeId(-1);

    private NodeId selectedDestination;
    private TransportType selectedTransportType;

    public DetectiveGameControllerImpl(
            final CommandDispatcher dispatcher,
            final GameState gameData,
            final GameView view,
            final Controller mainController) {
        super(dispatcher, gameData, view, mainController);

        this.view.getSidebar().setEndTurnListener(e -> onEndTurn());
    }

    @Override
    public void initializeGame() {
        this.initializePlayersPositionsView();
        this.manageGameRound();
        this.view.getSidebar().enableEndTurnButton(false);
    }

    /** Initializes the positions of player in GameView (in MapPanel). */
    public void initializePlayersPositionsView() {
        this.view.getMapPanel().initializeBobbies(this.gameState.getNumberOfPlayers());
        for (int i = 0; i < this.gameState.getNumberOfPlayers(); i++) {
            this.updatePlayerPositionView(this.gameState.getCurrentPlayer());
            this.gameState.changeCurrentPlayer();
        }
    }

    /** Hides Mister X position on the map */
    private void hideMisterXPosition() {
        this.view.getMapPanel().setMisterXPosition(NOT_VISIBLE_ON_MAP);
    }

    private void updatePlayerPositionView(Player currentPlayer) {
        switch (currentPlayer.getName()) {
            case "Detective":
                this.view.getMapPanel().setDetectivePosition(currentPlayer.getPosition());
                break;
            case "Mister X":
                if (this.gameState.hideMisterX()) {
                    hideMisterXPosition();
                } else {
                    this.view.getMapPanel().setMisterXPosition(currentPlayer.getPosition());
                }
                break;
            default:
                int index = Integer.parseInt(currentPlayer.getName().substring(5, 6)) - 1;
                this.view.getMapPanel().setBobbyPosition(currentPlayer.getPosition(), index);
        }
        this.view.getMapPanel().repaint();
    }

    /**
     * Manages a round of a game. If the game is over, it calls a method of the GameView, which opens a the game over
     * window, which takes back the user to the main menu.
     */
    public void manageGameRound() {
        if (this.gameState.isGameOver()) {
            this.loadGameOverWindow();
        } else {
            this.updateSidebar(this.gameState.getCurrentPlayer());
            if (this.gameState.hideMisterX()) {
                hideMisterXPosition();
            }
            this.updatePlayerPositionView(this.gameState.getCurrentPlayer());
            Set<Pair<NodeId, TransportType>> possibleDestinations =
                    new HashSet<>(this.mainController.getPossibleDestinations(
                            this.gameState.getPositionPlayer(this.gameState.getCurrentPlayer())));
            possibleDestinations = this.gameState.loadPossibleDestinations(possibleDestinations);
            Set<NodeId> possibleDestinationsIDs = new HashSet<>();
            for (Pair<NodeId, TransportType> pair : possibleDestinations) {
                possibleDestinationsIDs.add(pair.getX());
            }
            if ("Mister X".equals(this.gameState.getCurrentPlayer().getName())) {
                // TODO : executeIA(), per turno di Mister X
            } else {
                this.view.getMapPanel().loadPossibleDestinations(possibleDestinationsIDs);
            }
            this.view.getMapPanel().repaint();
        }
    }

    public void destinationChosen(NodeId newPositionId) {
        if (this.gameState.areMultipleTransportsAvailable(newPositionId)) {
            System.out.println(this.gameState.getAvailableTransports(newPositionId));
            this.view.loadTransportSelectionDialog(new HashSet<>(this.gameState.getAvailableTransports(newPositionId)));
            this.view.getSidebar().enableEndTurnButton(false);
        } else {
            this.selectTransport(
                    this.gameState.getAvailableTransports(newPositionId).getFirst());
            this.view.getSidebar().enableEndTurnButton(true);
        }
        this.selectedDestination = newPositionId;
        this.view.getMapPanel().repaint();
    }

    /** Action listener for the EndTurn button. It moves the player. */
    public void onEndTurn() {
        this.view.getSidebar().enableEndTurnButton(false);
        this.movePlayer();
    }

    public void selectTransport(TransportType transportType) {
        this.selectedTransportType = transportType;
    }

    private void movePlayer() {
        if (this.gameState.moveCurrentPlayer(this.selectedDestination, this.selectedTransportType)) {
            this.view.getMapPanel().setSelectedDestination(NOT_VISIBLE_ON_MAP);
            this.updatePlayerPositionView(this.gameState.getCurrentPlayer());
            this.dispatcher.dispatch(new MoveCommand(this.selectedDestination, this.selectedTransportType));
            this.dispatcher.dispatch(new EndTurnCommand());
            this.gameState.changeCurrentPlayer();
            this.gameState.nextRound();
            this.manageGameRound();
            this.view.getMapPanel().repaint();
        }
    }
}
