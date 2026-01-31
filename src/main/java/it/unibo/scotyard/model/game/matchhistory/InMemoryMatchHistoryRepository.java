package it.unibo.scotyard.model.game.matchhistory;

import it.unibo.scotyard.model.game.GameMode;
import java.io.IOException;
import java.util.function.Function;

/**
 * A MatchHistoryRepository implementation that stores
 * the state in memory and does not handle persistence.
 */
public final class InMemoryMatchHistoryRepository implements MatchHistoryRepository {

    private MatchHistory current;

    public InMemoryMatchHistoryRepository() {
        this.current = MatchHistoryImpl.getDefault();
    }

    @Override
    public MatchHistory loadOrDefault() {
        return current;
    }

    @Override
    public void trackOutcome(GameMode gameMode, boolean hasWon) {
        update(MatchHistoryImpl.incrementOnce(gameMode, hasWon));
    }

    @Override
    public void resetTracking() throws IOException {
        update(it -> MatchHistoryImpl.getDefault());
    }

    @Override
    public void update(Function<MatchHistory, MatchHistory> mutator) {
        final MatchHistory current = loadOrDefault();
        this.current = mutator.apply(current);
    }
}
