package it.unibo.scotyard.model.game.matchhistory;

import it.unibo.scotyard.model.game.GameMode;
import java.util.function.BiFunction;
import java.util.function.Function;

public record MatchHistory(int runnerWins, int runnerLoses, int seekerWins, int seekerLoses) {
    /**
     * Gets the initial MatchHistory value
     *
     * @return the initial MatchHistory value
     */
    public static MatchHistory getDefault() {
        return new MatchHistory(0, 0, 0, 0);
    }

    /**
     * Returns a MatchHistory mutator that increments the component matching the supplied GameMode and win state by one
     *
     * @param gameMode the GameMode to match
     * @param hasWon the win status to match
     * @return a MatchHistory mutator that increments the matching component by one
     */
    public static Function<MatchHistory, MatchHistory> incrementOnce(GameMode gameMode, boolean hasWon) {
        final var componentMatcher = componentMatcher(gameMode, hasWon, it -> it + 1);
        return mergeComponentMutators(
                componentMatcher.apply(GameMode.MISTER_X, true),
                componentMatcher.apply(GameMode.MISTER_X, false),
                componentMatcher.apply(GameMode.DETECTIVE, true),
                componentMatcher.apply(GameMode.DETECTIVE, false));
    }

    /**
     * Creates a component matcher that when the GameMode and win status matches the one
     * provided returns the mutator supplied, otherwise returns the identity function
     *
     * @param gameMode the GameMode to match
     * @param hasWon the win status to match
     * @param mutator the mutator to apply when the gameMode and hasWon match
     * @return a component matcher that returns the mutator to apply based on the component
     */
    private static BiFunction<GameMode, Boolean, Function<Integer, Integer>> componentMatcher(
            GameMode gameMode, boolean hasWon, Function<Integer, Integer> mutator) {
        return (componentGameMode, componentHasWon) -> {
            if (componentGameMode == gameMode && componentHasWon == hasWon) {
                return mutator;
            } else {
                return Function.identity();
            }
        };
    }

    /**
     * Joins together the mutator of each component of a MatchHistory into a single mutator of the whole MatchHistory.
     *
     * @param runnerWinsMutator the runner wins component mutator
     * @param runnerLosesMutator the runner loses component mutator
     * @param seekerWinsMutator the seeker wins component mutator
     * @param seekerLosesMutator the seeker loses component mutator
     * @return the MatchHistory mutator that applies the respective mutator to each of its components.
     */
    private static Function<MatchHistory, MatchHistory> mergeComponentMutators(
            Function<Integer, Integer> runnerWinsMutator,
            Function<Integer, Integer> runnerLosesMutator,
            Function<Integer, Integer> seekerWinsMutator,
            Function<Integer, Integer> seekerLosesMutator) {
        return it -> new MatchHistory(
                runnerWinsMutator.apply(it.runnerWins()),
                runnerLosesMutator.apply(it.runnerLoses()),
                seekerWinsMutator.apply(it.seekerWins()),
                seekerLosesMutator.apply(it.seekerLoses()));
    }
}
