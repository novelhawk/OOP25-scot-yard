package it.unibo.scotyard.view.sidebar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.scotyard.model.game.GameMode;

/**
 * Sidebar panel for game UI.
 * Currently displays only background, ready for future content.
 */
public final class SidebarPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int SIDEBAR_WIDTH = 200;
    private static final int PADDING = 10;

    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(62, 39, 35);
    private static final Color ACCENT_COLOR = new Color(255, 255, 255);
    
    // Typography
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font TICKETS_FONT = new Font("Arial", Font.PLAIN, 11);

    // Layout spacing
    private static final int SPACING = 10;
    private static final int SMALL_SPACING = 6;

    // Texts
    private static final String  INVENTORY_TEXT = "Inventario";
    private static final String TAXI_TICKETS_TEXT = "Biglietti Taxi";
    private static final String BUS_TICKETS_TEXT = "Biglietti Bus";
    private static final String UNDERGROUND_TICKETS_TEXT = "Biglietti Metro";
    private static final String BLACK_TICKETS_TEXT = "Biglietti neri";
    private static final String DOUBLE_MOVE_TICKETS_TEXT = "Biglietti doppia mossa";

    // Components
    JLabel currentGameModeLabel;
    JLabel roundLabel;
    JLabel taxiTicketsLabel;
    JLabel busTicketsLabel;
    JLabel undergroundTicketsLabel;
    JLabel blackTicketsLabel;
    JLabel doubleMoveTicketsLabel;
    

    /**
     * Creates a sidebar panel.
     */
    public SidebarPanel() {
        setupSidebar();
        buildContent();
    }

    // Configure sidebar properties
    private void setupSidebar() {
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    // Sidebar content
    private void buildContent() {
        this.currentGameModeLabel = createCurrentGameModeLabel();
        this.add(Box.createVerticalGlue());
        this.add(this.currentGameModeLabel);
        this.add(Box.createVerticalStrut(SPACING));

        this.roundLabel = createCountRoundLabel();
        this.add(roundLabel);
        this.add(Box.createVerticalStrut(SPACING));

        this.add(createInventoryLabel());
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.taxiTicketsLabel = createTicketLabel(TAXI_TICKETS_TEXT);
        this.add(taxiTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.busTicketsLabel = createTicketLabel(BUS_TICKETS_TEXT);
        this.add(busTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.undergroundTicketsLabel = createTicketLabel(UNDERGROUND_TICKETS_TEXT);
        this.add(undergroundTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.blackTicketsLabel = createTicketLabel(BLACK_TICKETS_TEXT);
        this.add(blackTicketsLabel);
        this.add(Box.createVerticalStrut(SMALL_SPACING));
        this.doubleMoveTicketsLabel = createTicketLabel(DOUBLE_MOVE_TICKETS_TEXT);
        this.add(doubleMoveTicketsLabel);
  
        this.add(Box.createVerticalGlue());
    }

    private JLabel createCurrentGameModeLabel(){
        final JLabel label = new JLabel("Player");
        label.setFont(TITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createCountRoundLabel(){
        final JLabel label = new JLabel("Round : ");
        label.setFont(SUBTITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createInventoryLabel(){
        final JLabel label = new JLabel(INVENTORY_TEXT);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createTicketLabel(String text){
        final JLabel label = new JLabel(text);
        label.setFont(TICKETS_FONT);
        label.setForeground(ACCENT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Sets the game mode label, according to the game mode selected.
     * 
     * @param gameMode the game mode chosen by the user
     */
    public void setGameModeLabel(GameMode gameMode){
        if(gameMode.equals(GameMode.DETECTIVE)){
            this.currentGameModeLabel.setText("Detective");
        } else{
            if(gameMode.equals(GameMode.MISTER_X)){
                this.currentGameModeLabel.setText("Mister X");
            }
        }
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying.
     * Updates the text of the roundLabel according to current roundNumber.
     * 
     * @param roundNumber the current round number
     */
    public void updateRoundLabel(int roundNumber){
        this.roundLabel.setText("Round " + Integer.toString(roundNumber));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying.
     * Updates the text of the taxiTicketsLabel according to current number of taxi tickets 
     * possessed by the user player.
     * 
     * @param tickets the number of tickets
     */
    public void updateTaxiTicketsLabel(int tickets){
        if(tickets==-1){
            this.taxiTicketsLabel.setText(TAXI_TICKETS_TEXT + " : infiniti");
        } else{
            this.taxiTicketsLabel.setText(TAXI_TICKETS_TEXT + " : " + Integer.toString(tickets));
        }
    }
    
    /**
     * Method which is called by the GameController when updating the sideabar displaying.
     * Updates the text of the busTicketsLabel according to current number of bus tickets 
     * possessed by the user player.
     * 
     * @param tickets the number of tickets
     */
    public void updateBusTicketsLabel(int tickets){
        if(tickets==-1){
            this.busTicketsLabel.setText(BUS_TICKETS_TEXT + " : infiniti");
        } else{
            this.busTicketsLabel.setText(BUS_TICKETS_TEXT + " : " + Integer.toString(tickets));
        }
        
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying.
     * Updates the text of the undergroundTicketsLabel according to current number of underground tickets 
     * possessed by the user player.
     * 
     * @param tickets the number of tickets
     */
    public void updateUndergroundTicketsLabel(int tickets){
        if(tickets==-1){
            this.undergroundTicketsLabel.setText(UNDERGROUND_TICKETS_TEXT + " : infiniti");
        } else{ 
            this.undergroundTicketsLabel.setText(UNDERGROUND_TICKETS_TEXT + " : " + Integer.toString(tickets));
        }
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying.
     * Updates the text of the blackTicketsLabel according to current number of black tickets 
     * possessed by the user player.
     * 
     * @param tickets the number of tickets
     */
    public void updateBlackTicketsLabel(int tickets){
        this.blackTicketsLabel.setText(BLACK_TICKETS_TEXT + " : " + Integer.toString(tickets));
    }

    /**
     * Method which is called by the GameController when updating the sideabar displaying.
     * Updates the text of the doubleMoveTicketsLabel according to current number of double move tickets 
     * possessed by the user player.
     * 
     * @param tickets the number of tickets
     */
    public void updateDoubleMoveTicketsLabel(int tickets){
        this.doubleMoveTicketsLabel.setText(DOUBLE_MOVE_TICKETS_TEXT + " : " + Integer.toString(tickets));
    }


}