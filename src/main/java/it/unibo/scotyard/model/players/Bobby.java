package it.unibo.scotyard.model.players;

import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.map.NodeId;

public class Bobby extends PlayerImpl {
    /**
     * Creates a new AI Bobby player starting at the given position.
     *
     * @param position the starting position
     * @param brain the AI brain
     */
    public Bobby(NodeId position, PlayerBrain brain) {
        super(position, brain);
        this.name = "Bobby";
    }

    /**
     * Creates a new Bobby player starting at the given position.
     *
     * @param position the starting position
     */
    public Bobby(NodeId position) {
        super(position);
        this.name = "Bobby";
    }
}
