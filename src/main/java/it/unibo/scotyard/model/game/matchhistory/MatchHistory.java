package it.unibo.scotyard.model.game.matchhistory;

/**
 * Contains the statistics from previous matches.
 */
public interface MatchHistory {
    /**
     * Gets the number of Mister X match wins
     *
     * @return the number of Mister X match wins
     */
    int runnerWins();

    /**
     * Gets the number of Mister X match loses
     *
     * @return the number of Mister X match loses
     */
    int runnerLoses();

    /**
     * Gets the number of seekers match wins
     *
     * @return the number of seekers match wins
     */
    int seekerWins();

    /**
     * Gets the number of seekers match loses
     *
     * @return the number of seekers match loses
     */
    int seekerLoses();
}
