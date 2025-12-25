package it.unibo.scotyard.view.menu;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.menu.NewGameMenuController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Start menu view
 *
 */
public final class NewGameMenuViewImpl implements NewGameMenuView {

    // Component sizes
    private static final int COMBO_WIDTH = 200;
    private static final int COMBO_HEIGHT = 30;

    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0); // black
    private static final Color ACCENT_COLOR = new Color(31, 81, 255); // neon blue

    // Typography
    private static final String MAIN_FONT_FAMILY = "Arial";
    private static final Font LABEL_FONT = new Font(MAIN_FONT_FAMILY, Font.PLAIN, 20);
    private static final Font BUTTON_FONT = new Font(MAIN_FONT_FAMILY, Font.BOLD, 20);
    private static final Font USER_FONT = new Font(MAIN_FONT_FAMILY, Font.PLAIN, 16);

    // UI text
    private static final String PLAYER_NAME_TEXT = "Inserire nome";
    private static final String SELECT_GAME_MODE_TEXT = "Selezionare modalità";
    private static final String SELECT_GAME_DIFFICULTY_TEXT = "Selezionare difficoltà";
    private static final String START_BUTTON_TEXT = "Avvia gioco";
    private static final String GO_BACK_BUTTON_TEXT = "Torna indietro";
    private static final String[] GAME_MODES_STRINGS = {"Mister X", "Detective"};
    private static final String[] DIFFICULTY_LEVELS_STRINGS = {"Facile", "Media", "Difficile"};

    // Layout spacing
    private static final int SMALL_SPACING = 20;
    private static final int DOUBLE_SPACING = 40;

    private final NewGameMenuController controller;
    private final JPanel mainPanel;

    /**
     * Creates the start new game menu view.
     *
     * @param controller the menu controller
     * @param resolution the window size
     * @throws NullPointerException if any parameter is null
     */
    public NewGameMenuViewImpl(final NewGameMenuController controller, final Size resolution) {
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");

        this.mainPanel = createMainPanel();
        buildUI();
    }

    @Override
    public void close() {
        this.controller.exit();
    }

    // UI components
    private void buildUI() {
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(createPlayerNameLabel());
        mainPanel.add(Box.createVerticalStrut(SMALL_SPACING));
        final JTextField playerTextField = createPlayerNameTextField();
        mainPanel.add(playerTextField);
        mainPanel.add(Box.createVerticalStrut(DOUBLE_SPACING));

        mainPanel.add(createSelectGameModeLabel());
        mainPanel.add(Box.createVerticalStrut(SMALL_SPACING));
        final JComboBox<?> gameModeComboBox = createSelectionGameModeComboBox();
        mainPanel.add(gameModeComboBox);
        mainPanel.add(Box.createVerticalStrut(DOUBLE_SPACING));

        mainPanel.add(createSelectGameDifficultyLabel());
        mainPanel.add(Box.createVerticalStrut(SMALL_SPACING));
        final JComboBox<?> difficultyLevelComboBox = createSelectionDifficultyLevelComboBox();
        mainPanel.add(difficultyLevelComboBox);
        mainPanel.add(Box.createVerticalStrut(DOUBLE_SPACING));

        mainPanel.add(createStartGameButton(gameModeComboBox, difficultyLevelComboBox, playerTextField));
        mainPanel.add(Box.createVerticalStrut(DOUBLE_SPACING));
        mainPanel.add(createGoBackButton());
        mainPanel.add(Box.createVerticalGlue());
    }

    // Main container panel
    private JPanel createMainPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    @Override
    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    // Insert player name label
    private JLabel createPlayerNameLabel() {
        final JLabel label = new JLabel(PLAYER_NAME_TEXT);
        label.setFont(LABEL_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Insert player name textfield
    private JTextField createPlayerNameTextField() {
        final JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        textField.setFont(USER_FONT);
        textField.setForeground(BACKGROUND_COLOR);
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        return textField;
    }

    // Selection game mode label
    private JLabel createSelectGameModeLabel() {
        final JLabel label = new JLabel(SELECT_GAME_MODE_TEXT);
        label.setFont(LABEL_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Selection game mode combo box
    private JComboBox<?> createSelectionGameModeComboBox() {
        final JComboBox<?> comboBox = new JComboBox<>(GAME_MODES_STRINGS);
        comboBox.setFont(USER_FONT);
        comboBox.setForeground(BACKGROUND_COLOR);
        comboBox.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        return comboBox;
    }

    // Seleceted game mode String
    private String getSelectedGameMode(final JComboBox<?> comboBox) {
        return comboBox.getSelectedItem().toString();
    }

    // Selection game difficulty label
    private JLabel createSelectGameDifficultyLabel() {
        final JLabel label = new JLabel(SELECT_GAME_DIFFICULTY_TEXT);
        label.setFont(LABEL_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Selection game mode combo box
    private JComboBox<?> createSelectionDifficultyLevelComboBox() {
        final JComboBox<?> comboBox = new JComboBox<>(DIFFICULTY_LEVELS_STRINGS);
        comboBox.setFont(USER_FONT);
        comboBox.setForeground(BACKGROUND_COLOR);
        comboBox.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        return comboBox;
    }

    // Seleceted difficulty level String
    private String getSelectedDifficultyLevel(final JComboBox<?> comboBox) {
        return comboBox.getSelectedItem().toString();
    }

    // Player name String
    private String getPlayerName(final JTextField textField) {
        return textField.getText();
    }

    // Start game button
    private JButton createStartGameButton(
            final JComboBox<?> gameModeComboBox,
            final JComboBox<?> difficultyLevelComboBox,
            final JTextField playerTextField) {
        final JButton button = new JButton(START_BUTTON_TEXT);
        button.setFont(BUTTON_FONT);
        button.setForeground(BACKGROUND_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.play(
                    getSelectedGameMode(gameModeComboBox),
                    getSelectedDifficultyLevel(difficultyLevelComboBox),
                    getPlayerName(playerTextField));
        });
        return button;
    }

    // Go back button
    private JButton createGoBackButton() {
        final JButton button = new JButton(GO_BACK_BUTTON_TEXT);
        button.setFont(BUTTON_FONT);
        button.setForeground(BACKGROUND_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.mainMenu();
        });
        return button;
    }
}
