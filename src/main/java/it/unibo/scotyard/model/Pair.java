package it.unibo.scotyard.model;

/**
 * Recdord used to define a Pair of generics.
 */
public record Pair<X, Y>(X x, Y y) {
    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}
