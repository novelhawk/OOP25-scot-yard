package it.unibo.scotyard.commons.patterns;

import java.awt.Font;

public final class ScotFont {

    private static final String ARIAL = "Arial";
    public static final Font TITLE_FONT_36 = new Font(ARIAL, Font.BOLD, 36);
    public static final Font TEXT_FONT_28 = new Font(ARIAL, Font.BOLD, 28);
    public static final Font TEXT_FONT_24 = new Font(ARIAL, Font.BOLD, 24);
    public static final Font TEXT_FONT_20 = new Font(ARIAL, Font.BOLD, 20);
    public static final Font TEXT_FONT_18 = new Font(ARIAL, Font.BOLD, 18);
    public static final Font TEXT_FONT_16 = new Font(ARIAL, Font.BOLD, 16);
    public static final Font TEXT_FONT_14 = new Font(ARIAL, Font.BOLD, 14);

    private ScotFont() {
        throw new AssertionError("non istanziabili le costanti Font");
    }
}
