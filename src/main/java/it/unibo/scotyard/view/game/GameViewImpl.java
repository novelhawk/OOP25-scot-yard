package it.unibo.scotyard.view.game;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.commons.patterns.CommonCostants;
import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
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
 * The game view.
 *
 */
public final class GameViewImpl implements GameView {

    private static final int SMALL_WINDOW_WIDTH = 300;

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
     * Creates a new game view.
     *
     * @param iconRegistry
     * @param mapInfo      the game map
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
    public void setObserver(final GameController gameController) {
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
        final Size smallSize = Size.of(SMALL_WINDOW_WIDTH, MagicNumbers.HEIGHT_100);
        final Window rulesWindow = new WindowImpl(smallSize, panel, CommonCostants.RULES_WINDOW_TITLE);
        rulesWindow.setsMainFeatures(smallSize);
        rulesWindow.setHideOnClose();
        rulesWindow.display();
    }

    public void createGameOverWindow() {
        final Size smallSize = Size.of(SMALL_WINDOW_WIDTH, MagicNumbers.HEIGHT_100);
        this.gameOverPanel = new JPanel();
        this.gameOverPanel.setLayout(new BoxLayout(this.gameOverPanel, BoxLayout.Y_AXIS));
        this.gameOverPanel.setBackground(ScotColors.BACKGROUND_COLOR);
        this.gameOverPanel.add(Box.createVerticalGlue());
        final JLabel titleLabel = new JLabel(CommonCostants.GAME_OVER_WINDOW_TITLE);
        titleLabel.setForeground(ScotColors.ACCENT_COLOR);
        titleLabel.setFont(ScotFont.TITLE_FONT_36);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameOverPanel.add(titleLabel);
        this.gameOverPanel.add(Box.createVerticalStrut(MagicNumbers.GAP_10));
        this.winnerLabel = new JLabel();
        this.winnerLabel.setForeground(ScotColors.ACCENT_COLOR);
        this.winnerLabel.setFont(ScotFont.TEXT_FONT_28);
        this.winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameOverPanel.add(this.winnerLabel);
        this.gameOverPanel.add(Box.createVerticalStrut(MagicNumbers.GAP_50));
        final JButton button = new JButton(CommonCostants.BACK_MAIN_MENU);
        button.setFont(ScotFont.TEXT_FONT_20);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameOverPanel.add(button);
        this.gameOverPanel.add(Box.createVerticalStrut(MagicNumbers.GAP_50));
        this.gameOverPanel.add(Box.createVerticalGlue());

        this.gameOverWindow = new WindowImpl(smallSize, this.gameOverPanel, CommonCostants.GAME_OVER_WINDOW_TITLE);
        this.gameOverWindow.setsMainFeatures(smallSize);

        button.addActionListener(e -> {
            observer.loadMainMenu();
            gameOverWindow.close();
        });
    }

    private void setResult(final String result) {
        this.winnerLabel.setText(result);
        if (new String(CommonCostants.WINNER_TEXT).equals(result)) {
            this.winnerLabel.setForeground(Color.GREEN);
        } else {
            this.winnerLabel.setForeground(Color.RED);
        }
    }

    @Override
    public void displayGameOverWindow(final String result) {
        displayGameOverWindow(result, 0, null, false, null);
    }

    @Override
    public void loadTransportSelectionDialog(final Set<TransportType> availableTransportTypes) {
        final JDialog selectionWindow = new JDialog();
        selectionWindow.setBackground(Color.WHITE);
        selectionWindow.setTitle(CommonCostants.SELECTION_TRANSPORT_JDIALOG);
        selectionWindow.setSize(new Dimension(SMALL_WINDOW_WIDTH, MagicNumbers.HEIGHT_100));
        selectionWindow.setLayout(new BorderLayout());

        final JLabel textLabel = new JLabel(CommonCostants.SELECTION_TRANSPORT_JDIALOG);
        textLabel.setForeground(ScotColors.BACKGROUND_COLOR);
        textLabel.setFont(ScotFont.TEXT_FONT_14);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectionWindow.add(textLabel, BorderLayout.NORTH);

        final JPanel buttonsPanel = new JPanel(new FlowLayout());
        for (final TransportType transport : availableTransportTypes) {
            final JButton button = new JButton();
            button.setForeground(ScotColors.BACKGROUND_COLOR);
            button.setFont(ScotFont.TEXT_FONT_14);
            switch (transport) {
                case TAXI:
                    button.setText(CommonCostants.TAXI_TEXT);
                    button.setBackground(ScotColors.TAXI_COLOR);
                    break;
                case BUS:
                    button.setText(CommonCostants.BUS_TEXT);
                    button.setBackground(ScotColors.BUS_COLOR);
                    break;
                case UNDERGROUND:
                    button.setText(CommonCostants.UNDERGROUND_TEXT);
                    button.setBackground(ScotColors.UNDERGROUND_COLOR);
                    break;
                case FERRY:
                    button.setText(CommonCostants.FERRY_TEXT);
                    button.setBackground(ScotColors.FERRY_COLOR);
                    break;
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
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
    public void destinationChosen(final NodeId destinationId) {
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
        final Component[] components = this.gameOverPanel.getComponents();
        for (int i = components.length - 1; i >= 0; i--) {
            final Component comp = components[i];
            if (comp instanceof JLabel) {
                final JLabel label = (JLabel) comp;
                final String text = label.getText();
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
            durationLabel.setFont(ScotFont.TEXT_FONT_16);
            durationLabel.setForeground(ScotColors.LONGEST_GAME); // gray
            durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            addLabelAfterWinner(durationLabel);

            addVerticalSpace(MagicNumbers.GAP_5);

            // Riga 2: nuovo record o record esistente
            if (isNewRecord) {
                final JLabel recordLabel = new JLabel("🏆 NUOVO RECORD per modalità "
                        + (gameMode == it.unibo.scotyard.model.game.GameMode.DETECTIVE ? "Detective" : "Mr. X") + "!");
                recordLabel.setFont(ScotFont.TEXT_FONT_18);
                recordLabel.setForeground(Color.ORANGE); // Orange
                recordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                addLabelAfterWinner(recordLabel);
            } else if (currentRecord != null) {
                final JLabel recordLabel = new JLabel("Record modalità "
                        + (gameMode == it.unibo.scotyard.model.game.GameMode.DETECTIVE ? "Detective" : "Mr. X") + ": "
                        + currentRecord);
                recordLabel.setFont(ScotFont.TEXT_FONT_16);
                recordLabel.setForeground(ScotColors.LONGEST_GAME);
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
            if (this.gameOverPanel.getComponent(i).equals(this.winnerLabel)) {
                this.gameOverPanel.add(label, i + 1);
                return;
            }
        }
        // Fallback: aggiungi comunque
        this.gameOverPanel.add(label);
    }

    private void addVerticalSpace(final int height) {
        for (int i = 0; i < this.gameOverPanel.getComponentCount(); i++) {
            if (this.gameOverPanel.getComponent(i).equals(this.winnerLabel)) {
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
        final long minutes = seconds % 3600 / 60;
        final long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
