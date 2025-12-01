package it.unibo.scotyard.view.window;

import java.awt.Dimension;
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
    private static final double ASPECT_RATIO = 2570.0 / 1926.0;
    private static final int MIN_WIDTH = 800;

    private int windowWidth;
    private int windowHeight;

    /**
     * Creates a window with specified resolution.
     *
     * @param resolution the window size
     * @throws NullPointerException if resolution is null
     */
    public WindowImpl(final Size resolution, String windowTitle) {
        super(windowTitle);
        Objects.requireNonNull(resolution, "Resolution cannot be null");

        this.setResoultion(resolution);
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

        // Imposta dimensione minima proporzionale
        setMinimumSize(new Dimension(MIN_WIDTH, (int) (MIN_WIDTH / ASPECT_RATIO)));
    }

    private void setResoultion(Size resolution){
        this.windowWidth = resolution.getWidth();
        this.windowHeight = resolution.getHeight();
    }

    @Override
    public void setsMainFeatures(Size resolution) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResoultion(resolution);
        setSize(this.windowWidth, this.windowHeight);
        setLocationByPlatform(true);
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public void setHideOnClose() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
}
