package it.unibo.scotyard.model.players;

public class PlayerImpl implements Player{

    private int currentPosition;

    @Override
    public void setInitialPosition() {
        // TO DO : generate random initial position 
        throw new UnsupportedOperationException("Unimplemented method 'getInitialPosition'");
    }

    @Override
    public int getCurrentPosition(){
        return this.currentPosition;
    }
    
}
