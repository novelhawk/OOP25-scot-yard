package it.unibo.scotyard.view.map;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import it.unibo.scotyard.commons.dtos.map.Connection;
import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.dtos.map.Node;
import it.unibo.scotyard.model.map.TransportType;

public class MapPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final int NODE_RADIUS = 14;
    private static final int NODE_LABEL_SIZE = 10;
    private static final Font NODE_FONT = new Font("Arial", Font.BOLD, NODE_LABEL_SIZE);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font LEGEND_FONT = new Font("Arial", Font.PLAIN, 11);
    private static final Font LEGEND_TITLE_FONT = new Font("Arial", Font.BOLD, 12);

    private static final float TAXI_WIDTH = 3.0f;
    private static final float BUS_WIDTH = 6.5f;
    private static final float UNDERGROUND_WIDTH = 3.0f;
    private static final float FERRY_WIDTH = 4.0f;

    private static final Color BACKGROUND_COLOR = new Color(170, 170, 170);
    private static final Color TAXI_COLOR = new Color(255, 255, 85); // yellow
    private static final Color BUS_COLOR = new Color(58, 132, 36); // green
    private static final Color UNDERGROUND_COLOR = new Color(200, 43, 29); // red
    private static final Color FERRY_COLOR = new Color(0, 0, 0); // black
    private static final Color NODE_FILL_COLOR = new Color(255, 255, 255);
    private static final Color NODE_TEXT_COLOR = new Color(33, 33, 33);
    private static final Color TITLE_COLOR = new Color(33, 33, 33);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);

    private static final int MARGIN = 100;
    private static final int LEGEND_X = 30;
    private static final int LEGEND_Y = 80;
    private static final int LEGEND_SPACING = 25;
    private static final int LEGEND_LINE_LENGTH = 40;

    private final MapInfo mapInfo;
    private BufferedImage backgroundImage;
    private BufferedImage scaledBackgroundImage;

    private double currentScale = 1.0; // scale factor for logical → screen conversion
    private int offsetX = 0; // screen offset for centering
    private int offsetY = 0; // screen offset for centering
    private int originalMapWidth = 0; // logical map width
    private int originalMapHeight = 0; // logical map height
    private boolean scaleCalculated = false; // flag to avoid recalculating every frame

    private final Map<Integer, Point2D> scaledNodePositions = new HashMap<>();

    /**
     * Creates a new MapPanel with the given map info DTO.
     *
     * @param mapInfo the map info DTO to render
     */
    public MapPanel(final MapInfo mapInfo) {
        this.mapInfo = Objects.requireNonNull(mapInfo, "Map info cannot be null");
        setupPanel();
    }

    /**
     * Sets up the panel with initial configuration.
     */
    private void setupPanel() {
        setOpaque(true);
        setBackground(BACKGROUND_COLOR);
        loadBackgroundImage();
        calculateOriginalMapBounds();

        // handle resize events
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {

                scaleCalculated = false;
                repaint();
            }
        });
    }

    /**
     * Calculates the original map bounds based on node positions.
     * This establishes the logical coordinate space boundaries.
     * 
     */
    private void calculateOriginalMapBounds() {
        int maxX = 0;
        int maxY = 0;

        // find maximum coordinates to establish logical space bounds
        for (final Node node : mapInfo.getNodes().toList()) {
            maxX = Math.max(maxX, node.getX());
            maxY = Math.max(maxY, node.getY());
        }

        // store logical space dimensions
        originalMapWidth = maxX;
        originalMapHeight = maxY;
    }

    /**
     * Calculates the dynamic scale and offset to fit the map in the available
     * space.
     * Also pre-scales the background image for performance
     */
    private void calculateScaleAndOffset() {
        final int availableWidth = getWidth();
        final int availableHeight = getHeight();

        if (availableWidth <= 0 || availableHeight <= 0 || originalMapWidth <= 0 || originalMapHeight <= 0) {
            return;
        }

        // calculate scale to fit both dimensions with margins
        final double scaleX = (double) (availableWidth - MARGIN * 2) / originalMapWidth;
        final double scaleY = (double) (availableHeight - MARGIN * 2) / originalMapHeight;

        // use the smaller scale to ensure everything fits
        currentScale = Math.min(scaleX, scaleY);

        // calculate offsets to center the map
        final int scaledWidth = (int) (originalMapWidth * currentScale);
        final int scaledHeight = (int) (originalMapHeight * currentScale);

        offsetX = (availableWidth - scaledWidth) / 2;
        offsetY = (availableHeight - scaledHeight) / 2;

        // Pre-scale background image
        prescaleBackgroundImage(availableWidth, availableHeight);

        // Cache scaled node positions
        cacheScaledNodePositions();

        // Mark as calculated to avoid recalculating every frame
        scaleCalculated = true;
    }

    /**
     * Caches scaled positions for all nodes
     * Similar to their approach of converting logical coordinates to screen
     * coordinates once.
     */
    private void cacheScaledNodePositions() {
        scaledNodePositions.clear();
        for (final Node node : mapInfo.getNodes().toList()) {
            final int screenX = (int) (node.getX() * currentScale) + offsetX;
            final int screenY = (int) (node.getY() * currentScale) + offsetY;
            scaledNodePositions.put(node.getId(), new Point2D.Double(screenX, screenY));
        }
    }

    /**
     * Pre-scales the background image to the current window size.
     * Similar to their ImageLoader(cellSize) approach.
     */
    private void prescaleBackgroundImage(final int width, final int height) {
        if (backgroundImage != null && width > 0 && height > 0) {
            scaledBackgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2d = scaledBackgroundImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, width, height, null);
            g2d.dispose();
        }
    }

    /**
     * Loads the background image from resources.
     */
    private void loadBackgroundImage() {
        try {
            final InputStream imageStream = getClass().getClassLoader()
                    .getResourceAsStream("it/unibo/scotyard/view/map/background.png");
            if (imageStream != null) {
                backgroundImage = ImageIO.read(imageStream);
            } else {
                System.err.println("Background image not found, using solid color background");
            }
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundImage = null;
        }
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        // only recalculate scale if not already calculated
        if (!scaleCalculated) {
            calculateScaleAndOffset();
        }

        final Graphics2D g2d = (Graphics2D) g;

        // Enable maximum quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Draw background image if available
        drawBackground(g2d);

        // Draw title
        drawTitle(g2d);

        // Draw legend
        drawLegend(g2d);

        // Draw connections
        drawConnections(g2d);

        // Draw nodes with shadows
        drawNodes(g2d);

    }

    private void drawBackground(final Graphics2D g2d) {
        if (scaledBackgroundImage != null) {
            // draw the pre-scaled background image (no scaling needed, already done on
            // resize)
            g2d.drawImage(scaledBackgroundImage, 0, 0, null);
        }
    }

    private void drawConnections(final Graphics2D g2d) {
        final var nodesList = mapInfo.getNodes().toList();
        final var allConnections = mapInfo.getConnections().toList();

        // draw non-TAXI connections first
        for (final Connection connection : allConnections) {
            if (connection.getTransport() != TransportType.TAXI) {
                final Node fromNode = findNodeById(nodesList, connection.getFrom());
                final Node toNode = findNodeById(nodesList, connection.getTo());

                if (fromNode != null && toNode != null) {
                    drawConnection(g2d, fromNode, toNode, connection.getTransport(), connection.getWaypoints());
                }
            }
        }

        // draw TAXI connections last (so they appear on top)
        for (final Connection connection : allConnections) {
            if (connection.getTransport() == TransportType.TAXI) {
                final Node fromNode = findNodeById(nodesList, connection.getFrom());
                final Node toNode = findNodeById(nodesList, connection.getTo());

                if (fromNode != null && toNode != null) {
                    drawConnection(g2d, fromNode, toNode, connection.getTransport(), connection.getWaypoints());
                }
            }
        }
    }

    private Node findNodeById(final java.util.List<Node> nodes, final int id) {
        return nodes.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void drawConnection(
            final Graphics2D g2d,
            final Node from,
            final Node to,
            final TransportType transport,
            final java.util.List<Integer> waypoints) {
        // use cached positions
        final Point2D fromPos = scaledNodePositions.get(from.getId());
        final Point2D toPos = scaledNodePositions.get(to.getId());

        if (fromPos == null || toPos == null) {
            return; // skip if positions not cached yet
        }

        // ff no waypoints, draw direct line (original behavior)
        if (waypoints.isEmpty()) {
            drawConnectionSegment(g2d, from, to, transport, fromPos, toPos);
        } else {
            // draw line through waypoints
            final var nodesList = mapInfo.getNodes().toList();

            // draw first waypoint
            final Node firstWaypointNode = findNodeById(nodesList, waypoints.get(0));
            if (firstWaypointNode != null) {
                final Point2D firstWpPos = scaledNodePositions.get(firstWaypointNode.getId());
                if (firstWpPos != null) {
                    drawConnectionSegment(g2d, from, firstWaypointNode, transport, fromPos, firstWpPos);
                }
            }

            // draw between consecutive waypoints
            for (int i = 0; i < waypoints.size() - 1; i++) {
                final Node wpNode1 = findNodeById(nodesList, waypoints.get(i));
                final Node wpNode2 = findNodeById(nodesList, waypoints.get(i + 1));

                if (wpNode1 != null && wpNode2 != null) {
                    final Point2D wp1Pos = scaledNodePositions.get(wpNode1.getId());
                    final Point2D wp2Pos = scaledNodePositions.get(wpNode2.getId());

                    if (wp1Pos != null && wp2Pos != null) {
                        drawConnectionSegment(g2d, wpNode1, wpNode2, transport, wp1Pos, wp2Pos);
                    }
                }
            }

            // draw last waypoint
            final Node lastWaypointNode = findNodeById(nodesList, waypoints.get(waypoints.size() - 1));
            if (lastWaypointNode != null) {
                final Point2D lastWpPos = scaledNodePositions.get(lastWaypointNode.getId());
                if (lastWpPos != null) {
                    drawConnectionSegment(g2d, lastWaypointNode, to, transport, lastWpPos, toPos);
                }
            }
        }
    }

    /**
     * Draws a single connection segment between two positions.
     *
     * @param g2d       the graphics context
     * @param from      the source node (for offset calculation)
     * @param to        the destination node (for offset calculation)
     * @param transport the transport type
     * @param fromPos   the source position
     * @param toPos     the destination position
     */
    private void drawConnectionSegment(
            final Graphics2D g2d,
            final Node from,
            final Node to,
            final TransportType transport,
            final Point2D fromPos,
            final Point2D toPos) {
        final int x1 = (int) fromPos.getX();
        final int y1 = (int) fromPos.getY();
        final int x2 = (int) toPos.getX();
        final int y2 = (int) toPos.getY();

        // calculate offset for parallel connections
        final int offset = getConnectionOffset(from, to, transport);

        // calculate perpendicular offset
        final double dx = x2 - x1;
        final double dy = y2 - y1;
        final double length = Math.sqrt(dx * dx + dy * dy);
        final double perpX = -dy / length * offset;
        final double perpY = dx / length * offset;

        final int offsetX1 = x1 + (int) perpX;
        final int offsetY1 = y1 + (int) perpY;
        final int offsetX2 = x2 + (int) perpX;
        final int offsetY2 = y2 + (int) perpY;

        // Draw shadow for depth
        g2d.setColor(SHADOW_COLOR);
        g2d.setStroke(getTransportStroke(transport, 1.5f));
        g2d.drawLine(offsetX1 + 1, offsetY1 + 1, offsetX2 + 1, offsetY2 + 1);

        // Draw connection
        g2d.setColor(getTransportColor(transport));
        g2d.setStroke(getTransportStroke(transport, 1.0f));
        g2d.drawLine(offsetX1, offsetY1, offsetX2, offsetY2);
    }

    /**
     * Calculates the offset for a connection based on how many connections exist
     * between the same nodes.
     * Special rule: TAXI connections always overlap with other transport types
     * (offset 0).
     *
     * @param from      the source node
     * @param to        the destination node
     * @param transport the current transport type
     * @return the offset in pixels
     */
    private int getConnectionOffset(final Node from, final Node to, final TransportType transport) {
        // count connections between these two nodes
        final var connections = mapInfo.getConnections()
                .filter(c -> (c.getFrom() == from.getId() && c.getTo() == to.getId())
                        || (c.getFrom() == to.getId() && c.getTo() == from.getId()))
                .toList();

        if (connections.size() <= 1) {
            return 0; // No offset for single connection
        }

        // check if there's a TAXI connection among these connections
        final boolean hasTaxi = connections.stream()
                .anyMatch(c -> c.getTransport() == TransportType.TAXI);

        // If there's a TAXI and this connection is TAXI or another transport type,
        // they should overlap (offset 0) to show the layering effect
        if (hasTaxi && connections.size() == 2) {
            return 0; // Overlap: TAXI on top, other transport visible underneath
        }

        // For other cases (multiple non-TAXI connections), distribute them
        final var sortedTransports = connections.stream()
                .map(c -> c.getTransport())
                .sorted()
                .toList();

        final int index = sortedTransports.indexOf(transport);
        final int total = sortedTransports.size();

        // calculate offset: distribute connections evenly
        final int spacing = 8;
        return (index - (total - 1) / 2.0) * spacing != 0 ? (int) ((index - (total - 1) / 2.0) * spacing) : 0;
    }

    /**
     * Draws all nodes on the map.
     *
     * @param g2d the graphics context
     */
    private void drawNodes(final Graphics2D g2d) {
        g2d.setFont(NODE_FONT);

        for (final Node node : mapInfo.getNodes().toList()) {
            drawNode(g2d, node);
        }
    }

    /**
     * Draws a single node with enhanced visibility and transport-based coloring.
     * Using cached positions.
     *
     * @param g2d  the graphics context
     * @param node the node to draw
     */
    private void drawNode(final Graphics2D g2d, final Node node) {
        // Use cached position
        final Point2D pos = scaledNodePositions.get(node.getId());

        if (pos == null) {
            return;
        }

        final int x = (int) pos.getX();
        final int y = (int) pos.getY();

        // get transport types available at this node from the DTO
        final var transports = node.getAvailableTransports();
        final boolean hasFerry = transports.contains(TransportType.FERRY);

        // Filter out TAXI since all nodes have it - only show BUS, UNDERGROUND, FERRY
        final var displayTransports = transports.stream()
                .filter(t -> t != TransportType.TAXI)
                .sorted()
                .toList();

        // draw shadow
        g2d.setColor(SHADOW_COLOR);
        g2d.fillOval(x - NODE_RADIUS + 2, y - NODE_RADIUS + 2, NODE_RADIUS * 2, NODE_RADIUS * 2);

        // draw node with white fill
        g2d.setColor(NODE_FILL_COLOR);
        g2d.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        // draw node border INTERNAL (black, dashed if ferry station)
        final int borderRadius = NODE_RADIUS - 2; // Internal border
        g2d.setColor(Color.BLACK);
        if (hasFerry) {
            // dashed stroke for ferry stations
            final float[] dashPattern = { 6.0f, 4.0f };
            g2d.setStroke(new BasicStroke(
                    3.0f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10.0f,
                    dashPattern,
                    0.0f));
        } else {
            // solid stroke for regular stations
            g2d.setStroke(new BasicStroke(3.0f));
        }
        g2d.drawOval(x - borderRadius, y - borderRadius, borderRadius * 2, borderRadius * 2);

        // draw colored arcs EXTERNAL based on transport types (excluding TAXI)
        if (!displayTransports.isEmpty()) {
            final int arcThickness = 4;
            final int arcRadius = NODE_RADIUS; // arcs start exactly at node edge
            final int startAngle = 90; // start from top
            final int anglePerSegment = 360 / displayTransports.size();

            for (int i = 0; i < displayTransports.size(); i++) {
                g2d.setColor(getTransportColor(displayTransports.get(i)));
                g2d.setStroke(new BasicStroke(arcThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawArc(
                        x - arcRadius, y - arcRadius,
                        arcRadius * 2, arcRadius * 2,
                        startAngle - (i * anglePerSegment),
                        -anglePerSegment);
            }
        }

        // node ID
        g2d.setColor(NODE_TEXT_COLOR);
        final String label = String.valueOf(node.getId());
        final FontMetrics fm = g2d.getFontMetrics();
        final int textWidth = fm.stringWidth(label);
        final int textHeight = fm.getAscent();
        g2d.drawString(label, x - textWidth / 2, y + textHeight / 2 - 2);
    }

    /**
     * Draws the map title centered at the top.
     *
     * @param g2d the graphics context
     */
    private void drawTitle(final Graphics2D g2d) {
        g2d.setColor(TITLE_COLOR);
        g2d.setFont(TITLE_FONT);
        final FontMetrics fm = g2d.getFontMetrics();
        final int titleWidth = fm.stringWidth(mapInfo.getName());
        final int x = (getWidth() - titleWidth) / 2;
        final int y = 40;
        g2d.drawString(mapInfo.getName(), x, y);
    }

    /**
     * Draws the legend showing transport types.
     *
     * @param g2d the graphics context
     */
    private void drawLegend(final Graphics2D g2d) {
        // legend title
        g2d.setFont(LEGEND_TITLE_FONT);
        g2d.setColor(TITLE_COLOR);
        g2d.drawString("Transport Types", LEGEND_X, LEGEND_Y - 20);

        g2d.setFont(LEGEND_FONT);

        final TransportType[] transports = {
                TransportType.TAXI,
                TransportType.BUS,
                TransportType.UNDERGROUND,
                TransportType.FERRY
        };

        final String[] labels = { "Taxi", "Bus", "Underground", "Ferry" };

        for (int i = 0; i < transports.length; i++) {
            final int y = LEGEND_Y + (i * LEGEND_SPACING);

            // shadow
            g2d.setColor(SHADOW_COLOR);
            g2d.setStroke(getTransportStroke(transports[i], 1.2f));
            g2d.drawLine(LEGEND_X + 1, y + 1, LEGEND_X + LEGEND_LINE_LENGTH + 1, y + 1);

            // colored line
            g2d.setColor(getTransportColor(transports[i]));
            g2d.setStroke(getTransportStroke(transports[i], 1.0f));
            g2d.drawLine(LEGEND_X, y, LEGEND_X + LEGEND_LINE_LENGTH, y);

            // label
            g2d.setColor(TITLE_COLOR);
            g2d.drawString(labels[i], LEGEND_X + LEGEND_LINE_LENGTH + 15, y + 5);
        }
    }

    /**
     * Gets the color for a specific transport type.
     *
     * @param transport the transport type
     * @return the corresponding color
     */
    private Color getTransportColor(final TransportType transport) {
        return switch (transport) {
            case TAXI -> TAXI_COLOR;
            case BUS -> BUS_COLOR;
            case UNDERGROUND -> UNDERGROUND_COLOR;
            case FERRY -> FERRY_COLOR;
        };
    }

    final float[] dashPattern = { 5.0f, 12.0f };

    /**
     * Gets the stroke for a specific transport type.
     *
     * @param transport  the transport type
     * @param multiplier width multiplier for effects
     * @return the corresponding stroke
     */
    private BasicStroke getTransportStroke(final TransportType transport, final float multiplier) {
        final float width = switch (transport) {
            case TAXI -> TAXI_WIDTH * multiplier;
            case BUS -> BUS_WIDTH * multiplier;
            case UNDERGROUND -> UNDERGROUND_WIDTH * multiplier;
            case FERRY -> FERRY_WIDTH * multiplier;
        };

        return switch (transport) {
            case FERRY, UNDERGROUND -> new BasicStroke(
                    width,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    10.0f,
                    dashPattern, // Il pattern di dash
                    0.0f);
            default -> new BasicStroke(
                    width,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND);
        };
    }

    /**
     * Gets the map info being rendered.
     *
     * @return the map info DTO
     */
    public MapInfo getMapInfo() {
        return mapInfo;
    }

}
