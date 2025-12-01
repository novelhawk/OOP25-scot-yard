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
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.game.GameViewImpl;
import it.unibo.scotyard.view.gamelauncher.GameLauncherView;
import it.unibo.scotyard.view.gamelauncher.GameLauncherViewImpl;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.window.Window;
import it.unibo.scotyard.view.window.WindowImpl;

/**
 * view implementation coordinating all UI components.
 */
public final class ViewImpl implements View {
    private static final String MAIN_WINDOW_TITLE = "Scotland Yard";

    private Window window;
    private final JPanel mainContainer;

    /**
     * new view instance.
     */
    public ViewImpl() {
        this.mainContainer = new JPanel(new BorderLayout());
        this.window = new WindowImpl(this.getMaxResolution(), MAIN_WINDOW_TITLE);
    }

    @Override
    public JPanel getContentPane() {
        return this.mainContainer;
    }

    @Override
    public void displayLauncher(final GameLauncherController controller) {
        Objects.requireNonNull(controller, "Controller cannot be null");

        final GameLauncherView launcherView = new GameLauncherViewImpl(controller);
        launcherView.display();
    }

    @Override
    public void setWindowMainFeatures(Size resolution){
        this.window.setsMainFeatures(resolution);
    }

    @Override
    public GameView createGameView(MapInfo mapInfo) {
        Objects.requireNonNull(mapInfo, "MapInfo cannot be null");
        return new GameViewImpl(mapInfo);
    }

    @Override
    public void displayPanel(final JPanel panel) {
        Objects.requireNonNull(panel, "Panel cannot be null");

        this.mainContainer.removeAll();
        this.mainContainer.add(panel, BorderLayout.CENTER);
        this.mainContainer.revalidate();
        this.mainContainer.repaint();

        if (this.window != null && !this.window.isVisible()) {
            this.window.display();
        }
    }

    @Override
    public Size getMaxResolution() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return Size.of((int) screenSize.getWidth(), (int) screenSize.getHeight());
    }

    // Force UI layout update on EDT
    public void forceLayoutUpdate(JPanel mainPanel, MapPanel mapPanel) {
        SwingUtilities.invokeLater(() -> {
            mainPanel.revalidate();
            if (mapPanel != null) {
                mapPanel.revalidate();
                mapPanel.repaint();
            }
        });
    }
}
