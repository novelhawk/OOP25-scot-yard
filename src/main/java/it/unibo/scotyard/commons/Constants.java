package it.unibo.scotyard.commons;

import it.unibo.scotyard.commons.engine.Size;

import java.awt.Color;
import java.util.List;

/** Application constants. */
public final class Constants {

    // res
    public static final List<Size> RESOLUTIONS = List.of(
            Size.of(3840, 2160), Size.of(3440, 1440), Size.of(2560, 1440), Size.of(1920, 1080), Size.of(1280, 720));

    // Colors
    public static final Color TAXI_COLOR = new Color(255, 255, 85);
    public static final Color BUS_COLOR = new Color(58, 132, 36);
    public static final Color UNDERGROUND_COLOR = new Color(200, 43, 29);
    public static final Color FERRY_COLOR = new Color(0,0,0);

    private Constants() {
        throw new AssertionError("non istanziabili le costanti");
    }
}
