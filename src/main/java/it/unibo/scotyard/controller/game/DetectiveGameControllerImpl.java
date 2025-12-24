package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.Pair;
import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.view.game.GameView;
import java.util.HashSet;
import java.util.Set;

public final class DetectiveGameControllerImpl extends GameControllerImpl {

    private int selectedDestination;
    private TransportType selectedTransportType;

    public DetectiveGameControllerImpl(final Game gameData, final GameView view, final Controller mainController) {
        super(gameData, view, mainController);

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
        this.view.getMapPanel().initializeBobbies(this.game.getNumberOfPlayers());
        for (int i = 0; i < this.game.getNumberOfPlayers(); i++) {
            this.updatePlayerPositionView(this.game.getCurrentPlayer());
            this.game.changeCurrentPlayer();
        }
    }

    private void updatePlayerPositionView(Player currentPlayer) {
        switch (currentPlayer.getName()) {
            case "Detective":
                this.view.getMapPanel().setDetectivePosition(currentPlayer.getCurrentPositionId());
                break;
            case "Mister X":
                this.view.getMapPanel().setMisterXPosition(currentPlayer.getCurrentPositionId());
                break;
            default:
                int index = Integer.valueOf(currentPlayer.getName().substring(5, 6)) - 1;
                this.view.getMapPanel().setBobbyPosition(currentPlayer.getCurrentPositionId(), index);
        }
        this.view.getMapPanel().repaint();
    }

    /**
     * Manages a round of a game. If the game is over, it calls a method of the GameView, which opens a the game over
     * window, which takes back the user to the main menu.
     */
    public void manageGameRound() {
        if (this.game.isGameOver()) {
            this.loadGameOverWindow();
        } else {
            this.updateSidebar(this.game.getCurrentPlayer());
            this.updatePlayerPositionView(this.game.getCurrentPlayer());
            Set<Pair<Integer, TransportType>> possibleDestinations =
                    new HashSet<>(this.mainController.getPossibleDestinations(
                            this.game.getPositionPlayer(this.game.getCurrentPlayer())));
            this.game.loadPossibleDestinations(possibleDestinations);
            Set<Integer> possibleDestinationsIDs = new HashSet<>();
            for (Pair<Integer, TransportType> pair : possibleDestinations) {
                possibleDestinationsIDs.add(pair.getX());
            }
            this.view.getMapPanel().loadPossibleDestinations(possibleDestinationsIDs);
            this.view.getMapPanel().repaint();
        }
    }

    public void destinationChosen(int newPositionId) {
        if (this.game.areMultipleTransportsAvailable(newPositionId)) {
            System.out.println(this.game.getAvailableTransports(newPositionId));
            this.view.loadTransportSelectionDialog(new HashSet<>(this.game.getAvailableTransports(newPositionId)));
            this.view.getSidebar().enableEndTurnButton(false);
        } else {
            this.selectTransport(this.game.getAvailableTransports(newPositionId).getFirst());
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
        if (this.game.moveCurrentPlayer(this.selectedDestination, this.selectedTransportType)) {
            this.view.getMapPanel().setSelectedDestination(-1);
            this.updatePlayerPositionView(this.game.getCurrentPlayer());
            this.game.changeCurrentPlayer();
            this.game.nextRound();
            this.manageGameRound();
            this.view.getMapPanel().repaint();
        } else {
            System.out.println("ERRORE");
        }
    }
}
