package it.unibo.scotyard.view;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import it.unibo.scotyard.controller.menu.MainMenuController;
import it.unibo.scotyard.controller.menu.StatisticsController;
import it.unibo.scotyard.model.game.matchhistory.MatchHistory;
import it.unibo.scotyard.model.game.record.GameRecord;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.menu.MainMenuView;
import it.unibo.scotyard.view.menu.StatisticsView;
import javax.swing.JPanel;

/** view interface coordinating all UI components. */
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
     * platform). To be called
     * before first window display.
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
     * Displays the input panel. Used to display MainMenu + NewGameMenu + Game
     *
     * @param panel the panel to display
     */
    void displayPanel(JPanel panel);

    /**
     * Returns the maximum available screen resolution.
     *
     * @return the maximum resolution
     */
    Size getMaxResolution();

    /**
     * Creates and returns the MainMenu view
     *
     * @param controller the MainMenu controller
     * @return the MainMenu view
     */
    MainMenuView showMainMenuView(MainMenuController controller);

    /**
     * Shows the static statistics page.
     */
    StatisticsView showStatisticsView(
            StatisticsController controller,
            GameRecord mrxRecord,
            GameRecord detectiveRecord,
            MatchHistory matchHistory);
}
