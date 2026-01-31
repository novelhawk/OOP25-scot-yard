package it.unibo.scotyard.view.menu;

import javax.swing.JPanel;

/** Main menu screen. */
public interface MainMenuView {

    /**
     * @return the main panel of the main menu.
     */
    JPanel getMainPanel();

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
