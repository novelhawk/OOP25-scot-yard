package it.unibo.scotyard.view.game;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.SidebarPanel;

public class GameViewImpl implements GameView{

    private MapPanel mapPanel;
    private SidebarPanel sidebar;
    private JPanel mainPanel;

    public GameViewImpl(final MapInfo mapInfo){
        this.mapPanel = new MapPanel(mapInfo);
        this.sidebar = new SidebarPanel();

        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.sidebar, BorderLayout.EAST);
        this.mainPanel.add(this.mapPanel, BorderLayout.CENTER);
    }

    @Override
    public JPanel getMainPanel(){
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
}
