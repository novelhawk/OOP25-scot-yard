package it.unibo.scotyard.view.menu;

import it.unibo.scotyard.model.game.record.GameRecord;
import java.util.Optional;
import javax.swing.JPanel;

/** Main menu screen. */
public interface MainMenuView {

    /**
     * @return the main panel of the main menu.
     */
    JPanel getMainPanel();

    /**
     * Displays statistics window with game records table.
     *
     * @param detectiveRecord Optional detective mode record
     * @param mrxRecord       Optional Mr. X mode record
     */
    void displayStatisticsTable(Optional<GameRecord> detectiveRecord, Optional<GameRecord> mrxRecord);

    /**
     * Shows confirmation message after reset.
     */
    void showResetConfirmation();

    /**
     * Shows error message dialog.
     *
     * @param errorMessage the error message to display
     */
    void showError(String errorMessage);

    /** Closes the main menu window and exits the application. */
    void close();
}
