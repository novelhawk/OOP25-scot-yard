package it.unibo.scotyard.commons.engine;

/**
 * Represents immutable dimensions (width x height).
 */
public interface Size {

    /**
     * @return the width
     */
    int getWidth();

    /**
     * @return the height
     */
    int getHeight();

    /**
     * @return a defensive copy of this size
     */
    Size copy();

    /**
     * Creates a new Size instance.
     * 
     * @param width  the width
     * @param height the height
     * @return a new Size
     */
    static Size of(final int width, final int height) {
        return new SizeImpl(width, height);
    }
}