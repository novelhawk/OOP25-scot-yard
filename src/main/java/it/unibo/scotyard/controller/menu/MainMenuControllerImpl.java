package it.unibo.scotyard.controller.menu;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.game.record.JsonRecordRepository;
import it.unibo.scotyard.model.game.record.RecordRepository;
import it.unibo.scotyard.view.View;
import it.unibo.scotyard.view.menu.MainMenuViewImpl;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * The controller used by the MainMenu view.
 *
 */
public final class MainMenuControllerImpl implements MainMenuController {

    private final Controller controller;
    private final View view;
    private MainMenuViewImpl menuView; // Mantieni riferimento alla view

    /**
     * Creates a start menu controller.
     *
     * @param controller the main controller
     * @param view       the view component
     * @throws NullPointerException if any parameter is null
     */
    public MainMenuControllerImpl(final Controller controller, final View view) {
        this.controller = Objects.requireNonNull(controller, "Controller cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
    }

    @Override
    public JPanel getMainPanel() {
        if (this.menuView == null) {
            this.menuView = new MainMenuViewImpl(this, this.view.getMaxResolution());
        }
        return this.menuView.getMainPanel();
    }

    @Override
    public void newGameMenu() {
        this.controller.loadNewGameMenu();
    }

    @Override
    public void showStatistics() {
        // recupera dati model
        try {
            final RecordRepository repository = JsonRecordRepository.initialize();

            // passa record completi alla view
            final java.util.Optional<it.unibo.scotyard.model.game.record.GameRecord> detectiveRecord =
                    repository.findByMode(GameMode.DETECTIVE);
            final java.util.Optional<it.unibo.scotyard.model.game.record.GameRecord> mrxRecord =
                    repository.findByMode(GameMode.MISTER_X);

            // delega alla view esistente la presentazione (formattazione tabella)
            if (this.menuView != null) {
                this.menuView.displayStatisticsTable(detectiveRecord, mrxRecord);
            }

        } catch (final IOException e) {
            Logger.getLogger(MainMenuControllerImpl.class.getName()).log(Level.SEVERE, "Failed to load statistics", e);
            // delega alla view anche l'errore
            if (this.menuView != null) {
                this.menuView.showError("Errore nel caricamento delle statistiche");
            }
        }
    }

    @Override
    public void resetRecords() {
        try {
            final RecordRepository repository = JsonRecordRepository.initialize();
            repository.resetAllRecords();

            if (this.menuView != null) {
                this.menuView.showResetConfirmation();
            }

        } catch (final IOException e) {
            Logger.getLogger(MainMenuControllerImpl.class.getName()).log(Level.SEVERE, "Failed to reset records", e);
            if (this.menuView != null) {
                this.menuView.showError("Errore durante il reset dei record");
            }
        }
    }

    @Override
    public void exit() {
        this.controller.exit();
    }
}
