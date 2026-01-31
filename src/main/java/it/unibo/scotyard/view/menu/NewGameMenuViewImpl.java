package it.unibo.scotyard.view.menu;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.commons.patterns.CommonCostants;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
import it.unibo.scotyard.controller.menu.NewGameMenuController;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Start menu view.
 *
 */
public final class NewGameMenuViewImpl implements NewGameMenuView {

    // Component sizes
    private static final int COMBO_WIDTH = 200;
    private static final int COMBO_HEIGHT = 30;

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
        panel.setBackground(ScotColors.BACKGROUND_COLOR);
        return panel;
    }

    @Override
    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    // Insert player name label
    private JLabel createPlayerNameLabel() {
        final JLabel label = new JLabel(CommonCostants.PLAYER_NAME_TEXT);
        label.setFont(ScotFont.TEXT_FONT_20);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Insert player name textfield
    private JTextField createPlayerNameTextField() {
        final JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        textField.setFont(ScotFont.TEXT_FONT_16);
        textField.setForeground(ScotColors.BACKGROUND_COLOR);
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        return textField;
    }

    // Selection game mode label
    private JLabel createSelectGameModeLabel() {
        final JLabel label = new JLabel(CommonCostants.SELECT_GAME_MODE_TEXT);
        label.setFont(ScotFont.TEXT_FONT_20);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Selection game mode combo box
    private JComboBox<?> createSelectionGameModeComboBox() {
        final JComboBox<?> comboBox = new JComboBox<>(CommonCostants.GAME_MODES_STRINGS);
        comboBox.setFont(ScotFont.TEXT_FONT_16);
        comboBox.setForeground(ScotColors.BACKGROUND_COLOR);
        comboBox.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        return comboBox;
    }

    // Seleceted game mode String
    private String getSelectedGameMode(final JComboBox<?> comboBox) {
        return comboBox.getSelectedItem().toString();
    }

    // Selection game difficulty label
    private JLabel createSelectGameDifficultyLabel() {
        final JLabel label = new JLabel(CommonCostants.SELECT_GAME_DIFFICULTY_TEXT);
        label.setFont(ScotFont.TEXT_FONT_20);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Selection game mode combo box
    private JComboBox<?> createSelectionDifficultyLevelComboBox() {
        final JComboBox<?> comboBox = new JComboBox<>(CommonCostants.DIFFICULTY_LEVELS_STRINGS);
        comboBox.setFont(ScotFont.TEXT_FONT_16);
        comboBox.setForeground(ScotColors.BACKGROUND_COLOR);
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
        final JButton button = new JButton(CommonCostants.START_BUTTON_TEXT);
        button.setFont(ScotFont.TEXT_FONT_20);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setBackground(ScotColors.ACCENT_COLOR);
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
        final JButton button = new JButton(CommonCostants.GO_BACK_BUTTON_TEXT);
        button.setFont(ScotFont.TEXT_FONT_20);
        button.setForeground(ScotColors.BACKGROUND_COLOR);
        button.setBackground(ScotColors.ACCENT_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.mainMenu();
        });
        return button;
    }
}
