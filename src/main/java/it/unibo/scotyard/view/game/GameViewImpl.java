package it.unibo.scotyard.view.game;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.game.GameController;
import it.unibo.scotyard.model.game.GameMode;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import it.unibo.scotyard.view.window.Window;
import it.unibo.scotyard.view.window.WindowImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameViewImpl implements GameView {

    private static final int SMALL_WINDOW_WIDTH = 300;
    private static final int SMALL_WINDOW_HEIGHT = 100;

    private static final int SPACING = 50;
    private static final int SMALL_SPACING = 10;

    private static final String RULES_WINDOW_TITLE = "Regole";
    private static final String GAME_OVER_WINDOW_TITLE = "Game Over";
    private static final String SELECTION_JDIALOG_TITLE = "Selezione mezzo di trasporto";
    private static final String TAXI_TEXT = "Taxi";
    private static final String BUS_TEXT = "Bus";
    private static final String UNDERGROUND_TEXT = "Metro";
    private static final String FERRY_TEXT = "Traghetto";

    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0); // black
    private static final Color ACCENT_COLOR = new Color(31, 81, 255); // neon blue
    private static final Color WHITE_COLOR = new Color(255, 255, 255);
    private static final Color RED_COLOR = new Color(255, 0, 0); // red

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    private static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font SMALL_TEXT_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font WINNER_FONT = new Font("Arial", Font.BOLD, 28);

    private MapPanel mapPanel;
    private SidebarPanel sidebar;
    private JPanel mainPanel;
    private Window gameOverWindow;
    private JLabel winnerLabel;

    private GameController observer;

    private TransportType selectedTransportType;
    private boolean isTransportTypeSelected;

    public GameViewImpl(final MapInfo mapInfo) {
        this.mapPanel = new MapPanel(mapInfo, this);
        this.sidebar = new SidebarPanel(this);
        this.createGameOverWindow();

        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.sidebar, BorderLayout.EAST);
        this.mainPanel.add(this.mapPanel, BorderLayout.CENTER);

        this.isTransportTypeSelected = false;
    }

    @Override
    public void setObserver(GameController gameController) {
        this.observer = gameController;
    }

    @Override
    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    @Override
    public SidebarPanel getSidebar() {
        return this.sidebar;
    }

    @Override
    public MapPanel getMapPanel() {
        return this.mapPanel;
    }

    @Override
    public void displayRulesWindow(JPanel panel) {
        final Size smallSize = Size.of(SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT);
        Window rulesWindow = new WindowImpl(smallSize, panel, RULES_WINDOW_TITLE);
        rulesWindow.setsMainFeatures(smallSize);
        rulesWindow.setHideOnClose();
        rulesWindow.display();
    }

    public void createGameOverWindow() {
        final Size smallSize = Size.of(SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT);
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.add(Box.createVerticalGlue());
        JLabel titleLabel = new JLabel("GAME OVER!");
        titleLabel.setForeground(RED_COLOR);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(SMALL_SPACING));
        this.winnerLabel = new JLabel();
        this.winnerLabel.setForeground(ACCENT_COLOR);
        this.winnerLabel.setFont(WINNER_FONT);
        this.winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(this.winnerLabel);
        panel.add(Box.createVerticalStrut(SPACING));
        JButton button = new JButton("Ritorna al menù principale");
        button.setFont(TEXT_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);
        panel.add(Box.createVerticalStrut(SPACING));
        panel.add(Box.createVerticalGlue());

        this.gameOverWindow = new WindowImpl(smallSize, panel, GAME_OVER_WINDOW_TITLE);
        this.gameOverWindow.setsMainFeatures(smallSize);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                observer.loadMainMenu();
                gameOverWindow.close();
            }
        });
    }

    private void setWinner(GameMode winner) {
        if (GameMode.DETECTIVE.equals(winner)) {
            this.winnerLabel.setText("Vittoria del detective");
        } else {
            this.winnerLabel.setText("Vittoria di Mister X");
        }
    }

    @Override
    public void displayGameOverWindow(GameMode winner) {
        this.setWinner(winner);
        this.gameOverWindow.display();
    }

    @Override
    public void loadTransportSelectionDialog(Set<TransportType> availableTransportTypes) {
        JDialog selectionWindow = new JDialog();
        selectionWindow.setBackground(WHITE_COLOR);
        selectionWindow.setTitle(SELECTION_JDIALOG_TITLE);
        selectionWindow.setSize(new Dimension(SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT));
        selectionWindow.setLayout(new BorderLayout());

        JLabel textLabel = new JLabel("Selezionare mezzo di trasporto");
        textLabel.setForeground(BACKGROUND_COLOR);
        textLabel.setFont(SMALL_TEXT_FONT);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectionWindow.add(textLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        for (TransportType transport : availableTransportTypes) {
            JButton button = new JButton();
            button.setForeground(BACKGROUND_COLOR);
            button.setFont(SMALL_TEXT_FONT);
            switch (transport) {
                case TAXI:
                    button.setText(TAXI_TEXT);
                    button.setBackground(Constants.TAXI_COLOR);
                    this.selectedTransportType = TransportType.TAXI;
                    break;
                case BUS:
                    button.setText(BUS_TEXT);
                    button.setBackground(Constants.BUS_COLOR);
                    this.selectedTransportType = TransportType.BUS;
                    break;
                case UNDERGROUND:
                    button.setText(UNDERGROUND_TEXT);
                    button.setBackground(Constants.UNDERGROUND_COLOR);
                    this.selectedTransportType = TransportType.UNDERGROUND;
                    break;
                case FERRY:
                    button.setText(FERRY_TEXT);
                    button.setBackground(Constants.FERRY_COLOR);
                    this.selectedTransportType = TransportType.FERRY;
                    break;
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isTransportTypeSelected = true;
                    selectionWindow.dispose();
                }
            });
            buttonsPanel.add(button);
        }
        selectionWindow.add(buttonsPanel, BorderLayout.CENTER);
        selectionWindow.setVisible(true);
    }

    @Override
    public boolean isTransportTypeSelected() {
        return this.isTransportTypeSelected;
    }

    @Override
    public TransportType getSelectedTransportType() {
        this.isTransportTypeSelected = false;
        return this.selectedTransportType;
    }

    @Override
    public void movePlayer(int destinationId){
        this.getMapPanel().repaint();
        this.observer.movePlayer(destinationId);
    }
}
