package it.unibo.scotyard.controller.menu;

public interface StatisticsController {

    /**
     * Shows the statistics view with the static data.
     */
    void showView();

    /**
     * Loads back the Main Menu.
     */
    void loadMainMenu();

    /**
     * Resets the saved statistics.
     */
    void resetStatistics();
}
