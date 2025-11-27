package it.unibo.scotyard.view;

import javax.swing.JPanel;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;

/**
 * view interface coordinating all UI components.
 */
public interface View {

    /**
     * Displays the main view.
     *
     * @throws IllegalStateException if view not properly initialized
     */
    void display();

    /**
     * Returns the main content panel.
     *
     * @return the content panel
     */
    JPanel getContentPane();

    /**
     * Displays the game launcher screen.
     *
     * @param controller the launcher controller
     * @throws NullPointerException if controller is null
     */
    void displayLauncher(GameLauncherController controller);

    /**
     * Sets window main features (deafult close operation, size, location by
     * platform).
     * To be called before first window display.
     */
    void setWindowMainFeatures();

    /**
     * Displays the input panel.
     * Used to display MainMenu + NewGameMenu
     *
     * @param panel
     */
    void displayPanel(JPanel panel);

    /**
     * Displays the main game window with specified resolution.
     *
     * @param resolution the window size
     * @throws NullPointerException if resolution is null
     */
    void displayGameWindow(Size resolution);

    /**
     * Creates the game panel with the map and the sidebar.
     */
    void createGamePanel();

    /**
     * Returns the maximum available screen resolution.
     *
     * @return the maximum resolution
     */
    Size getMaxResolution();
}