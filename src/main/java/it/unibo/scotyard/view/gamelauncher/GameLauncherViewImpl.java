package it.unibo.scotyard.view.gamelauncher;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Swing implementation of game launcher view. */
public final class GameLauncherViewImpl extends JFrame implements GameLauncherView {

    private static final long serialVersionUID = 1L;

    // Window properties
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 250;

    // Layout spacing
    private static final int PADDING = 20;
    private static final int COMPONENT_SPACING = 15;
    private static final int HORIZONTAL_SPACING = 10;
    private static final int DOUBLE_SPACING = COMPONENT_SPACING * 2;

    // Component sizes
    private static final int COMBO_WIDTH = 200;
    private static final int COMBO_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;

    private final GameLauncherController controller;

    /**
     * Creates the launcher view.
     *
     * @param controller the launcher controller
     * @throws NullPointerException if controller is null
     */
    public GameLauncherViewImpl(final GameLauncherController controller) {
        super(ViewConstants.LAUNCHER);
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
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

    // Configure window properties
    private void setupWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // Build the UI components
    private void buildUI() {
        final JPanel mainPanel = createMainPanel();

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(createTitleLabel());
        mainPanel.add(Box.createVerticalStrut(DOUBLE_SPACING));
        mainPanel.add(createResolutionPanel());
        mainPanel.add(Box.createVerticalStrut(DOUBLE_SPACING));
        mainPanel.add(createStartButton());
        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
    }

    // Create main container panel
    private JPanel createMainPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ScotColors.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        return panel;
    }

    // Create title label
    private JLabel createTitleLabel() {
        final JLabel label = new JLabel(ViewConstants.SCOTLAND_YARD);
        label.setFont(ScotFont.TEXT_FONT_24);
        label.setForeground(ScotColors.ACCENT_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    // Create resolution selection panel
    private JPanel createResolutionPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(ScotColors.BACKGROUND_COLOR);

        final JLabel label = new JLabel(ViewConstants.RESOLUTION_LABEL);
        label.setFont(ScotFont.TEXT_FONT_14);
        label.setForeground(ScotColors.ACCENT_COLOR);

        final JComboBox<String> comboBox = createResolutionComboBox();

        panel.add(label);
        panel.add(Box.createHorizontalStrut(HORIZONTAL_SPACING));
        panel.add(comboBox);

        return panel;
    }

    // Create resolution combo box with default selection
    private JComboBox<String> createResolutionComboBox() {
        final List<String> resolutionStrings =
                this.controller.getResolutions().stream().map(Size::toString).toList();

        final JComboBox<String> comboBox = new JComboBox<>(resolutionStrings.toArray(String[]::new));
        comboBox.setFont(ScotFont.TEXT_FONT_14);
        comboBox.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));

        final int defaultIndex = this.controller.getResolutions().size() / 2;
        comboBox.setSelectedIndex(defaultIndex);
        this.controller.selectResolution(defaultIndex);

        return comboBox;
    }

    // Create start button with action
    private JButton createStartButton() {
        final JButton button = new JButton(ViewConstants.START_BUTTON_TEXT);
        button.setFont(ScotFont.TEXT_FONT_16);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        button.addActionListener(e -> handleStartButton());

        return button;
    }

    // Handle start button click
    private void handleStartButton() {
        final JComboBox<?> comboBox = findResolutionComboBox();
        if (comboBox != null) {
            this.controller.selectResolution(comboBox.getSelectedIndex());
        }
        this.controller.startGame();
        close();
    }

    // Find resolution combo box in component tree
    private JComboBox<?> findResolutionComboBox() {
        return findComboBoxRecursive(getContentPane());
    }

    private JComboBox<?> findComboBoxRecursive(final Container container) {
        for (final Component comp : container.getComponents()) {
            if (comp instanceof JComboBox) {
                return (JComboBox<?>) comp;
            }
            if (comp instanceof Container) {
                final JComboBox<?> result = findComboBoxRecursive((Container) comp);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
