package it.unibo.scotyard.model.entities;

import it.unibo.scotyard.model.map.TransportType;
import java.util.List;

public interface RunnerTurnTracker {
    /**
     * Adds the Mister X turn to the tracked turns.
     *
     * @param turnMoves the last Mister X turn moves
     */
    void addTurn(List<TransportType> turnMoves);

    /**
     * Adds a subscriber that listens to tracked turn updates.
     *
     * @param subscriber the subscriber
     */
    void subscribe(RunnerTurnTrackerSubscriber subscriber);
}
