package it.unibo.scotyard.view.window;

import it.unibo.scotyard.commons.engine.Size;
import java.awt.Dimension;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** main game window. */
public final class WindowImpl extends JFrame implements Window {

    private static final long serialVersionUID = 1L;
    private static final double ASPECT_RATIO = 2570.0 / 1926.0;
    private static final int MIN_WIDTH = 800;

    private int windowWidth;
    private int windowHeight;

    /**
     * Creates a window with specified resolution.
     *
     * @param resolution the window size
     * @param containerPanel the container panel
     * @param windowTitle the window title
     * @throws NullPointerException if resolution is null
     */
    public WindowImpl(final Size resolution, final JPanel containerPanel, final String windowTitle) {
        super(windowTitle);
        Objects.requireNonNull(resolution, "Resolution cannot be null");

        this.setResoultion(resolution);
        this.setBody(containerPanel);
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

    private void setResoultion(final Size resolution) {
        this.windowWidth = resolution.getWidth();
        this.windowHeight = resolution.getHeight();
    }

    @Override
    public void setsMainFeatures(final Size resolution) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResoultion(resolution);
        setSize(this.windowWidth, this.windowHeight);
        setLocationByPlatform(true);
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void setHideOnClose() {
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    @Override
    public void close() {
        setVisible(false);
    }
}
