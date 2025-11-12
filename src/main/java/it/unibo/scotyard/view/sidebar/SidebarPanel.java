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
    private static final Color ACCENT_COLOR = new Color(255, 171, 145);
    
    // Typography
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font TICKETS_FONT = new Font("Arial", Font.PLAIN, 11);

    // Layout spacing
    private static final int SPACING = 10;
    private static final int SMALL_SPACING = 6;

    // Texts
    private static final String  INVENTORY_TEXT = "Inventario";

    // Components
    JLabel currentGameModeLabel;
    JLabel roundLabel;
    JLabel ticketsTaxiLabel;
    JLabel ticketsBusLabel;
    JLabel ticketsMetroLabel;
    

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
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
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
    
}