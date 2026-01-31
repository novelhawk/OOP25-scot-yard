package it.unibo.scotyard.view.menu;

public interface StatisticsView {

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
}
