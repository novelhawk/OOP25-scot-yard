package it.unibo.scotyard.model.entities;

import it.unibo.scotyard.model.map.TransportType;
import java.util.ArrayList;
import java.util.List;

public class RunnerTurnTrackerImpl implements RunnerTurnTracker {
    private final List<List<TransportType>> turns = new ArrayList<>();
    private final List<RunnerTurnTrackerSubscriber> subscribers = new ArrayList<>();

    public void addTurn(List<TransportType> turnMoves) {
        turns.add(turnMoves);
        notifySubscribers();
    }

    public void subscribe(RunnerTurnTrackerSubscriber subscriber) {
        subscribers.add(subscriber);
        subscriber.onTicketsChanged(turns);
    }

    private void notifySubscribers() {
        for (final var subscriber : subscribers) {
            subscriber.onTicketsChanged(turns);
        }
    }
}
