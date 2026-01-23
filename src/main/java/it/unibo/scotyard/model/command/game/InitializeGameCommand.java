package it.unibo.scotyard.model.command.game;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.game.GameDifficulty;
import it.unibo.scotyard.model.game.GameMode;

public record InitializeGameCommand(long seed, GameMode gameMode, GameDifficulty difficulty) implements GameCommand {}
