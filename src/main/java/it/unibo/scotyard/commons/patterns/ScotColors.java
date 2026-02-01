package it.unibo.scotyard.commons.patterns;

import java.awt.Color;

/**
 * All the Colors used in the game.
 */
public final class ScotColors {

    // Colori tema
    public static final Color BACKGROUND_COLOR = new Color(62, 39, 35); // marrone
    public static final Color ACCENT_COLOR = new Color(200, 160, 120); // arancio

    // Colori trasporti
    public static final Color TAXI_COLOR = new Color(255, 255, 85); // giallo
    public static final Color BUS_COLOR = new Color(85, 165, 150); // verde/blu
    public static final Color UNDERGROUND_COLOR = new Color(200, 43, 29); // rosso
    public static final Color FERRY_COLOR = new Color(33, 33, 33); // nero

    // Colori pedine
    public static final Color MISTER_X_COLOR = new Color(0, 0, 0); // nero
    public static final Color MISTER_X_EXPOSED_COLOR = new Color(239, 16, 35); // rosso
    public static final Color DETECTIVE_COLOR = new Color(0, 100, 200); // blu
    public static final Color BOBBIES_COLOR = new Color(255, 140, 0); // arancione scuro
    // bordi
    public static final Color MISTER_X_BORDER_COLOR = Color.GRAY;
    public static final Color DETECTIVE_BORDER_COLOR = new Color(0, 150, 255); // blu chiaro
    public static final Color BOBBIES_BORDER_COLOR = new Color(255, 200, 100); // arancione chiaro

    // Nodi
    public static final Color VALID_NODE = new Color(0, 255, 0, 80); // verde semi-trasp
    public static final Color SELECTED_NODE = new Color(0, 150, 255, 180); // blu semi-trasp

    // varie
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 30); // shadow
    public static final Color EMPTY_COLOR = new Color(66, 66, 66); // usato per il tracker
    public static final Color LONGEST_GAME = new Color(200, 200, 200);
    public static final Color MOUSE_HOVER = new Color(245, 245, 245);

    private ScotColors() {
        throw new AssertionError("non istanziabili le costanti Colors");
    }
}
