package it.unibo.scotyard.view.menu;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.controller.menu.MainMenuController;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;

/**
 * The main menu view
 *
 */
public final class MainMenuViewImpl implements MainMenuView {

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
        panel.setBackground(ScotColors.BACKGROUND_COLOR);
        return panel;
    }

    @Override
    public JPanel getMainPanel() {
        return this.mainPanel;
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
        final JLabel label = new JLabel(ViewConstants.SCOTLAND_YARD);
        label.setFont(ScotFont.TITLE_FONT_36);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // New game button
    private JButton createNewGameButton() {
        final JButton button = new JButton(ViewConstants.NEW_GAME_TEXT);
        button.setFont(ScotFont.TEXT_FONT_20);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.newGameMenu();
        });
        return button;
    }

    // Load game button
    private JButton createLoadGameButton() {
        final JButton button = new JButton(ViewConstants.LOAD_GAME_TEXT);
        button.setFont(ScotFont.TEXT_FONT_20);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            // TO DO
        });
        return button;
    }

    // Statistics button
    private JButton createStatisticsButton() {
        final JButton button = new JButton(ViewConstants.STATISTICS_TEXT);
        button.setFont(ScotFont.TEXT_FONT_20);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.showStatistics();
        });
        return button;
    }

    // Exit button
    private JButton createExitButton() {
        final JButton button = new JButton(ViewConstants.EXIT_TEXT);
        button.setFont(ScotFont.TEXT_FONT_20);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            close();
        });
        return button;
    }
}
