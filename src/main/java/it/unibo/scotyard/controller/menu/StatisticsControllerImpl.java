package it.unibo.scotyard.controller.menu;

import it.unibo.scotyard.controller.Controller;
import it.unibo.scotyard.model.Model;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.game.matchhistory.MatchHistory;
import it.unibo.scotyard.model.game.matchhistory.MatchHistoryRepository;
import it.unibo.scotyard.model.game.record.JsonRecordRepository;
import it.unibo.scotyard.model.game.record.RecordRepository;
import it.unibo.scotyard.view.View;
import it.unibo.scotyard.view.menu.StatisticsView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsControllerImpl implements StatisticsController {

    private final Model model;
    private final Controller controller;
    private final View view;
    private StatisticsView statisticsView;

    public StatisticsControllerImpl(Model model, Controller controller, View view) {
        this.model = model;
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void showView() {
        final MatchHistoryRepository matchHistoryRepository = this.model.getMatchHistoryRepository();
        final MatchHistory matchHistory = matchHistoryRepository.loadOrDefault();

        // recupera dati model
        try {
            final RecordRepository repository = JsonRecordRepository.initialize();

            // passa record completi alla view
            final java.util.Optional<it.unibo.scotyard.model.game.record.GameRecord> detectiveRecord =
                    repository.findByMode(GameMode.DETECTIVE);
            final java.util.Optional<it.unibo.scotyard.model.game.record.GameRecord> mrxRecord =
                    repository.findByMode(GameMode.MISTER_X);

            // delega alla view esistente la presentazione (formattazione tabella)
            this.statisticsView =
                    view.showStatisticsView(this, mrxRecord.orElse(null), detectiveRecord.orElse(null), matchHistory);
        } catch (IOException e) {
            Logger.getLogger(MainMenuControllerImpl.class.getName()).log(Level.SEVERE, "Failed to load statistics", e);
            // delega alla view anche l'errore
            if (this.statisticsView != null) {
                this.statisticsView.showError("Errore nel caricamento delle statistiche");
            }
        }
    }

    @Override
    public void loadMainMenu() {
        this.controller.loadMainMenu();
    }

    @Override
    public void resetStatistics() {
        try {
            final RecordRepository repository = JsonRecordRepository.initialize();
            repository.resetAllRecords();

            if (this.statisticsView != null) {
                this.statisticsView.showResetConfirmation();
            }

        } catch (IOException e) {
            Logger.getLogger(MainMenuControllerImpl.class.getName()).log(Level.SEVERE, "Failed to reset records", e);
            if (this.statisticsView != null) {
                this.statisticsView.showError("Errore durante il reset dei record");
            }
        }
    }
}
