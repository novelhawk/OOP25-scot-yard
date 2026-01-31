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
import java.util.stream.Collectors;

public final class DetectiveGameControllerImpl extends GameControllerImpl {

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
        super.initializeGame();
        this.initializePlayersPositionsView();
        this.view.getSidebar().enableEndTurnButton(false);
        this.manageGameRound();
    }

    /** Initializes the positions of player in GameView (in MapPanel). */
    public void initializePlayersPositionsView() {
        this.view.getMapPanel().initializeBobbies(this.gameState.getNumberOfPlayers());
        for (final Player player : gameState.getPlayers().getTurnOrder()) {
            this.updatePlayerPositionView(player);
        }
    }

    /** Hides Mister X position on the map */
    private void hideMisterXPosition() {
        this.view.getMapPanel().setMisterXPosition(HIDDEN_POSITION);
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
     * Manages a round of a game. If the game is over, it calls a method of the GameView, which opens the game over
     * window, which takes back the user to the main menu.
     */
    public void manageGameRound() {
        if (!gameState.getCurrentPlayer().isHuman()) {
            return;
        }

        this.updateSidebar(this.gameState.getCurrentPlayer());

        final Set<NodeId> destinationNodes = this.gameState.getPossibleDestinations().stream()
                .map(Pair::getX)
                .collect(Collectors.toSet());

        this.view.getMapPanel().loadPossibleDestinations(destinationNodes);
        this.view.getMapPanel().repaint();
    }

    @Override
    public void onTurnStart() {
        this.updatePlayerPositionView(this.gameState.getCurrentPlayer());
        manageGameRound();
    }

    @Override
    public void destinationChosen(NodeId newPositionId) {
        if (this.gameState.areMultipleTransportsAvailable(newPositionId)) {
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

    @Override
    public void selectTransport(TransportType transportType) {
        this.selectedTransportType = transportType;
    }

    private void movePlayer() {
        if (this.gameState.isMovableCurrentPlayer(this.selectedDestination, this.selectedTransportType)) {
            this.view.getMapPanel().setSelectedDestination(HIDDEN_POSITION);
            this.dispatcher.dispatch(new MoveCommand(this.selectedDestination, this.selectedTransportType));
            this.updatePlayerPositionView(this.gameState.getCurrentPlayer());
            this.view.getMapPanel().repaint();
            this.dispatcher.dispatch(new EndTurnCommand());
            this.manageGameRound();
        }
    }
}
