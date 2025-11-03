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
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    private static final Font SELECTION_FONT = new Font("Arial", Font.BOLD, 26);
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 20);

    // UI text
    private static final String TITLE_TEXT = "Scotland Yard";
    private static final String SELECT_GAME_MODE_TEXT = "Seleziona modalità";
    private static final String SELECT_GAME_DIFFICULTY_TEXT = "Seleziona difficoltà";
    private static final String START_BUTTON_TEXT = "Avvia gioco";
    private static final String GO_BACK_BUTTON_TEXT = "Torna indietro";
    private static final String [] GAME_MODES_STRING = {"Mister X", "Detective"};

    // Layout spacing
    private static final int TITLE_SPACING = 40;
    private static final int BUTTONS_SPACING = 20;

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

    // window properties
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
        //mainPanel.add(createTitleLabel());
        mainPanel.add(createSelectGameModeLabel());
        mainPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        mainPanel.add(createSelectionGameModeComboBox());
        mainPanel.add(Box.createVerticalStrut(TITLE_SPACING));
        mainPanel.add(createSelectGameDifficultyLabel());    
        mainPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        mainPanel.add(createStartGameButton());
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

    // Select game mode label
    private JLabel createSelectGameModeLabel(){
        final JLabel label = new JLabel(SELECT_GAME_MODE_TEXT);
        label.setFont(SELECTION_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // Select game difficulty label
    private JLabel createSelectGameDifficultyLabel(){
        final JLabel label = new JLabel(SELECT_GAME_DIFFICULTY_TEXT);
        label.setFont(SELECTION_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // Start game button
    private JButton createStartGameButton() {
        final JButton button = new JButton(START_BUTTON_TEXT);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.play();
            close();
        });
        return button;
    }

    // Selection game mode combo box
    private JComboBox<?> createSelectionGameModeComboBox(){
        final JComboBox<?> comboBox = new JComboBox<>(GAME_MODES_STRING);
        comboBox.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        return comboBox;
    }


}