package it.unibo.scotyard.view;

import javax.swing.JPanel;

import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import it.unibo.scotyard.controller.menu.MainMenuController;
import it.unibo.scotyard.controller.menu.NewGameMenuController;

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
     * Displays the main menu screen.
     * 
     * @param controller the menu controller
     * @throws NullPointerException if controller is null
     */
    void displayMainMenu(MainMenuController controller);


    /**
     * Displays the start menu screen.
     * 
     * @param controller the menu controller
     * @throws NullPointerException if controller is null
     */
    void displayNewGameMenu(NewGameMenuController controller);

    /**
     * Displays the main game window with specified resolution.
     * 
     * @param resolution the window size
     * @throws NullPointerException if resolution is null
     */
    void displayWindow(Size resolution);

    /**
     * Returns the maximum available screen resolution.
     * 
     * @return the maximum resolution
     */
    Size getMaxResolution();
}