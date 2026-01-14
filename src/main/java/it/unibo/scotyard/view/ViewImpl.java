package it.unibo.scotyard.view;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.controller.gamelauncher.GameLauncherController;
import it.unibo.scotyard.view.game.GameView;
import it.unibo.scotyard.view.game.GameViewImpl;
import it.unibo.scotyard.view.gamelauncher.GameLauncherView;
import it.unibo.scotyard.view.gamelauncher.GameLauncherViewImpl;
import it.unibo.scotyard.view.map.MapPanel;
import it.unibo.scotyard.view.resources.IconRegistry;
import it.unibo.scotyard.view.resources.IconRegistryImpl;
import it.unibo.scotyard.view.window.Window;
import it.unibo.scotyard.view.window.WindowImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** view implementation coordinating all UI components. */
public final class ViewImpl implements View {
    private static final String MAIN_WINDOW_TITLE = "Scotland Yard";

    private final Window window;
    private final JPanel mainContainer;
    private final IconRegistry iconRegistry;

    /** new view instance. */
    public ViewImpl() {
        this.mainContainer = new JPanel(new BorderLayout());
        this.window = new WindowImpl(this.getMaxResolution(), this.mainContainer, MAIN_WINDOW_TITLE);
        this.iconRegistry = new IconRegistryImpl();
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
    public void setWindowMainFeatures(final Size resolution) {
        Objects.requireNonNull(this.window, "window cannot be null");
        this.window.setsMainFeatures(resolution);
    }

    @Override
    public GameView createGameView(final MapInfo mapInfo) {
        Objects.requireNonNull(mapInfo, "MapInfo cannot be null");
        return new GameViewImpl(iconRegistry, mapInfo);
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

    /**
     * Force UI layout update on EDT
     *
     * @param mainPanel the main panel
     * @param mapPanel the map panel
     */
    public void forceLayoutUpdate(final JPanel mainPanel, final MapPanel mapPanel) {
        SwingUtilities.invokeLater(() -> {
            mainPanel.revalidate();
            if (mapPanel != null) {
                mapPanel.revalidate();
                mapPanel.repaint();
            }
        });
    }
}
