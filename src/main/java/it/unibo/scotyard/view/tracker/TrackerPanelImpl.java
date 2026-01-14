package it.unibo.scotyard.view.tracker;

import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.game.GameView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class TrackerPanelImpl extends JPanel implements TrackerPanel {

    private final GameView gameView;

    private static final int COLUMNS = 3;

    private static final int VERTICAL_GAP = 3;
    private static final int HORIZONTAL_GAP = 3;

    private static final Dimension PREFERRED_SIZE = new Dimension(0, 100);
    private static final Color BACKGROUND_COLOR = new Color(48, 48, 48);

    private final List<TrackerCell> cells = new ArrayList<>();

    public TrackerPanelImpl(final GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void createGrid(int count) {
        removeAll();
        setLayout(createGridLayout(count));

        for (int i = 0; i < count; i++) {
            final TrackerCell cell = new TrackerCell(gameView);
            cells.add(cell);
            add(cell);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }

    @Override
    public Color getBackground() {
        return BACKGROUND_COLOR;
    }

    @Override
    public void setTransportModes(int index, List<TransportType> transportTypes) {
        cells.get(index).setTickets(transportTypes);
    }

    private GridLayout createGridLayout(int count) {
        final GridLayout grid = new GridLayout(count / COLUMNS, COLUMNS);
        grid.setHgap(HORIZONTAL_GAP);
        grid.setVgap(VERTICAL_GAP);
        return grid;
    }
}
