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
        this.current = MatchHistory.getDefault();
    }

    @Override
    public MatchHistory loadOrDefault() {
        return current;
    }

    @Override
    public void trackWin(GameMode gameMode) throws IOException {
        update(MatchHistory.incrementOnce(gameMode, true));
    }

    @Override
    public void trackLose(GameMode gameMode) throws IOException {
        update(MatchHistory.incrementOnce(gameMode, false));
    }

    @Override
    public void update(Function<MatchHistory, MatchHistory> mutator) throws IOException {
        final MatchHistory current = loadOrDefault();
        this.current = mutator.apply(current);
    }
}
