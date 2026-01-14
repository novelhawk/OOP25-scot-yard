package it.unibo.scotyard.commons.engine;

/** Immutable implementation of Size. */
final class SizeImpl implements Size {

    private final int width;
    private final int height;

    public SizeImpl(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public SizeImpl(final Size size) {
        this.width = size.getWidth();
        this.height = size.getHeight();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Size copy() {
        return new SizeImpl(this);
    }

    @Override
    public String toString() {
        return this.width + "x" + this.height;
    }
}
