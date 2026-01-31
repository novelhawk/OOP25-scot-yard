package it.unibo.scotyard.commons.patterns;

/**
 * All the strings used in the game.
 */
public final class ViewConstants {

    public static final String LAUNCHER = "Scotland Yard - Game Launcher";
    public static final String SCOTLAND_YARD = "Scotland Yard";

    public static final String RESOLUTION_LABEL = "Seleziona Risoluzione:";

    public static final String NEW_GAME_TEXT = "Nuova partita";
    public static final String STATISTICS_TEXT = "Statistiche";
    public static final String EXIT_TEXT = "Esci";

    public static final String PLAYER_NAME_TEXT = "Inserire nome";
    public static final String SELECT_GAME_MODE_TEXT = "Selezionare modalità";
    public static final String SELECT_GAME_DIFFICULTY_TEXT = "Selezionare difficoltà";
    public static final String START_BUTTON_TEXT = "Avvia gioco";
    public static final String GO_BACK_BUTTON_TEXT = "Torna indietro";
    public static final String[] GAME_MODES_STRINGS = {"Mister X", "Detective"};
    public static final String[] DIFFICULTY_LEVELS_STRINGS = {"Facile", "Media", "Difficile"};

    public static final String MRX_PAWN = "X";
    public static final String DETECTIVE_PAWN = "D";
    public static final String BOBBIES_PAWN = "B";

    public static final String MRX_STRING = "Mister X";
    public static final String DETECTIVE_STRING = "Detective";
    public static final String BOBBIES_STRING = "Bobbies";
    public static final String BOBBY_STRING = "Bobby";

    public static final String TAXI_TEXT = "Taxi";
    public static final String BUS_TEXT = "Bus";
    public static final String UNDERGROUND_TEXT = "Metro";
    public static final String FERRY_TEXT = "Traghetto";

    public static final String SELECTION_TRANSPORT_JDIALOG = "Seleziona mezzo di trasporto";
    public static final String SELECTION_TRANSPORT_TITLE = "Seleziona mezzo di trasporto verso il nodo: ";

    public static final String RULES_WINDOW_TITLE = "Regole";

    public static final String INVENTORY_TEXT = "Inventario";
    public static final String TAXI_TICKETS_TEXT = "Biglietti taxi";
    public static final String BUS_TICKETS_TEXT = "Biglietti bus";
    public static final String UNDERGROUND_TICKETS_TEXT = "Biglietti metro";
    public static final String BLACK_TICKETS_TEXT = "Biglietti neri";
    public static final String DOUBLE_MOVE_TICKETS_TEXT = "Biglietti doppia mossa";
    public static final String LOAD_RULES_TEXT = "Regole";
    public static final String CURRENT_PLAYER_TEXT = "Turno di : ";
    public static final String NO_MOVES_SELECTED = "Nessuna mossa selezionata";

    public static final String STATISCS_TITLE = "Statistiche Partite";
    public static final String BACK = "Indietro";
    public static final String RESET_RECORD_TEXT = "Resetta Record";

    public static final String GAME_OVER_WINDOW_TITLE = "Game Over";
    public static final String BACK_MAIN_MENU = "Ritorna al menù principale";
    public static final String WINNER_TEXT = "Vittoria";
    public static final String LOSER_TEXT = "Sconfitta";

    public static final String CAPTURED_MISTER_X_MODE_TEXT = " : sei stato catturato!";
    public static final String CAPTURED_DETECTIVE_MODE_TEXT = " : Mister X catturato!";
    public static final String NO_MORE_TICKETS_AVAILABLE_TEXT = " : biglietti esauriti per le fermate raggiungibili";
    public static final String NO_MORE_MOVES_TEXT = " : non puoi muoverti";
    public static final String NO_MORE_TICKETS_AI_TEXT = " : gli avversari hanno finito i biglietti per muoversi";
    public static final String ESCAPED_MISTER_X_MODE_TEXT = " : sei riuscito a fuggire";
    public static final String ESCAPED_DETECTIVE_MODE_TEXT = " : Mister X non è stato catturato in tempo";

    public static final String MISTERX_GAMES = "Partite da Mister X";
    public static final String SEEKER_GAMES = "Partite da Detective";

    private ViewConstants() {
        throw new AssertionError("non istanziabili le costanti Stringhe");
    }
}
