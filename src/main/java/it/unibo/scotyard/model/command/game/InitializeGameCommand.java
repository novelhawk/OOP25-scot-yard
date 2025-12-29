package it.unibo.scotyard.model.command.game;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.game.GameMode;

public record InitializeGameCommand(GameMode gameMode) implements GameCommand {}
