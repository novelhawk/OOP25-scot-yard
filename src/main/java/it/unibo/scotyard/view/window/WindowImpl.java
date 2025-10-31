package it.unibo.scotyard.view.window;

import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.scotyard.commons.engine.Size;

/**
 * main game window.
 */
public final class WindowImpl extends JFrame implements Window {

    private static final long serialVersionUID = 1L;
    private static final String WINDOW_TITLE = "Scotland Yard";

    private final int windowWidth;
    private final int windowHeight;

    /**
     * Creates a window with specified resolution.
     * 
     * @param resolution the window size
     * @throws NullPointerException if resolution is null
     */
    public WindowImpl(final Size resolution) {
        super(WINDOW_TITLE);
        Objects.requireNonNull(resolution, "Resolution cannot be null");

        this.windowWidth = resolution.getWidth();
        this.windowHeight = resolution.getHeight();
    }

    @Override
    public int getWindowWidth() {
        return this.windowWidth;
    }

    @Override
    public int getWindowHeight() {
        return this.windowHeight;
    }

    @Override
    public void setBody(final JPanel panel) {
        Objects.requireNonNull(panel, "Panel cannot be null");
        setContentPane(panel);
    }

    @Override
    public void display() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(this.windowWidth, this.windowHeight);
        setLocationByPlatform(true);
        setVisible(true);
    }
}