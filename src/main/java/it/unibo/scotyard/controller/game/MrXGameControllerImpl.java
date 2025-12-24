package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.game.Game;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.turn.TurnManagerImpl.MoveOption;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.view.dialogs.TransportSelectionDialog;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Controller implementation for Mr. X gameplay. Manages game initialization, turn logic, and UI updates.
 *
 * <p>Double move state machine Node click handling Transport selection when multiple options available UI
 * synchronization
 */
public final class MrXGameControllerImpl extends GameControllerImpl {

    /** Double move state machine. */
    public enum DoubleMoveState {
        AVAILABLE,
        WAITING_FIRST_MOVE,
        WAITING_SECOND_MOVE,
        COMPLETED
    }

    private final MapData mapData;

    private MoveOption selectedMove;
    private DoubleMoveState doubleMoveState;

    /**
     * Creates a new Mr. X game controller.
     *
     * @param game the game instance
     * @param mapData the map data
     * @param gameView the game view
     * @throws NullPointerException if any parameter is null
     */
    public MrXGameControllerImpl(
            final Game game, final MapData mapData, final GameView gameView, final Controller controller) {
        super(game, gameView, controller);
        this.mapData = Objects.requireNonNull(mapData, "MapData cannot be null");
        this.doubleMoveState = DoubleMoveState.AVAILABLE;
    }

    @Override
    public void initializeGame() {
        initializeMrX();

        setupListeners();

        updateUI();

        super.updateSidebar(this.game.getCurrentPlayer());
    }

    /** Initializes Mr. X with random starting position. */
    private void initializeMrX() {
        final MisterX mrX = (MisterX) this.game.getUserPlayer();

        // poizione Random
        final List<Integer> initialPositions = this.mapData.getInitialPositions();
        final int startPos = initialPositions.get(new Random().nextInt(initialPositions.size()));
        mrX.setCurrentPosition(startPos);
        mrX.initialize(this.mapData);

        // Set game state
        this.game.setGameState(GameState.PLAYING);
    }

    /** Sets up UI event listeners. */
    private void setupListeners() {
        // Mapnode click listener
        this.getMapPanel().setNodeClickListener(this::onNodeClicked);

        // Sidebar button listeners
        this.view.getSidebar().setEndTurnListener(e -> onEndTurn());
        this.view.getSidebar().setDoubleMoveListener(e -> onDoubleMoveButtonClicked());

        this.view.getSidebar().setActionButtonsVisible(true);
    }

    /**
     * Handles node click events.
     *
     * @param nodeId the clicked node ID
     */
    private void onNodeClicked(final int nodeId) {
        if (this.game.getGameState() != GameState.PLAYING) {
            return;
        }

        final MisterX mrX = (MisterX) this.game.getUserPlayer();
        final int currentPos = mrX.getCurrentPosition().getId();

        // Se click on current position allora deselect
        if (nodeId == currentPos) {
            selectedMove = null;
            updateUI();
            return;
        }

        final Set<MoveOption> validMoves = mrX.getValidMoves(Set.of());

        final List<MoveOption> movesToNode = validMoves.stream()
                .filter(move -> move.getDestinationNode() == nodeId)
                .toList();

        if (movesToNode.isEmpty()) {
            // mossa non valida
            JOptionPane.showMessageDialog(
                    null,
                    "Mossa non valida " + nodeId + " - connessione non esistente!",
                    "Invalid Move",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // scelta in caso di piu trasporti possibili
        if (movesToNode.size() > 1) {
            selectedMove = chooseTransport(movesToNode, nodeId);
        } else {
            selectedMove = movesToNode.get(0);
        }

        updateUI();
    }

    /** Handles end turn button click. */
    private void onEndTurn() {
        if (this.game.getGameState() != GameState.PLAYING) {
            return;
        }

        // double move in progress, turno non completato
        if (doubleMoveState == DoubleMoveState.WAITING_FIRST_MOVE
                || doubleMoveState == DoubleMoveState.WAITING_SECOND_MOVE) {
            JOptionPane.showMessageDialog(
                    null, "Completa prima la double move!", "Double Move in Progress", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedMove == null) {
            JOptionPane.showMessageDialog(
                    null, "Seleziona la prima destinazione!", "No Move Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final MisterX mrX = (MisterX) this.game.getUserPlayer();
        try {
            // singola mossa
            mrX.makeMove(selectedMove.getDestinationNode(), selectedMove.getTransport(), this.game.getGameRound());

            // clear
            selectedMove = null;

            // reset double move (se non usata in quel round) per il round successivo
            if (doubleMoveState == DoubleMoveState.COMPLETED) {
                // Keep completed state
            } else {
                doubleMoveState = mrX.isDoubleMoveAvailable() ? DoubleMoveState.AVAILABLE : DoubleMoveState.COMPLETED;
            }

            // Check victory
            if (super.isGameOver()) {
                super.loadGameOverWindow();
            }

            // Advance turn (AI turn starts)
            this.game.changeCurrentPlayer();

            // TODO: executeAI() for detective and bobbies

            // Advance turn (back to Mr. X's turn)
            this.game.nextRound();

            // Check if game is over after AI's turn
            if (super.isGameOver()) {
                super.loadGameOverWindow();
            }

            // Update UI
            updateUI();

        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(
                    null, "Mossa non valida: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Handles double move button click. */
    private void onDoubleMoveButtonClicked() {
        if (this.game.getGameState() != GameState.PLAYING) {
            return;
        }

        final MisterX mrX = (MisterX) this.game.getUserPlayer();

        switch (doubleMoveState) {
            case AVAILABLE:
                // Activate double move
                if (!mrX.isDoubleMoveAvailable()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Double move is not available!",
                            "Double Move Unavailable",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                doubleMoveState = DoubleMoveState.WAITING_FIRST_MOVE;
                selectedMove = null;

                JOptionPane.showMessageDialog(
                        null,
                        "Double move activated!\n\nSelect your first destination.",
                        "Double Move",
                        JOptionPane.INFORMATION_MESSAGE);
                break;

            case WAITING_FIRST_MOVE:
                // Confirm first move
                if (selectedMove == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please select your first destination!",
                            "No Move Selected",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    mrX.startDoubleMove(
                            selectedMove.getDestinationNode(), selectedMove.getTransport(), this.game.getGameRound());

                    doubleMoveState = DoubleMoveState.WAITING_SECOND_MOVE;
                    selectedMove = null;

                    JOptionPane.showMessageDialog(
                            null,
                            "First move completed!\n\nNow select your second destination.",
                            "Double Move - Step 1 Done",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (IllegalArgumentException | IllegalStateException e) {
                    JOptionPane.showMessageDialog(
                            null, "Invalid move: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;

            case WAITING_SECOND_MOVE:
                // Confirm second move
                if (selectedMove == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please select your second destination!",
                            "No Move Selected",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    mrX.completeDoubleMove(
                            selectedMove.getDestinationNode(), selectedMove.getTransport(), this.game.getGameRound());

                    doubleMoveState = DoubleMoveState.COMPLETED;
                    selectedMove = null;

                    JOptionPane.showMessageDialog(
                            null,
                            "Double move completed!\n\nClick 'Fine Turno' to finish your turn.",
                            "Double Move Complete",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (IllegalArgumentException | IllegalStateException e) {
                    JOptionPane.showMessageDialog(
                            null, "Invalid move: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;

            case COMPLETED:
                // Should not happen, button is disabled
                break;
        }

        updateUI();
    }

    /**
     * Shows transport selection dialog when multiple options available.
     *
     * @param moves the list of possible moves to the same destination
     * @param nodeId the destination node ID
     * @return the selected move option, or null if cancelled
     */
    private MoveOption chooseTransport(final List<MoveOption> moves, final int nodeId) {

        final List<TransportType> transportTypes =
                moves.stream().map(MoveOption::getTransport).toList();

        // dialog
        final TransportSelectionDialog dialog = new TransportSelectionDialog(null, nodeId, transportTypes);
        final TransportType selectedTransport = dialog.showAndWait();

        if (selectedTransport == null) {
            return null; // User cancelled
        }

        // Trova la mossa corrispondente
        return moves.stream()
                .filter(move -> move.getTransport() == selectedTransport)
                .findFirst()
                .orElse(null);
    }

    /** Updates all UI components. */
    private void updateUI() {
        SwingUtilities.invokeLater(() -> {
            final MisterX mrX = (MisterX) this.game.getUserPlayer();

            updateDoubleMoveButtonUI();

            this.getMapPanel().setMisterXPosition(mrX.getCurrentPosition().getId());
            this.getMapPanel().setSelectedDestination(selectedMove != null ? selectedMove.getDestinationNode() : -1);
            this.getMapPanel().setValidMoves(mrX.getValidMoves(Set.of()));

            this.getMapPanel().repaint();
        });
    }

    /** Updates double move UI */
    private void updateDoubleMoveButtonUI() {
        final MisterX mrX = (MisterX) this.game.getUserPlayer();
        final SidebarPanel sidebar = this.view.getSidebar();

        switch (doubleMoveState) {
            case AVAILABLE:
                sidebar.updateDoubleMoveButton(mrX.isDoubleMoveAvailable(), "Doppia Mossa");
                break;
            case WAITING_FIRST_MOVE:
                sidebar.updateDoubleMoveButton(true, "Conferma 1° Mossa");
                break;
            case WAITING_SECOND_MOVE:
                sidebar.updateDoubleMoveButton(true, "Conferma 2° Mossa");
                break;
            case COMPLETED:
                sidebar.updateDoubleMoveButton(false, "Doppia Mossa");
                break;
        }
    }

    // --- GameController ---

    // It doesn't do anything
    public void destinationChosen(int newPositionId) {
        // TODO : Usare questo metodo (cambiando gestione turno)?
    }

    // It doesn't do anything
    public void selectTransport(TransportType transportType) {
        // TODO : Usare questo metodo (cambiando gestione turno)?
    }
}
