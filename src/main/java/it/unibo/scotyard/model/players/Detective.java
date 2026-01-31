package it.unibo.scotyard.model.players;

import it.unibo.scotyard.commons.patterns.CommonCostants;
import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.map.NodeId;

public class Detective extends PlayerImpl {

    /**
     * Creates a new AI Detective player starting at the given position.
     *
     * @param position the starting position
     * @param brain    the AI brain
     */
    public Detective(final NodeId position, final PlayerBrain brain) {
        super(position, brain);
        this.name = CommonCostants.DETECTIVE_STRING;
    }

    /**
     * Creates a new Detective player starting at the given position.
     *
     * @param position the starting position
     */
    public Detective(final NodeId position) {
        super(position);
        this.name = "Detective";
    }
}
