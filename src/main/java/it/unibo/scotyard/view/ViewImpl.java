package it.unibo.scotyard.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import it.unibo.scotyard.view.gamelauncher.GameLauncherView;
import it.unibo.scotyard.view.gamelauncher.GameLauncherViewImpl;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.sidebar.Sidebar;
import it.unibo.scotyard.view.window.Window;
import it.unibo.scotyard.view.window.WindowImpl;

/**
 * view implementation coordinating all UI components
 */
public final class ViewImpl implements View {

    private Window window;
    private MapPanel mapPanel;
    private Sidebar sidebar;
    private JPanel mainContainer;

    /**
     * new view instance.
     */
    public ViewImpl() {
        this.mainContainer = new JPanel(new BorderLayout());
        this.window = new WindowImpl(this.getMaxResolution());
    }

    /**
     * Initializes the view with map data.
     * Must be called before displaying the window.
     * 
     * @param mapInfo the map information DTO
     * @throws NullPointerException if mapInfo is null
     */
    public void initialize(final MapInfo mapInfo) {
        Objects.requireNonNull(mapInfo, "MapInfo cannot be null");

        this.mapPanel = new MapPanel(mapInfo);
        this.sidebar = new Sidebar();
    }

    @Override
    public void display() {
        if (this.window == null) {
            throw new IllegalStateException("Window non inizializzata. Chiamare displayWindow() prima.");
        }
        this.window.display();
        forceLayoutUpdate();
    }

    @Override
    public JPanel getContentPane() {
        return this.mainContainer;
    }

    /**
     * @return the sidebar component
     */
    public Sidebar getSidebar() {
        return this.sidebar;
    }

    /**
     * @return the map panel component
     */
    public MapPanel getMapPanel() {
        return this.mapPanel;
    }

    @Override
    public void displayLauncher(final GameLauncherController controller) {
        Objects.requireNonNull(controller, "Controller cannot be null");

        final GameLauncherView launcherView = new GameLauncherViewImpl(controller);
        launcherView.display();
    }

    @Override
    public void setWindowMainFeatures(){
        this.window.setsMainFeatures();
    }

    @Override
    public void displayPanel(JPanel panel){
        this.mainContainer = panel;
        this.window.setBody(this.mainContainer);
        this.window.display();
    }

    @Override
    public void displayGameWindow(final Size resolution) {
        Objects.requireNonNull(resolution, "Resolution cannot be null");
        
        this.createGamePanel();
        this.window.setBody(this.mainContainer);
        this.window.display();

        forceLayoutUpdate();
    }

    @Override
    public void createGamePanel(){
        this.mainContainer = new JPanel(new BorderLayout());
        this.mainContainer.add(this.sidebar, BorderLayout.EAST);
        this.mainContainer.add(this.mapPanel, BorderLayout.CENTER);
    }

    @Override
    public Size getMaxResolution() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return Size.of((int) screenSize.getWidth(), (int) screenSize.getHeight());
    }

    // Force UI layout update on EDT
    private void forceLayoutUpdate() {
        SwingUtilities.invokeLater(() -> {
            this.mainContainer.revalidate();
            if (this.mapPanel != null) {
                this.mapPanel.revalidate();
                this.mapPanel.repaint();
            }
        });
    }
}