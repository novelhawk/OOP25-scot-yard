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
import it.unibo.scotyard.controller.menu.StartMenuController;

/**
 * start menu view
 */
public final class StartMenuViewImpl extends JFrame implements StartMenuView {

    private static final long serialVersionUID = 1L;

    // Window properties
    private static final String WINDOW_TITLE = "Scotland Yard - Main Menu";

    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(62, 39, 35);
    private static final Color ACCENT_COLOR = new Color(255, 171, 145);

    // Typography
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);

    // UI text
    private static final String TITLE_TEXT = "Scotland Yard";
    private static final String START_BUTTON_TEXT = "Avvia";

    // Layout spacing
    private static final int TITLE_SPACING = 40;

    private final StartMenuController controller;
    private final Size resolution;

    /**
     * Creates the start menu view.
     * 
     * @param controller the menu controller
     * @param resolution the window size
     * @throws NullPointerException if any parameter is null
     */
    public StartMenuViewImpl(final StartMenuController controller, final Size resolution) {
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
        mainPanel.add(createTitleLabel());
        mainPanel.add(Box.createVerticalStrut(TITLE_SPACING));
        mainPanel.add(createStartButton());
        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
    }

    // main container panel
    private JPanel createMainPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    // title label
    private JLabel createTitleLabel() {
        final JLabel label = new JLabel(TITLE_TEXT);
        label.setFont(TITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // start button with action
    private JButton createStartButton() {
        final JButton button = new JButton(START_BUTTON_TEXT);
        button.setAlignmentX(CENTER_ALIGNMENT);

        button.addActionListener(e -> {
            this.controller.play();
            close();
        });

        return button;
    }
}