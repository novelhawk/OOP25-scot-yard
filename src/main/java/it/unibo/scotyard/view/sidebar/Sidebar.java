package it.unibo.scotyard.view.sidebar;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Sidebar panel for game UI.
 * Currently displays only background, ready for future content.
 */
public final class Sidebar extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int SIDEBAR_WIDTH = 200;
    private static final int PADDING = 10;
    private static final Color BACKGROUND_COLOR = new Color(62, 39, 35);

    /**
     * Creates a sidebar panel.
     */
    public Sidebar() {
        setupSidebar();
        buildContent();
    }

    // Configure sidebar properties
    private void setupSidebar() {
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    // sidebar content (empty for now)
    private void buildContent() {
        // Placeholder
    }
}