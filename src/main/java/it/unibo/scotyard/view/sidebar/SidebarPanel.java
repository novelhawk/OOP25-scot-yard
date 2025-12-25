package it.unibo.scotyard.view.sidebar;

import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.view.game.GameView;
import java.awt.BorderLayout;
import java.awt.Color;
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
    private static final String BASE_FONT_FAMILY = "Arial";
    private static final Font TITLE_FONT = new Font(BASE_FONT_FAMILY, Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font(BASE_FONT_FAMILY, Font.BOLD, 18);
    private static final Font TEXT_FONT = new Font(BASE_FONT_FAMILY, Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font(BASE_FONT_FAMILY, Font.BOLD, 14);

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

    // Components
    private JLabel currentGameModeLabel;
    private JLabel roundLabel;
    private JLabel taxiTicketsLabel;
    private JLabel busTicketsLabel;
    private JLabel undergroundTicketsLabel;
    private JLabel blackTicketsLabel;
    private JLabel doubleMoveTicketsLabel;
    private JButton loadRulesButton;

    private GameView gameView;
    private GameMode currentGameMode;

    /**
     * Creates a sidebar panel.
     *
     * @param gameView the game view
     */
    public SidebarPanel(final GameView gameView) {
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

        this.loadRulesButton = createLoadRulesButton(LOAD_RULES_TEXT);
        this.add(loadRulesButton);

        this.add(Box.createVerticalGlue());
    }

    private JLabel createCurrentGameModeLabel() {
        final JLabel label = new JLabel("Player");
        label.setFont(TITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createCountRoundLabel() {
        final JLabel label = new JLabel("Round : ");
        label.setFont(SUBTITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createInventoryLabel() {
        final JLabel label = new JLabel(INVENTORY_TEXT);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createTicketLabel(final String text) {
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
        label.setAlignmentY(TOP_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
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
        panel.setBackground(BACKGROUND_COLOR);
        final JTextArea textArea = new JTextArea();
        textArea.setFont(TEXT_FONT);
        if (this.currentGameMode == GameMode.DETECTIVE) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(
                    "Nel gioco sono presenti : 1 detective, 1 fuggitivo (Mister X) e 2 o 3 bobby (aiutanti del detective).\n");
            stringBuilder.append(
                    "L'obiettivo del detective è catturare Mister X il prima possibile. Il gioco si compone di 23 round.\n");
            stringBuilder.append(
                    "La posizione di Mister X è nascosta, ma è possibile vedere quali mezzi usa nei suoi spostamenti (tranne \n");
            stringBuilder.append(
                    "se utilizza un biglietto nero) e la sua posizione viene rivelata dopo i turni : 3, 8, 13.\n");
            stringBuilder.append(
                    "Il detective e i bobby possono muoversi su tre mezzi di trasporto : taxi, bus, metropolitana.\n");
            stringBuilder.append(
                    "Il traghetto può essere usato solo da Mister X, con i biglietti neri. Il detective ha un numero limitato \n");
            stringBuilder.append(
                    "di biglietti per ciascun mezzo; mentre i bobby no. I vari giocatori non possono trovarsi in contemporanea \n");
            stringBuilder.append(
                    "su una stessa posizione della mappa. Se questo accade con un detective e Mister X, o con un bobby e \n");
            stringBuilder.append("Mister X, allora il gioco termina, con la vittoria del detective. \n");
            stringBuilder.append(
                    "Il gioco, invece, termina con la vittoria di Mister X se il detective e i bobby non lo hanno preso alla \n");
            stringBuilder.append(
                    "fine dell'ultimo round, oppure se il detective non può più muoversi (in un qualsiasi turno) a causa \n");
            stringBuilder.append("dell'esaurimento dei suoi biglietti.");
            textArea.append(stringBuilder.toString());
        }
        if (this.currentGameMode == GameMode.MISTER_X) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(
                    "Nel gioco sono presenti : 1 detective, 1 fuggitivo (Mister X) e 2 o 3 bobby (aiutanti del detective).\n");
            stringBuilder.append(
                    "L'obiettivo di Mister X è non farsi catturare dal detective e dai bobby. Il gioco si compone di 23 round.\n");
            stringBuilder.append(
                    "La posizione di Mister X è nascosta ma l'avversario può vedere quali mezzi usa nei suoi spostamenti e la \n");
            stringBuilder.append("sua posizione viene rivelata dopo i turni : 3, 8, 13.\n");
            stringBuilder.append(
                    "Mister X può utilizzare i tre mezzi di trasporto principali (taxi, bus, metropolitana) senza limiti, dato \n ");
            stringBuilder.append(
                    "che non ha dei biglietti infiniti per questi mezzi. Mister X possiede, però, dei biglietti speciali : \n");
            stringBuilder.append(
                    "- biglietto doppia mossa, che gli consente di fare due spostamenti (con mezzi diversi) in un unico turno\n");
            stringBuilder.append(
                    "- biglietto nero, che nasconde il mezzo utilizzato e che consente l'uso del traghetto\n");
            stringBuilder.append("  (non usaebile in altro modo).\n");
            stringBuilder.append(
                    "Il gioco termina con la vittoria del detective se Mister X viene catturato entro la fine dell'ultimo round.\n");
            stringBuilder.append(
                    "Altrimenti, il gioco termina con la vittoria di Mister X se non viene catturato entro la fine dell'ultimo \n");
            stringBuilder.append(
                    "round, oppure se, in un qualsiasi turno il detective non può più effettuare spostamenti.");
            textArea.setText(stringBuilder.toString());
        }
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setForeground(ACCENT_COLOR);
        panel.add(textArea, BorderLayout.CENTER);
        return panel;
    }

    private JButton createLoadRulesButton(final String text) {
        final JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
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
     * Method which is called by the GameController when updating the sideabar displaying. Updates the
     * text of the roundLabel according to current roundNumber.
     *
     * @param roundNumber the current round number
     */
    public void updateRoundLabel(final int roundNumber) {
        this.roundLabel.setText("Round " + Integer.toString(roundNumber));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the
     * text of the taxiTicketsLabel according to current number of taxi tickets possessed by the user
     * player.
     *
     * @param tickets the number of tickets
     */
    public void updateTaxiTicketsLabel(final int tickets) {
        this.taxiTicketsLabel.setText(getTicketCountLabel(TAXI_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the
     * text of the busTicketsLabel according to current number of bus tickets possessed by the user
     * player.
     *
     * @param tickets the number of tickets
     */
    public void updateBusTicketsLabel(final int tickets) {
        this.busTicketsLabel.setText(getTicketCountLabel(BUS_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the
     * text of the undergroundTicketsLabel according to current number of underground tickets
     * possessed by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateUndergroundTicketsLabel(final int tickets) {
        this.undergroundTicketsLabel.setText(getTicketCountLabel(UNDERGROUND_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the
     * text of the blackTicketsLabel according to current number of black tickets possessed by the
     * user player.
     *
     * @param tickets the number of tickets
     */
    public void updateBlackTicketsLabel(final int tickets) {
        this.blackTicketsLabel.setText(getTicketCountLabel(BLACK_TICKETS_TEXT, tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying. Updates the
     * text of the doubleMoveTicketsLabel according to current number of double move tickets possessed
     * by the user player.
     *
     * @param tickets the number of tickets
     */
    public void updateDoubleMoveTicketsLabel(final int tickets) {
        this.doubleMoveTicketsLabel.setText(getTicketCountLabel(DOUBLE_MOVE_TICKETS_TEXT, tickets));
    }

    /**
     * Generates the label to display the number of tickets available with special handling of the
     * infinite amount.
     *
     * @param ticketType the description of the type of ticket
     * @param tickets the number of tickets
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
}
