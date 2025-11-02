package it.unibo.scotyard.view.menu;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.JFrame;
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

    // UI text
    private static final String TITLE_TEXT = "Scotland Yard";

    // Layout spacing
    private static final int TITLE_SPACING = 40;

    private final MainMenuController controller;
    private final Size resolution;

    /**
     * Creates the start new game menu view.
     * 
     * @param controller the menu controller
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'display'");
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
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
        // TO DO
    }
    
}
