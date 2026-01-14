package it.unibo.scotyard.model.entities;

import it.unibo.scotyard.model.map.TransportType;
import java.util.List;

public interface RunnerTurnTracker {
    void addTurn(List<TransportType> turnMoves);

    void subscribe(RunnerTurnTrackerSubscriber subscriber);
}
