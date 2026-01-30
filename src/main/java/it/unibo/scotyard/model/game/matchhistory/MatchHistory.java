package it.unibo.scotyard.model.game.matchhistory;

public record MatchHistory(int runnerWins, int runnerLoses, int seekerWins, int seekerLoses) {
    public static MatchHistory getDefault() {
        return new MatchHistory(0, 0, 0, 0);
    }
}
