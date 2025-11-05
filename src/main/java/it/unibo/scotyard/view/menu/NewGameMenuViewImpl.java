package it.unibo.scotyard.view.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.menu.NewGameMenuController;

/**
 * start menu view
 */
public final class NewGameMenuViewImpl extends JFrame implements NewGameMenuView {

    private static final long serialVersionUID = 1L;

    // Window properties
    private static final String WINDOW_TITLE = "Scotland Yard - New Game Menu";

    // Component sizes
    private static final int COMBO_WIDTH = 200;
    private static final int COMBO_HEIGHT = 30;

    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(62, 39, 35);
    private static final Color ACCENT_COLOR = new Color(255, 171, 145);

    // Typography
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 20);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font USER_FONT = new Font("Arial", Font.PLAIN, 16);

    // UI text
    private static final String PLAYER_NAME_TEXT = "Inserire nome";
    private static final String SELECT_GAME_MODE_TEXT = "Selezionare modalità";
    private static final String SELECT_GAME_DIFFICULTY_TEXT = "Selezionare difficoltà";
    private static final String START_BUTTON_TEXT = "Avvia gioco";
    private static final String GO_BACK_BUTTON_TEXT = "Torna indietro";
    private static final String [] GAME_MODES_STRINGS = {"Mister X", "Detective"};
    private static final String [] DIFFICULTY_LEVELS_STRINGS = {"Facile", "Media", "Difficile"};

    // Layout spacing
    private static final int SMALL_SPACING = 20;
    private static final int DOUBLE_SPACING = 40;
    

    private final NewGameMenuController controller;
    private final Size resolution;

    /**
     * Creates the start new game menu view.
     * 
     * @param controller the menu controller
     * @param resolution the window size
     * @throws NullPointerException if any parameter is null
     */
    public NewGameMenuViewImpl(final NewGameMenuController controller, final Size resolution) {
        super(WINDOW_TITLE);
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        this.resolution = Objects.requireNonNull(resolution, "Resolution cannot be null");

        setupWindow();
        buildUI();
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void close() {
        dispose();
    }

    // Window properties
    private void setupWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(this.resolution.getWidth(), this.resolution.getHeight());
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // UI components
    private void buildUI() {
        final JPanel mainPanel = createMainPanel();

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(createPlayerNameLabel());
        mainPanel.add(Box.createVerticalStrut(SMALL_SPACING));
        mainPanel.add(createPlayerNameTextField());
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

        mainPanel.add(createStartGameButton(gameModeComboBox, difficultyLevelComboBox));
        mainPanel.add(Box.createVerticalStrut(DOUBLE_SPACING));
        mainPanel.add(createGoBackButton());
        mainPanel.add(Box.createVerticalGlue());
        
        setContentPane(mainPanel);
    }

    // Main container panel
    private JPanel createMainPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    // Insert player name label
    private JLabel createPlayerNameLabel(){
        final JLabel label = new JLabel(PLAYER_NAME_TEXT);
        label.setFont(LABEL_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // Insert player name textfield
    private JTextField createPlayerNameTextField(){
        final JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        textField.setFont(USER_FONT);
        textField.setForeground(BACKGROUND_COLOR);
        textField.setAlignmentX(CENTER_ALIGNMENT);
        return textField;
    }

    // Selection game mode label
    private JLabel createSelectGameModeLabel(){
        final JLabel label = new JLabel(SELECT_GAME_MODE_TEXT);
        label.setFont(LABEL_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // Selection game mode combo box
    private JComboBox<?> createSelectionGameModeComboBox(){
        final JComboBox<?> comboBox = new JComboBox<>(GAME_MODES_STRINGS);
        comboBox.setFont(USER_FONT);
        comboBox.setForeground(BACKGROUND_COLOR);
        comboBox.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        return comboBox;
    }

    // Seleceted game mode String
    private String getSelectedGameMode(JComboBox<?> comboBox){
        return comboBox.getSelectedItem().toString();
    }

    // Selection game difficulty label
    private JLabel createSelectGameDifficultyLabel(){
        final JLabel label = new JLabel(SELECT_GAME_DIFFICULTY_TEXT);
        label.setFont(LABEL_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // Selection game mode combo box
    private JComboBox<?> createSelectionDifficultyLevelComboBox(){
        final JComboBox<?> comboBox = new JComboBox<>(DIFFICULTY_LEVELS_STRINGS);
        comboBox.setFont(USER_FONT);
        comboBox.setForeground(BACKGROUND_COLOR);
        comboBox.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        return comboBox;
    }

    // Seleceted difficulty level String
    private String getSelectedDifficultyLevel(JComboBox<?> comboBox){
        return comboBox.getSelectedItem().toString();
    }

    // Start game button
    private JButton createStartGameButton(JComboBox<?> gameModeComboBox, JComboBox<?> difficultyLevelComboBox) {
        final JButton button = new JButton(START_BUTTON_TEXT);
        button.setFont(BUTTON_FONT);
        button.setForeground(BACKGROUND_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            System.out.println(getSelectedGameMode(gameModeComboBox));
            this.controller.play(getSelectedGameMode(gameModeComboBox), getSelectedDifficultyLevel(difficultyLevelComboBox),"Ciao");
            close();
        });
        return button;
    }

    // Go back button
    private JButton createGoBackButton(){
        final JButton button = new JButton(GO_BACK_BUTTON_TEXT);
        button.setFont(BUTTON_FONT);
        button.setForeground(BACKGROUND_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            close();
            this.controller.mainMenu();
        });
        return button;
    }

}