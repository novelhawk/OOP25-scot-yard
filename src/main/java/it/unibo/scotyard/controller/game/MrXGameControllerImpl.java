package it.unibo.scotyard.controller.game;

import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.command.turn.EndTurnCommand;
import it.unibo.scotyard.model.command.turn.MoveCommand;
import it.unibo.scotyard.model.command.turn.UseDoubleMoveCommand;
import it.unibo.scotyard.model.game.GameState;
import it.unibo.scotyard.model.game.GameStatus;
import it.unibo.scotyard.model.game.turn.TurnManagerImpl.MoveOption;
import it.unibo.scotyard.model.map.MapData;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.router.CommandDispatcher;
import it.unibo.scotyard.view.dialogs.TransportSelectionDialog;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Controller implementation for Mr. X gameplay. Manages game initialization,
 * turn logic, and UI updates.
 *
 * <p>
 * Double move state machine Node click handling Transport selection when
 * multiple options available UI
 * synchronization.
 */
public final class MrXGameControllerImpl extends GameControllerImpl {

    /** Double move state machine. */
    public enum DoubleMoveState {
        AVAILABLE,
        WAITING_FIRST_MOVE,
        WAITING_SECOND_MOVE,
        COMPLETED,
        USED
    }

    private final MapData mapData;

    private MoveOption selectedMove;
    private DoubleMoveState doubleMoveState;

    /**
     * Creates a new Mr. X game controller.
     *
     * @param dispatcher the command dispatcher
     * @param game       the game instance
     * @param mapData    the map data
     * @param gameView   the game view
     * @param controller the controller
     * @throws NullPointerException if any parameter is null
     */
    public MrXGameControllerImpl(
            final CommandDispatcher dispatcher,
            final GameState game,
            final MapData mapData,
            final GameView gameView,
            final Controller controller) {
        super(dispatcher, game, gameView, controller);
        this.mapData = Objects.requireNonNull(mapData, "MapData cannot be null");
        this.doubleMoveState = DoubleMoveState.AVAILABLE;
    }

    @Override
    public void initializeGame() {
        super.initializeGame();
        initializeMrX();

        // Inizzializza bobbies list in MapPanel
        final int numberOfBobbies = this.gameState.getBobbies().size();
        this.getMapPanel().initializeBobbies(numberOfBobbies);

        setupListeners();

        updateUI();

        super.updateSidebar(this.gameState.getCurrentPlayer());
    }

    /** Initializes Mr. X with random starting position. */
    private void initializeMrX() {
        final MisterX mrX = (MisterX) this.gameState.getUserPlayer();

        mrX.initialize(this.mapData);
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
     * Converts legal moves from Model format (MoveAction) to Controller format
     * (MoveOption).
     * Gets legal moves from TurnState which already filters occupied positions.
     *
     * @return set of occupied node IDs
     */
    private Set<MoveOption> getValidMoves() {
        return this.gameState.getTurnState().getLegalMoves().stream()
                .map(moveAction -> new MoveOption(moveAction.destination(), moveAction.transportType()))
                .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Handles node click events.
     *
     * @param nodeId the clicked node ID
     */
    private void onNodeClicked(final NodeId nodeId) {
        if (this.gameState.getGameStatus() != GameStatus.PLAYING) {
            return;
        }

        // Se la doppia mossa è completata, blocca selezione nodi
        if (doubleMoveState == DoubleMoveState.COMPLETED) {
            this.view.showInfoDialog(
                    "Doppia mossa completata!\n\nClicca 'Fine Turno' per terminare il tuo turno.", "Turno Completato");
            return;
        }

        final MisterX mrX = (MisterX) this.gameState.getUserPlayer();
        final NodeId currentPos = mrX.getPosition();

        // Se click on current position allora deselect
        if (nodeId.equals(currentPos)) {
            selectedMove = null;
            updateUI();
            return;
        }

        final Set<MoveOption> validMoves = getValidMoves();

        final List<MoveOption> movesToNode = validMoves.stream()
                .filter(move -> move.getDestinationNode().equals(nodeId))
                .toList();

        if (movesToNode.isEmpty()) {
            // mossa non valida
            this.view.showWarningDialog(
                    "Mossa non valida " + nodeId.id() + " - connessione non esistente!", "Invalid Move");
            return;
        }

        // scelta in caso di piu trasporti possibili
        if (movesToNode.size() > 1) {
            selectedMove = chooseTransport(movesToNode, nodeId);
        } else {
            selectedMove = movesToNode.getFirst();
        }

        updateUI();
    }

    /** Handles end turn button click. */
    private void onEndTurn() {
        if (!this.gameState.getCurrentPlayer().isHuman()) {
            return;
        }

        if (this.gameState.getGameStatus() != GameStatus.PLAYING) {
            return;
        }

        // double move in progress, turno non completato
        if (doubleMoveState == DoubleMoveState.WAITING_FIRST_MOVE
                || doubleMoveState == DoubleMoveState.WAITING_SECOND_MOVE) {
            this.view.showWarningDialog("Completa prima la double move!", "Double Move in Progress");
            return;
        }

        final MisterX mrX = this.gameState.getPlayers().getMisterX();

        if (doubleMoveState == DoubleMoveState.COMPLETED) {
            // La doppia mossa è già stata eseguita, non serve makeMove()
            selectedMove = null;

            doubleMoveState = DoubleMoveState.USED;
        } else if (doubleMoveState == DoubleMoveState.AVAILABLE || doubleMoveState == DoubleMoveState.USED) {
            // Mossa normale (può farla se è AVAILABLE o USED)
            if (selectedMove == null) {
                this.view.showWarningDialog("Seleziona la prima destinazione!", ViewConstants.NO_MOVES_SELECTED);
                return;
            }

            try {
                // singola mossa
                mrX.makeMove(
                        selectedMove.getDestinationNode(), selectedMove.getTransport(), this.gameState.getGameRound());

                dispatcher.dispatch(new MoveCommand(selectedMove.getDestinationNode(), selectedMove.getTransport()));

                // clear
                selectedMove = null;

                // Rimane nello stato corrente (AVAILABLE o USED)

            } catch (IllegalArgumentException | IllegalStateException e) {
                this.view.showErrorDialog("Mossa non valida: " + e.getMessage(), "Errore");
                return;
            }
        }

        dispatcher.dispatch(new EndTurnCommand());

        updateUI();
    }

    @Override
    public void onRoundStart() {
        if (!gameState.getCurrentPlayer().isHuman()) {
            return;
        }

        final MisterX mrX = gameState.getPlayers().getMisterX();

        // RESET stato per il nuovo turno
        // Controlla se ha ancora ticket doppia mossa
        if (mrX.isDoubleMoveAvailable()) {
            doubleMoveState = DoubleMoveState.AVAILABLE;
        } else {
            doubleMoveState = DoubleMoveState.USED;
        }

        // Update sidebar
        super.updateSidebar(this.gameState.getCurrentPlayer());

        updateUI();
    }

    /** Handles double move button click. */
    private void onDoubleMoveButtonClicked() {
        if (this.gameState.getGameStatus() != GameStatus.PLAYING) {
            return;
        }

        final MisterX mrX = this.gameState.getPlayers().getMisterX();

        switch (doubleMoveState) {
            case AVAILABLE:
                // Activate double move
                if (!mrX.isDoubleMoveAvailable()) {
                    this.view.showWarningDialog("Double move is not available!", "Double Move Unavailable");
                    return;
                }

                doubleMoveState = DoubleMoveState.WAITING_FIRST_MOVE;
                selectedMove = null;
                dispatcher.dispatch(new UseDoubleMoveCommand());

                this.view.showInfoDialog("Double move activated!\n\nSelect your first destination.", "Double Move");
                break;

            case WAITING_FIRST_MOVE:
                // Confirm first move
                if (selectedMove == null) {
                    this.view.showWarningDialog(
                            "Please select your first destination!", ViewConstants.NO_MOVES_SELECTED);
                    return;
                }

                try {
                    mrX.startDoubleMove(
                            selectedMove.getDestinationNode(),
                            selectedMove.getTransport(),
                            this.gameState.getGameRound());

                    dispatcher.dispatch(
                            new MoveCommand(selectedMove.getDestinationNode(), selectedMove.getTransport()));

                    doubleMoveState = DoubleMoveState.WAITING_SECOND_MOVE;
                    selectedMove = null;

                    this.view.showInfoDialog(
                            "First move completed!\n\nNow select your second destination.",
                            "Double Move - Step 1 Done");

                } catch (IllegalArgumentException | IllegalStateException e) {
                    this.view.showErrorDialog("Invalid move: " + e.getMessage(), "Error");
                    return;
                }
                break;

            case WAITING_SECOND_MOVE:
                // Confirm second move
                if (selectedMove == null) {
                    this.view.showWarningDialog(
                            "Please select your second destination!", ViewConstants.NO_MOVES_SELECTED);
                    return;
                }

                try {
                    mrX.completeDoubleMove(
                            selectedMove.getDestinationNode(),
                            selectedMove.getTransport(),
                            this.gameState.getGameRound());

                    dispatcher.dispatch(
                            new MoveCommand(selectedMove.getDestinationNode(), selectedMove.getTransport()));

                    doubleMoveState = DoubleMoveState.COMPLETED;
                    selectedMove = null;

                    this.view.showInfoDialog(
                            "Double move completed!\n\nClick 'Fine Turno' to finish your turn.",
                            "Double Move Complete");

                } catch (IllegalArgumentException | IllegalStateException e) {
                    this.view.showErrorDialog("Invalid move: " + e.getMessage(), "Error");
                    return;
                }
                break;

            case COMPLETED:
                // Should not happen, button is disabled
                break;
            default:
                break;
        }

        updateUI();
    }

    /**
     * Shows transport selection dialog when multiple options available.
     *
     * @param moves  the list of possible moves to the same destination
     * @param nodeId the destination node ID
     * @return the selected move option, or null if cancelled
     */
    private MoveOption chooseTransport(final List<MoveOption> moves, final NodeId nodeId) {

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
        this.view.executeOnUIThread(() -> {
            final MisterX mrX = (MisterX) this.gameState.getUserPlayer();

            updateDoubleMoveButtonUI();

            // Mr. X position
            final NodeId mrXPos = mrX.getPosition();
            this.getMapPanel().setMisterXPosition(mrXPos);

            // Detective position (Mr. X mode mostra tutto)
            final Player detective = this.gameState.getDetective();
            if (detective != null) {
                final NodeId detectivePos = detective.getPosition();
                this.getMapPanel().setDetectivePosition(detectivePos);
            }

            // Bobby positions
            final List<Bobby> bobbies = this.gameState.getBobbies();
            for (int i = 0; i < bobbies.size(); i++) {
                final Bobby bobby = bobbies.get(i);
                final NodeId bobbyPos = bobby.getPosition();
                this.getMapPanel().setBobbyPosition(bobbyPos, i);
            }

            this.getMapPanel()
                    .setSelectedDestination(selectedMove != null ? selectedMove.getDestinationNode() : new NodeId(-1));

            // Se la doppia mossa è completata, non mostrare validMoves
            if (doubleMoveState == DoubleMoveState.COMPLETED) {
                this.getMapPanel().setValidMoves(Set.of()); // Empty set
            } else {
                this.getMapPanel().setValidMoves(getValidMoves());
            }

            this.getMapPanel().repaint();
        });
    }

    /** Updates double move UI. */
    private void updateDoubleMoveButtonUI() {
        final MisterX mrX = (MisterX) this.gameState.getUserPlayer();
        final SidebarPanel sidebar = this.view.getSidebar();

        switch (doubleMoveState) {
            case AVAILABLE:
                sidebar.showElseHideDoubleMoveButton(true);
                sidebar.updateDoubleMoveButton(mrX.isDoubleMoveAvailable(), "Doppia Mossa");
                break;
            case WAITING_FIRST_MOVE:
                sidebar.showElseHideDoubleMoveButton(true);
                sidebar.updateDoubleMoveButton(true, "Conferma 1° Mossa");
                break;
            case WAITING_SECOND_MOVE:
                sidebar.showElseHideDoubleMoveButton(true);
                sidebar.updateDoubleMoveButton(true, "Conferma 2° Mossa");
                break;
            case COMPLETED:
            case USED:
                sidebar.showElseHideDoubleMoveButton(false);
                break;
            default:
                break;
        }
    }

    // --- GameController ---

    // It doesn't do anything
    @Override
    public void destinationChosen(final NodeId newPositionId) {
        // TODO : Usare questo metodo (cambiando gestione turno)?
    }

    // It doesn't do anything
    @Override
    public void selectTransport(final TransportType transportType) {
        // TODO : Usare questo metodo (cambiando gestione turno)?
    }
}
