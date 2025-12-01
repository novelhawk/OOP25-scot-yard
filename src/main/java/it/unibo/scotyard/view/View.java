package it.unibo.scotyard.view;

import javax.swing.JPanel;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import it.unibo.scotyard.view.game.GameView;

/**
 * view interface coordinating all UI components.
 */
public interface View {

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
     * Sets window main features (default close operation, size, location by
     * platform).
     * To be called before first window display.
     * 
     * @param resolution the window resolution
     */
    void setWindowMainFeatures(Size resolution);

    /**
     * Creates the main game view.
     * 
     * @param mapInfo the map info
     * @return the game view created
     */
    GameView createGameView(MapInfo mapInfo);

    /**
     * Displays the input panel.
     * Used to display MainMenu + NewGameMenu + Game
     *
     * @param panel 
     */
    void displayPanel(JPanel panel);

    /**
     * Returns the maximum available screen resolution.
     *
     * @return the maximum resolution
     */
    Size getMaxResolution();
}
