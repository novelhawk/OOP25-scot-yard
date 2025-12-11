package it.unibo.scotyard.view.game;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.game.GameController;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import it.unibo.scotyard.view.window.Window;
import it.unibo.scotyard.view.window.WindowImpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameViewImpl implements GameView {

    private static final int SMALL_WINDOW_WIDTH = 100;
    private static final int SMALL_WINDOW_HEIGHT = 70;
    private static final String RULES_WINDOW_TITLE = "Regole";
    private static final String GAME_OVER_WINDOW_TITLE = "Game Over";
    private static final int SPACING = 15;

    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0); // black
    private static final Color ACCENT_COLOR = new Color(31, 81, 255); // neon blue

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    private static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 20);

    private MapPanel mapPanel;
    private SidebarPanel sidebar;
    private JPanel mainPanel;
    private Window gameOverWindow;

    private GameController observer;

    public GameViewImpl(final MapInfo mapInfo) {
        this.mapPanel = new MapPanel(mapInfo);
        this.sidebar = new SidebarPanel(this);
        this.createGameOverWindow();
        
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.sidebar, BorderLayout.EAST);
        this.mainPanel.add(this.mapPanel, BorderLayout.CENTER);
    }

    @Override
    public void setObserver(GameController gameController){
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

    public void createGameOverWindow(){
        final Size smallSize = Size.of(SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT);
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.add(Box.createVerticalGlue());
        JLabel titleLabel = new JLabel("GAME OVER!");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createHorizontalStrut(SPACING));
        JButton button = new JButton("Ritorna al menù principale");
        button.setFont(TEXT_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);
        panel.add(Box.createHorizontalStrut(SPACING));
        panel.add(Box.createVerticalGlue());

        this.gameOverWindow = new WindowImpl(smallSize, panel, GAME_OVER_WINDOW_TITLE);
        this.gameOverWindow.setsMainFeatures(smallSize);
        this.gameOverWindow.display();

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                observer.loadMainMenu();
                gameOverWindow.close();
            }
        });
    }

    @Override
    public void displayGameOverWindow(){
        this.gameOverWindow.display();
    }
}
