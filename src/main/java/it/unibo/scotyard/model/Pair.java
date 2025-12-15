package it.unibo.scotyard.model;

public record Pair<X,Y> (X x, Y y){
    public X getX(){
        return x;
    }

    public Y getY(){
        return y;
    }
}
