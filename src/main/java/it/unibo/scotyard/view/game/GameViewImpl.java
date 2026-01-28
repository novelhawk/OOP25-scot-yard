package it.unibo.scotyard.view.game;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.controller.game.GameController;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.resources.IconRegistry;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import it.unibo.scotyard.view.tracker.TrackerPanel;
import it.unibo.scotyard.view.window.Window;
import it.unibo.scotyard.view.window.WindowImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The game view
 *
 */
public final class GameViewImpl implements GameView {

    private static final int SMALL_WINDOW_WIDTH = 300;
    private static final int SMALL_WINDOW_HEIGHT = 100;

    private static final int SPACING = 50;
    private static final int SMALL_SPACING = 10;

    private static final String RULES_WINDOW_TITLE = "Regole";
    private static final String GAME_OVER_WINDOW_TITLE = "Game Over";
    private static final String SELECTION_JDIALOG_TITLE = "Selezione mezzo di trasporto";
    private static final String TAXI_TEXT = "Taxi";
    private static final String BUS_TEXT = "Bus";
    private static final String UNDERGROUND_TEXT = "Metro";
    private static final String FERRY_TEXT = "Traghetto";

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    private static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font SMALL_TEXT_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font WINNER_FONT = new Font("Arial", Font.BOLD, 28);

    private final IconRegistry iconRegistry;
    private final MapPanel mapPanel;
    private final SidebarPanel sidebar;
    private final JPanel mainPanel;
    private Window gameOverWindow;
    private JPanel gameOverPanel;

    private JLabel winnerLabel;

    private GameController observer;

    private TransportType selectedTransportType;

    /**
     * Creates a new game view
     *
     * @param mapInfo the game map
     */
    public GameViewImpl(final IconRegistry iconRegistry, final MapInfo mapInfo) {
        this.iconRegistry = iconRegistry;
        this.mapPanel = new MapPanel(mapInfo, this);
        this.sidebar = new SidebarPanel(this);
        this.createGameOverWindow();

        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.sidebar, BorderLayout.EAST);
        this.mainPanel.add(this.mapPanel, BorderLayout.CENTER);
    }

    @Override
    public void setObserver(GameController gameController) {
        this.observer = gameController;
    }

    @Override
    public TrackerPanel getTrackerPanel() {
        return sidebar.getTrackerPanel();
    }

    @Override
    public IconRegistry getIconRegistry() {
        return iconRegistry;
    }

    @Override
    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    @Override
    public SidebarPanel getSidebar() {
        return this.sidebar;
    }

    @Override
    public MapPanel getMapPanel() {
        return this.mapPanel;
    }

    @Override
    public void displayRulesWindow(final JPanel panel) {
        final Size smallSize = Size.of(SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT);
        final Window rulesWindow = new WindowImpl(smallSize, panel, RULES_WINDOW_TITLE);
        rulesWindow.setsMainFeatures(smallSize);
        rulesWindow.setHideOnClose();
        rulesWindow.display();
    }

    public void createGameOverWindow() {
        final Size smallSize = Size.of(SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT);
        this.gameOverPanel = new JPanel();
        this.gameOverPanel.setLayout(new BoxLayout(this.gameOverPanel, BoxLayout.Y_AXIS));
        this.gameOverPanel.setBackground(ScotColors.BACKGROUND_COLOR);
        this.gameOverPanel.add(Box.createVerticalGlue());
        JLabel titleLabel = new JLabel("GAME OVER!");
        titleLabel.setForeground(ScotColors.ACCENT_COLOR);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameOverPanel.add(titleLabel);
        this.gameOverPanel.add(Box.createVerticalStrut(SMALL_SPACING));
        this.winnerLabel = new JLabel();
        this.winnerLabel.setForeground(ScotColors.ACCENT_COLOR);
        this.winnerLabel.setFont(WINNER_FONT);
        this.winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameOverPanel.add(this.winnerLabel);
        this.gameOverPanel.add(Box.createVerticalStrut(SPACING));
        JButton button = new JButton("Ritorna al menù principale");
        button.setFont(TEXT_FONT);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameOverPanel.add(button);
        this.gameOverPanel.add(Box.createVerticalStrut(SPACING));
        this.gameOverPanel.add(Box.createVerticalGlue());

        this.gameOverWindow = new WindowImpl(smallSize, this.gameOverPanel, GAME_OVER_WINDOW_TITLE);
        this.gameOverWindow.setsMainFeatures(smallSize);

        button.addActionListener(e -> {
            observer.loadMainMenu();
            gameOverWindow.close();
        });
    }

    private void setResult(String result) {
        this.winnerLabel.setText(result);
        if (new String("Vittoria").equals(result)) {
            this.winnerLabel.setForeground(Color.GREEN);
        } else {
            this.winnerLabel.setForeground(Color.RED);
        }
    }

    @Override
    public void displayGameOverWindow(String result) {
        displayGameOverWindow(result, 0, null, false, null);
    }

    @Override
    public void loadTransportSelectionDialog(Set<TransportType> availableTransportTypes) {
        JDialog selectionWindow = new JDialog();
        selectionWindow.setBackground(Color.WHITE);
        selectionWindow.setTitle(SELECTION_JDIALOG_TITLE);
        selectionWindow.setSize(new Dimension(SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT));
        selectionWindow.setLayout(new BorderLayout());

        JLabel textLabel = new JLabel("Selezionare mezzo di trasporto");
        textLabel.setForeground(ScotColors.BACKGROUND_COLOR);
        textLabel.setFont(SMALL_TEXT_FONT);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectionWindow.add(textLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        for (TransportType transport : availableTransportTypes) {
            JButton button = new JButton();
            button.setForeground(ScotColors.BACKGROUND_COLOR);
            button.setFont(SMALL_TEXT_FONT);
            switch (transport) {
                case TAXI:
                    button.setText(TAXI_TEXT);
                    button.setBackground(Constants.TAXI_COLOR);
                    break;
                case BUS:
                    button.setText(BUS_TEXT);
                    button.setBackground(Constants.BUS_COLOR);
                    break;
                case UNDERGROUND:
                    button.setText(UNDERGROUND_TEXT);
                    button.setBackground(Constants.UNDERGROUND_COLOR);
                    break;
                case FERRY:
                    button.setText(FERRY_TEXT);
                    button.setBackground(Constants.FERRY_COLOR);
                    break;
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (transport) {
                        case TAXI:
                            selectedTransportType = TransportType.TAXI;
                            break;
                        case BUS:
                            selectedTransportType = TransportType.BUS;
                            break;
                        case UNDERGROUND:
                            selectedTransportType = TransportType.UNDERGROUND;
                            break;
                        case FERRY:
                            selectedTransportType = TransportType.FERRY;
                            break;
                    }
                    selectionWindow.dispose();
                    observer.selectTransport(selectedTransportType);
                    getSidebar().enableEndTurnButton(true);
                }
            });
            buttonsPanel.add(button);
        }
        selectionWindow.add(buttonsPanel, BorderLayout.CENTER);
        selectionWindow.setVisible(true);
    }

    @Override
    public void destinationChosen(NodeId destinationId) {
        this.observer.destinationChosen(destinationId);
        this.getMapPanel().repaint();
    }

    @Override
    public void displayGameOverWindow(
            final String result,
            final long gameDuration,
            final it.unibo.scotyard.model.game.GameMode gameMode,
            final boolean isNewRecord,
            final String currentRecord) {
        this.setResult(result);

        // rimuovi eventuali label di durata precedenti
        Component[] components = this.gameOverPanel.getComponents();
        for (int i = components.length - 1; i >= 0; i--) {
            Component comp = components[i];
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                String text = label.getText();
                if (text != null && (text.contains("Durata") || text.contains("Record") || text.contains("NUOVO"))) {
                    this.gameOverPanel.remove(comp);
                }
            }
        }

        // se ci sono dati di gioco, mostra il messaggio
        if (gameDuration > 0 && gameMode != null) {
            // formatta durata
            final String formattedDuration = formatDuration(gameDuration);

            // Riga 1: durata partita
            final JLabel durationLabel = new JLabel("Durata partita: " + formattedDuration);
            durationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            durationLabel.setForeground(new Color(200, 200, 200));
            durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            addLabelAfterWinner(durationLabel);

            addVerticalSpace(5);

            // Riga 2: nuovo record o record esistente
            if (isNewRecord) {
                final JLabel recordLabel = new JLabel("🏆 NUOVO RECORD per modalità "
                        + (gameMode == it.unibo.scotyard.model.game.GameMode.DETECTIVE ? "Detective" : "Mr. X") + "!");
                recordLabel.setFont(new Font("Arial", Font.BOLD, 18));
                recordLabel.setForeground(new Color(255, 215, 0)); // Gold
                recordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                addLabelAfterWinner(recordLabel);
            } else if (currentRecord != null) {
                final JLabel recordLabel = new JLabel("Record modalità "
                        + (gameMode == it.unibo.scotyard.model.game.GameMode.DETECTIVE ? "Detective" : "Mr. X") + ": "
                        + currentRecord);
                recordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                recordLabel.setForeground(new Color(200, 200, 200));
                recordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                addLabelAfterWinner(recordLabel);
            }

            this.gameOverPanel.revalidate();
            this.gameOverPanel.repaint();
        }

        this.gameOverWindow.display();
    }

    private void addLabelAfterWinner(final JLabel label) {
        for (int i = 0; i < this.gameOverPanel.getComponentCount(); i++) {
            if (this.gameOverPanel.getComponent(i) == this.winnerLabel) {
                this.gameOverPanel.add(label, i + 1);
                return;
            }
        }
        // Fallback: aggiungi comunque
        this.gameOverPanel.add(label);
    }

    private void addVerticalSpace(final int height) {
        for (int i = 0; i < this.gameOverPanel.getComponentCount(); i++) {
            if (this.gameOverPanel.getComponent(i) == this.winnerLabel) {
                this.gameOverPanel.add(Box.createVerticalStrut(height), i + 1);
                return;
            }
        }
    }

    private String formatDuration(final long millis) {
        if (millis <= 0) {
            return "00:00:00";
        }
        final long seconds = millis / 1000;
        final long hours = seconds / 3600;
        final long minutes = (seconds % 3600) / 60;
        final long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
