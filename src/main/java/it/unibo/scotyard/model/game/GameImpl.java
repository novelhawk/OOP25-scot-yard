package it.unibo.scotyard.model.game;

public class GameImpl implements Game {

    private GameState gameState;

    public GameImpl() {

    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void setGameState(GameState state) {
        this.gameState = state;
    }

}
