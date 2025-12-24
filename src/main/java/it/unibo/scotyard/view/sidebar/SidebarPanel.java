package it.unibo.scotyard.view.sidebar;

import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.view.game.GameView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/** Sidebar panel for game UI. Currently displays only background, ready for future content. */
public final class SidebarPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int SIDEBAR_WIDTH = 200;
    private static final int PADDING = 10;

    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0); // black
    private static final Color ACCENT_COLOR = new Color(31, 81, 255); // neon blue
    private static final Color TAXI_COLOR = new Color(255, 255, 85); // yellow
    private static final Color BUS_COLOR = new Color(58, 132, 36); // green
    private static final Color UNDERGROUND_COLOR = new Color(200, 43, 29); // red
    private static final Color FERRY_COLOR = new Color(128, 128, 128); // grey
    private static final Color DOUBLE_MOVE_COLOR = new Color(255, 255, 255); // white

    // Typography
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    // Layout spacing
    private static final int SPACING = 10;
    private static final int SMALL_SPACING = 6;

    // Texts
    private static final String INVENTORY_TEXT = "Inventario";
    private static final String TAXI_TICKETS_TEXT = "Biglietti taxi";
    private static final String BUS_TICKETS_TEXT = "Biglietti bus";
    private static final String UNDERGROUND_TICKETS_TEXT = "Biglietti metro";
    private static final String BLACK_TICKETS_TEXT = "Biglietti neri";
    private static final String DOUBLE_MOVE_TICKETS_TEXT = "Biglietti doppia mossa";
    private static final String LOAD_RULES_TEXT = "Regole";
    private static final String CURRENT_PLAYER_TEXT = "Turno di : ";

    // Components
    JLabel currentGameModeLabel;
    JLabel roundLabel;
    JLabel currentPlayerLabel;
    JLabel taxiTicketsLabel;
    JLabel busTicketsLabel;
    JLabel undergroundTicketsLabel;
    JLabel blackTicketsLabel;
    JLabel doubleMoveTicketsLabel;
    JButton endTurnButton;
    JButton loadRulesButton;
    JButton doubleMoveButton;

    GameView gameView;
    GameMode currentGameMode;

    /** Creates a sidebar panel. */
    public SidebarPanel(GameView gameView) {
        setupSidebar();
        buildContent();
        this.gameView = gameView;
    }

    // Configure sidebar properties
    private void setupSidebar() {
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setBackground(BACKGROUND_COLOR);
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
        this.taxiTicketsLabel = createTicketLabel(TAXI_TICKETS_TEXT);
        this.add(taxiTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.busTicketsLabel = createTicketLabel(BUS_TICKETS_TEXT);
        this.add(busTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.undergroundTicketsLabel = createTicketLabel(UNDERGROUND_TICKETS_TEXT);
        this.add(undergroundTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.blackTicketsLabel = createTicketLabel(BLACK_TICKETS_TEXT);
        this.add(blackTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.doubleMoveTicketsLabel = createTicketLabel(DOUBLE_MOVE_TICKETS_TEXT);
        this.add(doubleMoveTicketsLabel);
        this.add(Box.createVerticalStrut(SPACING));

        // TODO : aggiungere pannello per tracciamento posizioni e mezzi usati da Mister X

        this.doubleMoveButton = createActionButton("Doppia Mossa");
        this.add(doubleMoveButton);
        this.add(Box.createVerticalStrut(SMALL_SPACING));

        this.endTurnButton = createActionButton("Fine Turno");
        this.add(endTurnButton);
        this.add(Box.createVerticalStrut(SPACING));

        // Nasconde i bottony by default (da capire)
        this.doubleMoveButton.setVisible(false);

        this.loadRulesButton = createLoadRulesButton(LOAD_RULES_TEXT);
        this.add(loadRulesButton);

        this.add(Box.createVerticalGlue());
    }

    private JLabel createCurrentGameModeLabel() {
        final JLabel label = new JLabel("Player");
        label.setFont(TITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createCountRoundLabel() {
        final JLabel label = new JLabel("Round : ");
        label.setFont(SUBTITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createInventoryLabel() {
        final JLabel label = new JLabel(INVENTORY_TEXT);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createTicketLabel(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(TEXT_FONT);
        switch (text) {
            case TAXI_TICKETS_TEXT:
                label.setForeground(TAXI_COLOR);
                break;
            case BUS_TICKETS_TEXT:
                label.setForeground(BUS_COLOR);
                break;
            case UNDERGROUND_TICKETS_TEXT:
                label.setForeground(UNDERGROUND_COLOR);
                break;
            case BLACK_TICKETS_TEXT:
                label.setForeground(FERRY_COLOR);
                break;
            case DOUBLE_MOVE_TICKETS_TEXT:
                label.setForeground(DOUBLE_MOVE_COLOR);
                break;
            default:
                label.setForeground(ACCENT_COLOR);
        }
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createCurrentPlayerLabel() {
        final JLabel label = new JLabel(CURRENT_PLAYER_TEXT);
        label.setFont(TEXT_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton createActionButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(SIDEBAR_WIDTH - 2 * PADDING, 40));
        return button;
    }

    /*
     * This method is called by the GameController. It creates the main JPanel
     * of a small window which displays the summary of the rules.
     * The window will be created by the GameController.
     */
    public JPanel createRulesPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        panel.setBackground(BACKGROUND_COLOR);
        JTextArea textArea = new JTextArea();
        textArea.setFont(TEXT_FONT);
        if (GameMode.DETECTIVE.equals(this.currentGameMode)) {
            textArea.append(
                    "Nel gioco sono presenti : 1 detective, 1 fuggitivo (Mister X) e 2 o 3 bobby (aiutanti del detective).\n"
                            + "L'obiettivo del detective è catturare Mister X il prima possibile. Il gioco si compone di 23 round.\n"
                            + "La posizione di Mister X è nascosta, ma è possibile vedere quali mezzi usa nei suoi spostamenti (tranne \n"
                            + "se utilizza un biglietto nero) e la sua posizione viene rivelata dopo i turni : 3, 8, 13.\n"
                            + "Il detective e i bobby possono muoversi su tre mezzi di trasporto : taxi, bus, metropolitana.\n"
                            + "Il traghetto può essere usato solo da Mister X, con i biglietti neri. Il detective ha un numero limitato \n"
                            + "di biglietti per ciascun mezzo; mentre i bobby no. I vari giocatori non possono trovarsi in contemporanea \n"
                            + "su una stessa posizione della mappa. Se questo accade con un detective e Mister X, o con un bobby e \n"
                            + "Mister X, allora il gioco termina, con la vittoria del detective. \n"
                            + "Il gioco, invece, termina con la vittoria di Mister X se il detective e i bobby non lo hanno preso alla \n"
                            + "fine dell'ultimo round, oppure se il detective non può più muoversi (in un qualsiasi turno) a causa \n"
                            + "dell'esaurimento dei suoi biglietti.");
        }
        if (GameMode.MISTER_X.equals(this.currentGameMode)) {
            textArea.setText(
                    "Nel gioco sono presenti : 1 detective, 1 fuggitivo (Mister X) e 2 o 3 bobby (aiutanti del detective).\n"
                            + "L'obiettivo di Mister X è non farsi catturare dal detective e dai bobby. Il gioco si compone di 23 round.\n"
                            + "La posizione di Mister X è nascosta ma l'avversario può vedere quali mezzi usa nei suoi spostamenti e la \n"
                            + "sua posizione viene rivelata dopo i turni : 3, 8, 13.\n"
                            + "Mister X può utilizzare i tre mezzi di trasporto principali (taxi, bus, metropolitana) senza limiti, dato \n "
                            + "che non ha dei biglietti infiniti per questi mezzi. Mister X possiede, però, dei biglietti speciali : \n"
                            + "- biglietto doppia mossa, che gli consente di fare due spostamenti (con mezzi diversi) in un unico turno\n"
                            + "- biglietto nero, che nasconde il mezzo utilizzato e che consente l'uso del traghetto\n"
                            + "  (non usaebile in altro modo).\n"
                            + "Il gioco termina con la vittoria del detective se Mister X viene catturato entro la fine dell'ultimo round.\n"
                            + "Altrimenti, il gioco termina con la vittoria di Mister X se non viene catturato entro la fine dell'ultimo \n"
                            + "round, oppure se, in un qualsiasi turno il detective non può più effettuare spostamenti.");
        }
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setForeground(ACCENT_COLOR);
        panel.add(textArea, BorderLayout.CENTER);
        return panel;
    }

    private JButton createLoadRulesButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
    public void setGameModeLabel(GameMode gameMode) {
        this.currentGameMode = gameMode;
        if (GameMode.DETECTIVE.equals(this.currentGameMode)) {
            this.currentGameModeLabel.setText("Detective");
        } else {
            if (GameMode.MISTER_X.equals(this.currentGameMode)) {
                this.currentGameModeLabel.setText("Mister X");
            }
        }
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the text of the
     * roundLabel according to current roundNumber.
     *
     * @param roundNumber the current round number
     */
    public void updateRoundLabel(int roundNumber) {
        this.roundLabel.setText("Round " + Integer.toString(roundNumber));
    }

    /**
     * Method which is called by the GameController when updating the sidebar displaying. Updates the text of the
     * currentPlayerLabel according to the current player.
     */
    public void updateCurrentPlayerLabel(Player player) {
        this.currentPlayerLabel.setText(CURRENT_PLAYER_TEXT + player.getName());
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the text of the
     * taxiTicketsLabel according to current number of taxi tickets possessed by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateTaxiTicketsLabel(int tickets) {
        if (tickets == -1) {
            this.taxiTicketsLabel.setText(TAXI_TICKETS_TEXT + " : infiniti");
        } else {
            this.taxiTicketsLabel.setText(TAXI_TICKETS_TEXT + " : " + Integer.toString(tickets));
        }
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the text of the
     * busTicketsLabel according to current number of bus tickets possessed by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateBusTicketsLabel(int tickets) {
        if (tickets == -1) {
            this.busTicketsLabel.setText(BUS_TICKETS_TEXT + " : infiniti");
        } else {
            this.busTicketsLabel.setText(BUS_TICKETS_TEXT + " : " + Integer.toString(tickets));
        }
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the text of the
     * undergroundTicketsLabel according to current number of underground tickets possessed by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateUndergroundTicketsLabel(int tickets) {
        if (tickets == -1) {
            this.undergroundTicketsLabel.setText(UNDERGROUND_TICKETS_TEXT + " : infiniti");
        } else {
            this.undergroundTicketsLabel.setText(UNDERGROUND_TICKETS_TEXT + " : " + Integer.toString(tickets));
        }
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the text of the
     * blackTicketsLabel according to current number of black tickets possessed by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateBlackTicketsLabel(int tickets) {
        this.blackTicketsLabel.setText(BLACK_TICKETS_TEXT + " : " + Integer.toString(tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the text of the
     * doubleMoveTicketsLabel according to current number of double move tickets possessed by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateDoubleMoveTicketsLabel(int tickets) {
        this.doubleMoveTicketsLabel.setText(DOUBLE_MOVE_TICKETS_TEXT + " : " + Integer.toString(tickets));
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
     * @param text the button text
     */
    public void updateDoubleMoveButton(boolean enabled, String text) {
        doubleMoveButton.setEnabled(enabled);
        doubleMoveButton.setText(text);
    }
}
