package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.entities.ExposedPosition;

public interface GameStateSubscriber {
    default void onTurnStart() {}

    default void onTurnEnd() {}

    default void onExposedPosition(ExposedPosition exposedPosition) {}
}
