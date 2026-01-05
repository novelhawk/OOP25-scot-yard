package it.unibo.scotyard.model.entities;

import it.unibo.scotyard.model.map.TransportType;
import java.util.List;

@FunctionalInterface
public interface RunnerTurnTrackerSubscriber {
    /**
     * Called whenever the list of MisterX turns gets updated.
     *
     * @param tickets the MisterX turns
     */
    void onTicketsChanged(List<List<TransportType>> tickets);
}
