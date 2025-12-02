package it.unibo.scotyard.view.game;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;
import it.unibo.scotyard.view.window.Window;
import it.unibo.scotyard.view.window.WindowImpl;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class GameViewImpl implements GameView {

    private static final int SMALL_WINDOW_WIDTH = 700;
    private static final int SMALL_WINDOW_HEIGHT = 270;
    private static final String RULES_WINDOW_TITLE = "Regole";

    private MapPanel mapPanel;
    private SidebarPanel sidebar;
    private JPanel mainPanel;

    public GameViewImpl(final MapInfo mapInfo) {
        this.mapPanel = new MapPanel(mapInfo);
        this.sidebar = new SidebarPanel(this);

        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.sidebar, BorderLayout.EAST);
        this.mainPanel.add(this.mapPanel, BorderLayout.CENTER);
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
}
