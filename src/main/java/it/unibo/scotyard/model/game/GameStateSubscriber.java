package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.entities.ExposedPosition;

public interface GameStateSubscriber {
    default void onRoundStart() {}

    default void onTurnStart() {}

    default void onTurnEnd() {}

    default void onRoundEnd() {}

    default void onExposedPosition(ExposedPosition exposedPosition) {}

    default void onRunnerHidden() {}
}
