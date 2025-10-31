package it.unibo.scotyard.controller.gamelauncher;

import java.util.List;

import it.unibo.scotyard.commons.engine.Size;

/**
 * Controller for game launcher screen with resolution selection.
 */
public interface GameLauncherController {

    /**
     * Displays the launcher screen.
     */
    void run();

    /**
     * @return unmodifiable list of available resolutions
     */
    List<Size> getResolutions();

    /**
     * Selects a resolution by index.
     * 
     * @param selection the index of the resolution to select
     * @throws IllegalArgumentException if selection is out of bounds
     */
    void selectResolution(int selection);

    /**
     * Starts the game with selected resolution.
     */
    void startGame();
}