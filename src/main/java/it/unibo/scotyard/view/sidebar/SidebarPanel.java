package it.unibo.scotyard.view.sidebar;

import it.unibo.scotyard.commons.patterns.CommonCostants;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.tracker.TrackerPanel;
import it.unibo.scotyard.view.tracker.TrackerPanelImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Sidebar panel for game UI. Currently displays only background, ready for
 * future content.
 */
public final class SidebarPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int SIDEBAR_WIDTH = 300;
    private static final int PADDING = 10;

    // Typography

    // Layout spacing
    private static final int SPACING = 10;
    private static final int SMALL_SPACING = 6;

    // Components
    private JLabel currentGameModeLabel;
    private JLabel roundLabel;
    private JLabel currentPlayerLabel;
    private JLabel taxiTicketsLabel;
    private JLabel busTicketsLabel;
    private JLabel undergroundTicketsLabel;
    private JLabel blackTicketsLabel;
    private JLabel doubleMoveTicketsLabel;
    private JButton endTurnButton;
    private JButton loadRulesButton;
    private JButton doubleMoveButton;
    private final TrackerPanelImpl trackerPanel;

    private GameView gameView;
    private GameMode currentGameMode;

    /**
     * Creates a sidebar panel.
     *
     * @param gameView the game view
     */
    public SidebarPanel(final GameView gameView) {
        this.gameView = gameView;
        this.trackerPanel = new TrackerPanelImpl(gameView);
        setupSidebar();
        buildContent();
    }

    /**
     * Gets the Mister X tracker panel
     *
     * @return the Mister X tracker panel
     */
    public TrackerPanel getTrackerPanel() {
        return trackerPanel;
    }

    // Configure sidebar properties
    private void setupSidebar() {
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setBackground(ScotColors.BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    // Sidebar content
    private void buildContent() {
        this.currentGameModeLabel = createCurrentGameModeLabel();
        this.add(this.currentGameModeLabel);
        this.add(Box.createVerticalStrut(SPACING));

        this.roundLabel = createCountRoundLabel();
        this.add(roundLabel);
        this.add(Box.createVerticalStrut(SPACING));

        this.currentPlayerLabel = createCurrentPlayerLabel();
        this.add(currentPlayerLabel);
        this.add(Box.createVerticalStrut(SPACING));

        this.add(createInventoryLabel());
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.taxiTicketsLabel = createTicketLabel(CommonCostants.TAXI_TICKETS_TEXT);
        this.add(taxiTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.busTicketsLabel = createTicketLabel(CommonCostants.BUS_TICKETS_TEXT);
        this.add(busTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.undergroundTicketsLabel = createTicketLabel(CommonCostants.UNDERGROUND_TICKETS_TEXT);
        this.add(undergroundTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.blackTicketsLabel = createTicketLabel(CommonCostants.BLACK_TICKETS_TEXT);
        this.add(blackTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.doubleMoveTicketsLabel = createTicketLabel(CommonCostants.DOUBLE_MOVE_TICKETS_TEXT);
        this.add(doubleMoveTicketsLabel);
        this.add(Box.createVerticalStrut(SPACING));

        this.add(trackerPanel);
        this.add(Box.createVerticalStrut(SPACING));

        this.doubleMoveButton = createActionButton("Doppia Mossa");
        this.add(doubleMoveButton);
        this.add(Box.createVerticalStrut(SMALL_SPACING));

        this.endTurnButton = createActionButton("Fine Turno");
        this.add(endTurnButton);
        this.add(Box.createVerticalStrut(SPACING));

        // Nasconde i bottony by default (da capire)
        this.doubleMoveButton.setVisible(false);

        this.loadRulesButton = createLoadRulesButton(CommonCostants.LOAD_RULES_TEXT);
        this.add(loadRulesButton);

        this.add(Box.createVerticalGlue());
    }

    private JLabel createCurrentGameModeLabel() {
        final JLabel label = new JLabel("Player");
        label.setFont(ScotFont.TEXT_FONT_24);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createCountRoundLabel() {
        final JLabel label = new JLabel("Round : ");
        label.setFont(ScotFont.TEXT_FONT_18);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createInventoryLabel() {
        final JLabel label = new JLabel(CommonCostants.INVENTORY_TEXT);
        label.setFont(ScotFont.TEXT_FONT_18);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createTicketLabel(final String text) {
        final JLabel label = new JLabel(text);
        label.setFont(ScotFont.TEXT_FONT_14);
        switch (text) {
            case CommonCostants.TAXI_TICKETS_TEXT:
                label.setForeground(ScotColors.TAXI_COLOR);
                break;
            case CommonCostants.BUS_TICKETS_TEXT:
                label.setForeground(ScotColors.BUS_COLOR);
                break;
            case CommonCostants.UNDERGROUND_TICKETS_TEXT:
                label.setForeground(ScotColors.UNDERGROUND_COLOR);
                break;
            case CommonCostants.BLACK_TICKETS_TEXT:
                label.setForeground(ScotColors.FERRY_COLOR);
                break;
            case CommonCostants.DOUBLE_MOVE_TICKETS_TEXT:
                label.setForeground(Color.WHITE);
                break;
            default:
                label.setForeground(ScotColors.ACCENT_COLOR);
        }
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createCurrentPlayerLabel() {
        final JLabel label = new JLabel(CommonCostants.CURRENT_PLAYER_TEXT);
        label.setFont(ScotFont.TEXT_FONT_14);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentY(CENTER_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JButton createActionButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(ScotFont.TEXT_FONT_14);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentY(CENTER_ALIGNMENT);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(SIDEBAR_WIDTH - 2 * PADDING, 40));
        return button;
    }

    /**
     * This method is called by the GameController. It creates the main JPanel
     * of a small window which displays the summary of the rules.
     * The window will be created by the GameController.
     *
     * @return the panel containing the rules
     */
    public JPanel createRulesPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        panel.setBackground(ScotColors.ACCENT_COLOR);
        final JTextArea textArea = new JTextArea();
        textArea.setFont(ScotFont.TEXT_FONT_14);
        textArea.setEditable(false);
        if (this.currentGameMode == GameMode.DETECTIVE) {
            String text =
                    "Nel gioco sono presenti : 1 detective, 1 fuggitivo (Mister X) e fino a un massimo di 3 bobby (aiutanti del detective).\n"
                            + "L'obiettivo del detective è catturare Mister X il prima possibile. Il gioco si compone di 23 round.\n"
                            + "La posizione di Mister X è nascosta, ma è possibile vedere quali mezzi usa nei suoi spostamenti (tranne se utilizza \n"
                            + "un biglietto nero) e la sua posizione viene rivelata dopo i turni : 3, 8, 13.\n"
                            + "Il detective e i bobby possono muoversi su tre mezzi di trasporto : taxi, bus, metropolitana.\n"
                            + "Il traghetto può essere usato solo da Mister X, con i biglietti neri. Il detective ha un numero limitato di biglietti per \n"
                            + "ciascun mezzo; mentre i bobby no. I vari giocatori non possono trovarsi in contemporanea su una stessa posizione della \n"
                            + "mappa. Se questo accade con un detective e Mister X, o con un bobby e Mister X, allora il gioco termina, con la vittoria \n"
                            + "del detective. \n"
                            + "Il gioco, invece, termina con la vittoria di Mister X se il detective e i bobby non lo hanno preso alla fine dell'ultimo \n"
                            + "round, oppure se il detective non può più muoversi (in un qualsiasi turno) a causa dell'esaurimento dei suoi biglietti.\n"
                            + "\nCOME SI GIOCA\n"
                            + "Ciascun giocatore nel proprio turno fa un solo spostamento, cliccando su uno dei nodi evidenziati di verde.\n"
                            + "Una volta selezionato il nodo di destinazione bisogna cliccare sul bottone \"Fine turno\".\n"
                            + "Quando viene rivelata la posizione di Mister X, questa fa riferimento alla posizione del turno precedente, in quanto, nel\n"
                            + "frattempo Mister X si è spostato.";
            textArea.append(text);
        }
        if (this.currentGameMode == GameMode.MISTER_X) {
            String text =
                    "Nel gioco sono presenti : 1 detective, 1 fuggitivo (Mister X) e fino a un massimo di 3 bobby (aiutanti del detective).\n"
                            + "L'obiettivo di Mister X è non farsi catturare dal detective o dai bobbies. Il gioco si compone di 23 round.\n"
                            + "La posizione di Mister X è nascosta, ma l'avversario può vedere quali mezzi usa nei suoi spostamenti (tranne se utilizza \n"
                            + "un biglietto nero) e la sua posizione viene rivelata dopo i turni : 3, 8, 13.\n"
                            + "Mister X può utilizzare i tre mezzi di trasporto principali (taxi, bus, metropolitana) senza limiti, dato che ha dei \n"
                            + "biglietti infiniti per questi mezzi. Possiede, però, dei biglietti speciali : \n"
                            + "- biglietto doppia mossa, che gli consente di fare due spostamenti (con mezzi diversi) in un unico turno\n"
                            + "- biglietto nero, che nasconde il mezzo utilizzato e che consente l'uso del traghetto (non usabile altrimenti)\n"
                            + "Il gioco termina con la vittoria del detective se Mister X viene catturato entro la fine dell'ultimo round.\n"
                            + "Altrimenti, il gioco termina con la vittoria di Mister X se non viene catturato entro la fine dell'ultimo round, oppure se\n"
                            + "in un qualsiasi turno il detective non può più effettuare spostamenti.\n"
                            + "\nCOME SI GIOCA\n"
                            + "Ciascun giocatore nel proprio turno fa un solo spostamento, cliccando su uno dei nodi evidenziati di verde.\n"
                            + "Una volta selezionato il nodo di destinazione bisogna cliccare sul bottone \"Fine turno\".\n"
                            + "Se si vuole effettuare una doppia mossa, bisogna cliccare sul bottone \"Doppia Mossa\" e seguire le istruzioni.";
            textArea.append(text);
        }
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(ScotColors.BACKGROUND_COLOR);
        panel.add(textArea, BorderLayout.CENTER);
        return panel;
    }

    private JButton createLoadRulesButton(final String text) {
        final JButton button = new JButton(text);
        button.setFont(ScotFont.TEXT_FONT_14);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentY(BOTTOM_ALIGNMENT);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                gameView.displayRulesWindow(createRulesPanel());
            }
        });
        return button;
    }

    /**
     * Sets the game mode label, according to the game mode selected.
     *
     * @param gameMode the game mode chosen by the user
     */
    public void setGameModeLabel(final GameMode gameMode) {
        this.currentGameMode = gameMode;
        if (this.currentGameMode == GameMode.DETECTIVE) {
            this.currentGameModeLabel.setText("Detective");
        } else if (this.currentGameMode == GameMode.MISTER_X) {
            this.currentGameModeLabel.setText("Mister X");
        }
    }

    /**
     * Method which is called by the GameController when updating the sideabar
     * displaying. Updates the
     * text of the roundLabel according to current roundNumber.
     *
     * @param roundNumber the current round number
     */
    public void updateRoundLabel(final int roundNumber) {
        this.roundLabel.setText("Round " + Integer.toString(roundNumber));
    }

    /**
     * Method which is called by the GameController when updating the sidebar
     * displaying. Updates the text of the
     * currentPlayerLabel according to the current player.
     */
    public void updateCurrentPlayerLabel(Player player) {
        this.currentPlayerLabel.setText(CommonCostants.CURRENT_PLAYER_TEXT + player.getName());
    }

    public void showElseHideDoubleMoveButton(final boolean show) {
        doubleMoveButton.setVisible(show);
    }

    /**
     * Method which is called by the GameController when updating the sidebar
     * displaying. Updates the
     * text of the taxiTicketsLabel according to current number of taxi tickets
     * possessed by the user
     * player.
     *
     * @param tickets the number of tickets
     */
    public void updateTaxiTicketsLabel(final int tickets) {
        this.taxiTicketsLabel.setText(getTicketCountLabel(CommonCostants.TAXI_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar
     * displaying. Updates the
     * text of the busTicketsLabel according to current number of bus tickets
     * possessed by the user
     * player.
     *
     * @param tickets the number of tickets
     */
    public void updateBusTicketsLabel(final int tickets) {
        this.busTicketsLabel.setText(getTicketCountLabel(CommonCostants.BUS_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar
     * displaying. Updates the
     * text of the undergroundTicketsLabel according to current number of
     * underground tickets
     * possessed by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateUndergroundTicketsLabel(final int tickets) {
        this.undergroundTicketsLabel.setText(getTicketCountLabel(CommonCostants.UNDERGROUND_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar
     * displaying. Updates the
     * text of the blackTicketsLabel according to current number of black tickets
     * possessed by the
     * user player.
     *
     * @param tickets the number of tickets
     */
    public void updateBlackTicketsLabel(final int tickets) {
        this.blackTicketsLabel.setText(getTicketCountLabel(CommonCostants.BLACK_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar
     * displaying. Updates the
     * text of the doubleMoveTicketsLabel according to current number of double move
     * tickets possessed
     * by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateDoubleMoveTicketsLabel(final int tickets) {
        this.doubleMoveTicketsLabel.setText(getTicketCountLabel(CommonCostants.DOUBLE_MOVE_TICKETS_TEXT, tickets));
    }

    /**
     * Generates the label to display the number of tickets available with special
     * handling of the
     * infinite amount.
     *
     * @param ticketType the description of the type of ticket
     * @param tickets    the number of tickets
     * @return the ticket label
     */
    private String getTicketCountLabel(final String ticketType, final int tickets) {
        final String label;
        if (tickets == -1) {
            label = "%s : infiniti".formatted(ticketType);
        } else {
            label = "%s : %d".formatted(ticketType, tickets);
        }

        return label;
    }

    /**
     * Sets the listener for the end turn button.
     *
     * @param listener the action listener
     */
    public void setEndTurnListener(ActionListener listener) {
        // rimuove tutti i listener
        for (ActionListener al : endTurnButton.getActionListeners()) {
            endTurnButton.removeActionListener(al);
        }
        endTurnButton.addActionListener(listener);
    }

    /**
     * Updates the end turn button state.
     *
     * @param enabled true to enable button
     */
    public void updateEndTurnButton(boolean enabled) {
        endTurnButton.setEnabled(enabled);
    }

    public void enableEndTurnButton(boolean value) {
        this.endTurnButton.setEnabled(value);
    }

    /**
     * Sets the listener for the double move button.
     *
     * @param listener the action listener
     */
    public void setDoubleMoveListener(ActionListener listener) {
        // rimuove tutti i listener
        for (ActionListener al : doubleMoveButton.getActionListeners()) {
            doubleMoveButton.removeActionListener(al);
        }
        doubleMoveButton.addActionListener(listener);
    }

    /**
     * Shows or hides the game action buttons (for MrX mode).
     *
     * @param visible true to show buttons, false to hide
     */
    public void setActionButtonsVisible(boolean visible) {
        endTurnButton.setVisible(visible);
        doubleMoveButton.setVisible(visible);
    }

    /**
     * Updates the double move button state and text.
     *
     * @param enabled true to enable button
     * @param text    the button text
     */
    public void updateDoubleMoveButton(boolean enabled, String text) {
        doubleMoveButton.setEnabled(enabled);
        doubleMoveButton.setText(text);
    }
}
