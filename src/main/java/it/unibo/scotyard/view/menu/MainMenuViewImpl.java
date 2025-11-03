package it.unibo.scotyard.view.menu;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.menu.MainMenuController;

public class MainMenuViewImpl extends JFrame implements MainMenuView{

    private static final long serialVersionUID = 1L;

    // Window properties
    private static final String WINDOW_TITLE = "Scotland Yard - Main Menu";

    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(62, 39, 35);
    private static final Color ACCENT_COLOR = new Color(255, 171, 145);

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
    private final Size resolution;

    /**
     * Creates the main menu view.
     * 
     * @param controller the main menu controller
     * @param resolution the window size
     * @throws NullPointerException if any parameter is null
     */
    public MainMenuViewImpl(final MainMenuController controller, final Size resolution) {
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
        mainPanel.add(createTitleLabel());
        mainPanel.add(Box.createVerticalStrut(TITLE_SPACING));
        mainPanel.add(createNewGameButton());
        mainPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        mainPanel.add(createLoadGameButton());
        mainPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        mainPanel.add(createStatisticsButton());
        mainPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
        mainPanel.add(createExitButton());
        mainPanel.add(Box.createVerticalStrut(BUTTONS_SPACING));
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

    // Title label
    private JLabel createTitleLabel(){
        final JLabel label = new JLabel(TITLE_TEXT);
        label.setFont(TITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // New game button
    private JButton createNewGameButton(){
        final JButton button = new JButton(NEW_GAME_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(e ->{
            this.controller.newGame();
            close();
        });
        return button;
    }

    // Load game button
    private JButton createLoadGameButton(){
        final JButton button = new JButton(LOAD_GAME_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            // TO DO 
        });
        return button;
    }

    // Statistics button
    private JButton createStatisticsButton(){
        final JButton button = new JButton(STATISTICS_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            // TO DO
        });
        return button;
    }

    // Exit button
    private JButton createExitButton(){
        final JButton button = new JButton(EXIT_TEXT);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            this.controller.exit();
        });
        return button;
    }
    
}
