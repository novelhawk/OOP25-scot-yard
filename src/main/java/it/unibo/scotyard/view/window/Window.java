package it.unibo.scotyard.view.window;

import it.unibo.scotyard.commons.engine.Size;
import javax.swing.JPanel;

/** Main application window interface. */
public interface Window {

    /**
     * Sets the main content panel.
     *
     * @param panel the content panel to display
     * @throws NullPointerException if panel is null
     */
    void setBody(JPanel panel);

    /**
     * Returns the window width.
     *
     * @return width in pixels
     */
    int getWindowWidth();

    /**
     * Returns the window height.
     *
     * @return height in pixels
     */
    int getWindowHeight();

    /**
     * Sets : default close operation, size, location by platform, resolution. Before first display.
     *
     * @param resolution the requested resolution
     */
    void setsMainFeatures(Size resolution);

    /** Displays the window. */
    void display();

    /**
     * Checks if the window is currently visible.
     *
     * @return true if window is visible, false otherwise
     */
    boolean isVisible();

    /** Hides the window when closing. */
    void setHideOnClose();

    /** Hides the window. */
    void close();
}
