package it.unibo.scotyard.commons;

import it.unibo.scotyard.commons.engine.Size;
import java.io.File;
import java.util.List;

/** Application constants. */
public final class Constants {

    // res
    public static final List<Size> RESOLUTIONS = List.of(
            Size.of(3840, 2160), Size.of(3440, 1440), Size.of(2560, 1440), Size.of(1920, 1080), Size.of(1280, 720));

    // Delay time (in milliseconds)
    public static final int DELAY_TIME = 1000;

    // Folder for longest game records json
    public static final String DATA_GAME_FOLDER =
            // non sicuro, to check
            System.getProperty("user.dir") + File.separator + "data";

    private Constants() {
        throw new AssertionError("non istanziabili le costanti");
    }
}
