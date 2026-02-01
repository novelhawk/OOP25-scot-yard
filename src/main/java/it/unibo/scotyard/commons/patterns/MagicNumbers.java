package it.unibo.scotyard.commons.patterns;

/**
 * All the Magic Numbers used in the game.
 */
public final class MagicNumbers {

    public static final int GAP_1 = 1;
    public static final int GAP_2 = 2;
    public static final int GAP_3 = 3;
    public static final int GAP_4 = 4;
    public static final int GAP_5 = 50;
    public static final int GAP_10 = 10;
    public static final int GAP_20 = 20;
    public static final int GAP_30 = 30;
    public static final int GAP_40 = 40;
    public static final int GAP_50 = 50;

    public static final int HEIGHT_50 = 50;
    public static final int HEIGHT_100 = 100;
    public static final int WIDTH_120 = 120;
    public static final int HEIGHT_150 = 150;
    public static final int WIDTH_700 = 700;
    public static final int WIDTH_800 = 800;

    // GameState
    public static final int NOT_REVEALED_YET = -1;
    public static final int FINAL_ROUND_COUNT = 24;

    // Inventory constants
    public static final int NONE = 0;
    public static final int INFINITE = -1;
    public static final int NUMBER_TICKETS_TAXI = 11;
    public static final int NUMBER_TICKETS_BUS = 8;
    public static final int NUMBER_TICKETS_UNDERGROUND = 4;
    public static final int NUMBER_TICKETS_BLACK = 5;
    public static final int NUMBER_TICKETS_DOUBLE_MOVE = 2;

    private MagicNumbers() {
        throw new AssertionError("non istanziabili le costanti Numbers");
    }
}
