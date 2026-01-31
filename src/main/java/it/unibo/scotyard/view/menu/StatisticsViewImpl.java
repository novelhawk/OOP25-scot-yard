package it.unibo.scotyard.view.menu;

import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.controller.menu.StatisticsController;
import it.unibo.scotyard.model.game.matchhistory.MatchHistory;
import it.unibo.scotyard.model.game.record.GameRecord;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StatisticsViewImpl extends JPanel implements StatisticsView {

    private final StatisticsController controller;
    private final GameRecord mrxRecord;
    private final GameRecord detectiveRecord;
    private final MatchHistory matchHistory;

    public StatisticsViewImpl(
            StatisticsController controller,
            GameRecord mrxRecord,
            GameRecord detectiveRecord,
            MatchHistory matchHistory) {
        this.controller = controller;
        this.mrxRecord = mrxRecord;
        this.detectiveRecord = detectiveRecord;
        this.matchHistory = matchHistory;

        setLayout(new GridBagLayout());
        setBackground(ScotColors.BACKGROUND_COLOR);
        buildContent();
    }

    public void buildContent() {
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 20, 0);
        c.fill = GridBagConstraints.NONE;

        add(pageTitleComponent(), c);

        c.gridy = 1;
        add(createTableComponent(), c);

        c.gridy = 2;
        add(createMatchHistoryComponent(), c);

        c.insets = new Insets(40, 0, 0, 0);
        c.gridy = 3;
        add(createActionButtonComponent(), c);

        c.gridy = 4;
        c.weighty = 1;
        add(Box.createHorizontalGlue(), c);
    }

    @Override
    public void showError(final String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showResetConfirmation() {
        JOptionPane.showMessageDialog(
                this, "Tutti i record sono stati resettati!", "Reset Completato", JOptionPane.INFORMATION_MESSAGE);
        // Ricarica statistiche per mostrare dati aggiornati
        this.controller.showView();
    }

    private Component pageTitleComponent() {
        final JLabel titleLabel = new JLabel(ViewConstants.STATISCS_TITLE.toUpperCase());
        titleLabel.setFont(ScotFont.TEXT_FONT_28);
        titleLabel.setForeground(ScotColors.ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titleLabel;
    }

    private Component gameRecapComponent(final String title, final int winCount, final int loseCount) {
        final Box frame = Box.createVerticalBox();
        frame.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(ScotFont.TEXT_FONT_16);
        titleLabel.setForeground(ScotColors.ACCENT_COLOR);
        frame.add(titleLabel);

        frame.add(Box.createVerticalStrut(15));

        final Box score = Box.createHorizontalBox();
        score.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel wins = new JLabel(String.valueOf(winCount));
        wins.setFont(ScotFont.TEXT_FONT_18);
        wins.setForeground(Color.green);
        score.add(wins);

        final JLabel separator = new JLabel(" / ");
        separator.setFont(ScotFont.TEXT_FONT_18);
        separator.setForeground(ScotColors.ACCENT_COLOR);
        score.add(separator);

        final JLabel loses = new JLabel(String.valueOf(loseCount));
        loses.setFont(ScotFont.TEXT_FONT_18);
        loses.setForeground(Color.RED);
        score.add(loses);

        frame.add(score);
        return frame;
    }

    private Component createTableComponent() {
        final String[] columnNames = {"Modalità", "Tempo", "Data"};
        final Object[][] data = new Object[2][3];

        // Riga 1: Detective
        data[0][0] = ViewConstants.DETECTIVE_STRING;
        if (detectiveRecord != null && detectiveRecord.isValid()) {
            data[0][1] = formatDuration(detectiveRecord.getDurationMillis());
            data[0][2] = formatDate(detectiveRecord.getTimestamp());
        } else {
            data[0][1] = "Nessun record";
            data[0][2] = "-";
        }

        // Riga 2: Mr. X
        data[1][0] = ViewConstants.MRX_STRING;
        if (mrxRecord != null && mrxRecord.isValid()) {
            data[1][1] = formatDuration(mrxRecord.getDurationMillis());
            data[1][2] = formatDate(mrxRecord.getTimestamp());
        } else {
            data[1][1] = "Nessun record";
            data[1][2] = "-";
        }

        // model non editabile
        final DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };

        final JTable table = new JTable(tableModel);
        table.setFont(ScotFont.TEXT_FONT_16);
        table.setRowHeight(MagicNumbers.GAP_40);
        table.setBackground(ScotColors.BACKGROUND_COLOR);
        table.setForeground(Color.WHITE);
        table.setGridColor(ScotColors.ACCENT_COLOR);
        table.getTableHeader().setFont(ScotFont.TEXT_FONT_18);
        table.getTableHeader().setBackground(ScotColors.ACCENT_COLOR);
        table.getTableHeader().setForeground(ScotColors.BACKGROUND_COLOR);
        table.getTableHeader().setReorderingAllowed(false);

        // Centra testo nelle celle
        final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(ScotColors.BACKGROUND_COLOR);
        centerRenderer.setForeground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Limita dimensione table
        table.setPreferredScrollableViewportSize(new Dimension(MagicNumbers.WIDTH_700, MagicNumbers.HEIGHT_100));

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMaximumSize(new Dimension(MagicNumbers.WIDTH_800, MagicNumbers.HEIGHT_150));
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(
                MagicNumbers.GAP_10, MagicNumbers.GAP_50, MagicNumbers.GAP_30, MagicNumbers.GAP_50));
        scrollPane.setBackground(ScotColors.BACKGROUND_COLOR);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        return scrollPane;
    }

    private Component createMatchHistoryComponent() {
        final JPanel blocksPanel = new JPanel(new GridLayout(1, 2, 100, 0));
        blocksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        blocksPanel.setBackground(ScotColors.BACKGROUND_COLOR);
        blocksPanel.add(
                gameRecapComponent(ViewConstants.MISTERX_GAMES, matchHistory.runnerWins(), matchHistory.runnerLoses()));
        blocksPanel.add(
                gameRecapComponent(ViewConstants.SEEKER_GAMES, matchHistory.seekerWins(), matchHistory.seekerLoses()));
        return blocksPanel;
    }

    private Component createActionButtonComponent() {
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 2, MagicNumbers.GAP_20, 0));
        buttonPanel.setBackground(ScotColors.BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton backButton = new JButton(ViewConstants.BACK);
        backButton.setFont(ScotFont.TEXT_FONT_20);
        backButton.setBackground(ScotColors.ACCENT_COLOR);
        backButton.setForeground(ScotColors.BACKGROUND_COLOR);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> this.controller.loadMainMenu());

        final JButton resetButton = new JButton(ViewConstants.RESET_RECORD_TEXT);
        resetButton.setFont(ScotFont.TEXT_FONT_20);
        resetButton.setBackground(Color.RED); // rosso
        resetButton.setForeground(Color.WHITE);
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(false);
        resetButton.addActionListener(e -> {
            final int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Sei sicuro di voler resettare tutti i record?\nQuesta operazione non può essere annullata!",
                    "Conferma Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                this.controller.resetStatistics();
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(resetButton);
        return buttonPanel;
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
}
