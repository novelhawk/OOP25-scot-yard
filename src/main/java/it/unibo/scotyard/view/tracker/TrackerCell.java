package it.unibo.scotyard.view.tracker;

import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.game.GameView;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public final class TrackerCell extends JPanel {
    private static final Color TAXI_COLOR = new Color(255, 255, 85);
    private static final Color BUS_COLOR = new Color(58, 132, 36);
    private static final Color UNDERGROUND_COLOR = new Color(200, 43, 29);
    private static final Color FERRY_COLOR = new Color(128, 128, 128);

    private static final Color EMPTY_COLOR = new Color(66, 66, 66);

    private static final Map<TransportType, Color> TRANSPORT_TYPES_COLORS = Map.of(
            TransportType.BUS, BUS_COLOR,
            TransportType.UNDERGROUND, UNDERGROUND_COLOR,
            TransportType.FERRY, FERRY_COLOR,
            TransportType.TAXI, TAXI_COLOR);

    private static final Dimension MINIMUM_SIZE = new Dimension(100, 20);
    private static final Color BACKGROUND_COLOR = new Color(48, 48, 48);

    private final GameView gameView;
    private List<TransportType> tickets;

    public TrackerCell(final GameView gameView) {
        this.gameView = gameView;
        this.tickets = List.of();
        setLayout(new GridLayout(1, 0));
    }

    public void setTickets(final List<TransportType> tickets) {
        this.tickets = tickets;
        createTicketIcons();
    }

    private void createTicketIcons() {
        removeAll();

        for (final TransportType transportType : tickets) {
            final Component component = createTicketIcon(transportType);
            add(component);
        }

        repaint();
        revalidate();
    }

    private Component createTicketIcon(TransportType transportType) {
        final ImageIcon icon = gameView.getIconRegistry().getTransportIcon(transportType);
        return new JLabel(icon);
    }

    @Override
    public Dimension getMinimumSize() {
        return MINIMUM_SIZE;
    }

    @Override
    public Color getBackground() {
        return BACKGROUND_COLOR;
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final float borderRadius = 15;
        final Dimension size = getSize();

        final Shape slot =
                new RoundRectangle2D.Float(0, 0, size.width - 1, size.height - 1, borderRadius, borderRadius);
        g2d.setClip(slot);

        g2d.setColor(EMPTY_COLOR);
        g2d.fillRect(0, 0, size.width, size.height);

        final int sections = tickets.size();
        final int sectionWidth = size.width / Math.max(sections, 1);

        for (int s = 0; s < sections; s++) {
            final TransportType transportType = this.tickets.get(s);
            g2d.setColor(TRANSPORT_TYPES_COLORS.get(transportType));
            g2d.fillRect(sectionWidth * s + 1, 0, sectionWidth, size.height);
        }

        g2d.setClip(null);
        g2d.setColor(Color.black);
        g2d.draw(slot);
    }
}
