package it.unibo.scotyard.view.menu;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.menu.MainMenuController;
import it.unibo.scotyard.model.game.record.GameRecord;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Objects;
import java.util.Optional;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * The main menu view
 *
 */
public final class MainMenuViewImpl implements MainMenuView {

    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0); // black
    private static final Color ACCENT_COLOR = new Color(31, 81, 255); // neon blue

    // Typography
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 20);

    // UI text
    private static final String TITLE_TEXT = "Scotland Yard";
    private static final String NEW_GAME_TEXT = "Nuova partita";
    private static final String LOAD_GAME_TEXT = "Carica partita";
    private static final String STATISTICS_TEXT = "Statistiche";
    private static final String EXIT_TEXT = "Esci";

    // Layout spacing
    private static final int TITLE_SPACING = 40;
    private static final int BUTTONS_SPACING = 20;

    private final MainMenuController controller;
    private final JPanel mainPanel;
    private final JPanel menuPanel; // Panel originale del menu

    /**
     * Creates the main menu view.
     *
     * @param controller the main menu controller
     * @param resolution the window size
     * @throws NullPointerException if any parameter is null
     */
    public MainMenuViewImpl(final MainMenuController controller, final Size resolution) {
        // super(WINDOW_TITLE);
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        // this.resolution = Objects.requireNonNull(resolution, "Resolution cannot be
        // null");

        this.mainPanel = new JPanel(new BorderLayout());
        this.menuPanel = createMenuPanel();
        buildUI();
        this.mainPanel.add(this.menuPanel, BorderLayout.CENTER);
    }

    @Override
    public void close() {
        this.controller.exit();
    }

    // UI components
    private void buildUI() {
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(createTitleLabel());
        menuPanel.add(Box.createVerticalStrut(TITLE_SPACING));
        menuPanel.add(createNewGameButton());
        menuPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        menuPanel.add(createLoadGameButton());
        menuPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        menuPanel.add(createStatisticsButton());
        menuPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        menuPanel.add(createExitButton());
        menuPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        menuPanel.add(Box.createVerticalGlue());
    }

    // Menu panel
    private JPanel createMenuPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    @Override
    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    @Override
    public void displayStatisticsTable(
            final Optional<GameRecord> detectiveRecord, final Optional<GameRecord> mrxRecord) {
        // panel statistiche con BoxLayout
        final JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(BACKGROUND_COLOR);

        // Titolo
        final JLabel titleLabel = new JLabel("STATISTICHE PARTITE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(titleLabel);

        // tabella
        final String[] columnNames = {"Modalità", "Tempo", "Data"};
        final Object[][] data = new Object[2][3];

        // Riga 1: Detective
        data[0][0] = "DETECTIVE";
        if (detectiveRecord.isPresent() && detectiveRecord.get().isValid()) {
            data[0][1] = formatDuration(detectiveRecord.get().getDurationMillis());
            data[0][2] = formatDate(detectiveRecord.get().getTimestamp());
        } else {
            data[0][1] = "Nessun record";
            data[0][2] = "-";
        }

        // Riga 2: Mr. X
        data[1][0] = "MR. X";
        if (mrxRecord.isPresent() && mrxRecord.get().isValid()) {
            data[1][1] = formatDuration(mrxRecord.get().getDurationMillis());
            data[1][2] = formatDate(mrxRecord.get().getTimestamp());
        } else {
            data[1][1] = "Nessun record";
            data[1][2] = "-";
        }

        // model non editabile
        final DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        final JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(40);
        table.setBackground(BACKGROUND_COLOR);
        table.setForeground(Color.WHITE);
        table.setGridColor(ACCENT_COLOR);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(ACCENT_COLOR);
        table.getTableHeader().setForeground(BACKGROUND_COLOR);
        table.getTableHeader().setReorderingAllowed(false);

        // Centra testo nelle celle
        final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(BACKGROUND_COLOR);
        centerRenderer.setForeground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Limita dimensione table
        table.setPreferredScrollableViewportSize(new Dimension(700, 100));

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMaximumSize(new Dimension(800, 150));
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 50, 30, 50));
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(scrollPane);

        // Pannello bottoni
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton backButton = new JButton("Indietro");
        backButton.setFont(BUTTON_FONT);
        backButton.setBackground(ACCENT_COLOR);
        backButton.setForeground(BACKGROUND_COLOR);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> showMenu());

        final JButton resetButton = new JButton("Resetta Record");
        resetButton.setFont(BUTTON_FONT);
        resetButton.setBackground(new Color(220, 20, 20)); // rosso
        resetButton.setForeground(Color.WHITE);
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(false);
        resetButton.addActionListener(e -> {
            final int confirm = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Sei sicuro di voler resettare tutti i record?\nQuesta operazione non può essere annullata!",
                    "Conferma Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                this.controller.resetRecords();
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(resetButton);
        statsPanel.add(buttonPanel);

        statsPanel.add(Box.createVerticalGlue());

        // Sostituisci contenuto mainPanel
        mainPanel.removeAll();
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showMenu() {
        // Torna al menu principale
        mainPanel.removeAll();
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
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

    private String formatDate(final String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return "-";
        }
        try {
            // Formato: "Dow Mon DD HH:MM:SS zzz YYYY"
            // Prendiamo: "Mon DD, YYYY HH:MM"
            final String[] parts = timestamp.split(" ");
            if (parts.length >= 5) {
                return parts[1] + " " + parts[2] + ", " + parts[5] + " " + parts[3].substring(0, 5);
            }
        } catch (Exception e) {
            // Fallback: ritorna timestamp completo
        }
        return timestamp;
    }

    @Override
    public void showResetConfirmation() {
        JOptionPane.showMessageDialog(
                mainPanel, "Tutti i record sono stati resettati!", "Reset Completato", JOptionPane.INFORMATION_MESSAGE);
        // Ricarica statistiche per mostrare dati aggiornati
        this.controller.showStatistics();
    }

    @Override
    public void showError(final String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    // Title label
    private JLabel createTitleLabel() {
        final JLabel label = new JLabel(TITLE_TEXT);
        label.setFont(TITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // New game button
    private JButton createNewGameButton() {
        final JButton button = new JButton(NEW_GAME_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.newGameMenu();
        });
        return button;
    }

    // Load game button
    private JButton createLoadGameButton() {
        final JButton button = new JButton(LOAD_GAME_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            // TO DO
        });
        return button;
    }

    // Statistics button
    private JButton createStatisticsButton() {
        final JButton button = new JButton(STATISTICS_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.showStatistics();
        });
        return button;
    }

    // Exit button
    private JButton createExitButton() {
        final JButton button = new JButton(EXIT_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            close();
        });
        return button;
    }
}
