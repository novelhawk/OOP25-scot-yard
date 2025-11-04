package it.unibo.scotyard.commons;

import java.util.List;

import it.unibo.scotyard.commons.engine.Size;

/**
 * Application constants.
 */
public final class Constants {

    // res
    public static final List<Size> RESOLUTIONS = List.of(
            Size.of(3840, 2160),
            Size.of(3440, 1440),
            Size.of(2560, 1440),
            Size.of(1920, 1080),
            Size.of(1280, 720));

    private Constants() {
        throw new AssertionError("non istanziabili le costanti");
    }
}