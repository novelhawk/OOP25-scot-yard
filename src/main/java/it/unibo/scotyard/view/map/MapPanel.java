package it.unibo.scotyard.view.map;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.dtos.map.Node;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.game.GameView;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The map panel.
 *
 */
public final class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(MapPanel.class.getName());

    private static final int NODE_RADIUS = 14;
    private static final int NODE_LABEL_SIZE = 10;
    private static final Font NODE_FONT = new Font(ScotFont.ARIAL, Font.BOLD, NODE_LABEL_SIZE);

    // Dimensioni originali del background
    private static final int ORIGINAL_BACKGROUND_WIDTH = 2570;
    private static final int ORIGINAL_BACKGROUND_HEIGHT = 1926;

    // Zoom settings
    private static final double MIN_ZOOM = 1.0;
    private static final double MAX_ZOOM = 10.0;
    private static final double ZOOM_STEP = 0.1; // Ridotto per trackpad più fluido
    private static final double ZOOM_COMPARISON = 0.001;

    // Scaling factors per i nodi durante lo zoom
    // I nodi scalano meno del background per rimanere leggibili
    private static final double NODE_SCALE_FACTOR = 0.5; // I nodi scalano al 50% rispetto allo zoom

    // Game State (Detective Mode)
    private static final NodeId POSITION_NOT_SET = new NodeId(-1);

    private final MapInfo mapInfo;
    private BufferedImage backgroundImage;

    private double baseScaleX = 1.0;
    private double baseScaleY = 1.0;
    private double zoomLevel = 1.0;
    private double currentScaleX = 1.0;
    private double currentScaleY = 1.0;

    private int baseOffsetX;
    private int baseOffsetY;
    private int panOffsetX;
    private int panOffsetY;

    private int scaledBackgroundWidth;
    private int scaledBackgroundHeight;
    private boolean scaleCalculated;

    private final Map<NodeId, Point2D> scaledNodePositions = new HashMap<>();

    // Pan dragging
    private Point dragStartPoint;
    private int dragStartPanX;
    private int dragStartPanY;

    // Game Status
    private NodeId misterXPosition;
    private NodeId detectivePosition;
    private final List<NodeId> bobbiesPositions;
    private Set<NodeId> possibleDestinations;
    private NodeId selectedDestination;
    private Set<it.unibo.scotyard.model.game.turn.TurnManagerImpl.MoveOption> validMoves = new HashSet<>();
    private Consumer<NodeId> nodeClickListener;

    private final GameView gameView;

    /**
     * Creates a new MapPanel with the given map info DTO.
     *
     * @param mapInfo the map info DTO to render
     * @param view    the game view
     * @throws NullPointerException if mapInfo is null
     */
    public MapPanel(final MapInfo mapInfo, final GameView view) {
        this.mapInfo = Objects.requireNonNull(mapInfo, "Map info cannot be null");
        this.gameView = view;
        this.misterXPosition = POSITION_NOT_SET;
        this.detectivePosition = POSITION_NOT_SET;
        this.bobbiesPositions = new ArrayList<>();
        this.possibleDestinations = new HashSet<>();
        this.selectedDestination = POSITION_NOT_SET;
        setupPanel();
    }

    private void setupPanel() {
        setOpaque(true);
        setBackground(ScotColors.BACKGROUND_COLOR);
        loadBackgroundImage();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                scaleCalculated = false;
                zoomLevel = MIN_ZOOM;
                panOffsetX = 0;
                panOffsetY = 0;
                repaint();
            }
        });

        // Mouse wheel listener per zoom con mouse e trackpad
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e) {
                // Usa getPreciseWheelRotation per supporto migliore del trackpad
                final double rotation = e.getPreciseWheelRotation();

                if (rotation < 0) {
                    // Zoom in
                    zoomIn(e.getPoint());
                } else if (rotation > 0) {
                    // Zoom out
                    zoomOut();
                }
            }
        });

        // Mouse listeners per pan
        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (isZoomed()) {
                    dragStartPoint = e.getPoint();
                    dragStartPanX = panOffsetX;
                    dragStartPanY = panOffsetY;
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                if (isZoomed() && dragStartPoint != null) {
                    final int dx = e.getX() - dragStartPoint.x;
                    final int dy = e.getY() - dragStartPoint.y;

                    panOffsetX = dragStartPanX + dx;
                    panOffsetY = dragStartPanY + dy;

                    constrainPan();
                    cacheScaledNodePositions();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                dragStartPoint = null;
                if (isZoomed()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (isZoomed()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                handleNodeClick(e.getX(), e.getY());
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    /**
     * Zooms in centered on the specified point (or center if null).
     *
     * @param centerPoint the point to center zoom on, null for window center
     */
    public void zoomIn(final Point centerPoint) {
        final double oldZoom = zoomLevel;
        zoomLevel = Math.min(MAX_ZOOM, zoomLevel + ZOOM_STEP);

        if (Math.abs(oldZoom - zoomLevel) > ZOOM_COMPARISON) {
            final Point zoomCenter;
            if (centerPoint != null) {
                zoomCenter = centerPoint;
            } else {
                zoomCenter = new Point(getWidth() / 2, getHeight() / 2);
            }

            final double mapX = (zoomCenter.x - baseOffsetX - panOffsetX) / oldZoom;
            final double mapY = (zoomCenter.y - baseOffsetY - panOffsetY) / oldZoom;

            panOffsetX = (int) (zoomCenter.x - baseOffsetX - mapX * zoomLevel);
            panOffsetY = (int) (zoomCenter.y - baseOffsetY - mapY * zoomLevel);
            constrainPan();
        }

        updateZoom();
    }

    /** Zooms out towards initial state. */
    public void zoomOut() {
        final double oldZoom = zoomLevel;
        zoomLevel = Math.max(MIN_ZOOM, zoomLevel - ZOOM_STEP);

        if (Math.abs(zoomLevel - MIN_ZOOM) < ZOOM_COMPARISON) {
            zoomLevel = MIN_ZOOM;
            panOffsetX = 0;
            panOffsetY = 0;
        } else if (Math.abs(oldZoom - zoomLevel) > ZOOM_COMPARISON) {
            // Mantieni il centro durante il dezoom
            final Point zoomCenter = new Point(getWidth() / 2, getHeight() / 2);
            final double mapX = (zoomCenter.x - baseOffsetX - panOffsetX) / oldZoom;
            final double mapY = (zoomCenter.y - baseOffsetY - panOffsetY) / oldZoom;

            panOffsetX = (int) (zoomCenter.x - baseOffsetX - mapX * zoomLevel);
            panOffsetY = (int) (zoomCenter.y - baseOffsetY - mapY * zoomLevel);

            constrainPan();
        }

        updateZoom();
    }

    /** Resets zoom to initial state. */
    public void resetZoom() {
        zoomLevel = MIN_ZOOM;
        panOffsetX = 0;
        panOffsetY = 0;
        updateZoom();
    }

    private void updateZoom() {
        currentScaleX = baseScaleX * zoomLevel;
        currentScaleY = baseScaleY * zoomLevel;
        cacheScaledNodePositions();

        if (isZoomed()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }

        repaint();
    }

    private boolean isZoomed() {
        return zoomLevel > MIN_ZOOM + ZOOM_COMPARISON;
    }

    private void constrainPan() {
        if (!isZoomed()) {
            panOffsetX = 0;
            panOffsetY = 0;
            return;
        }

        final int availableWidth = getWidth();
        final int availableHeight = getHeight();

        final int zoomedWidth = (int) (scaledBackgroundWidth * zoomLevel);
        final int zoomedHeight = (int) (scaledBackgroundHeight * zoomLevel);

        final int minX = availableWidth - zoomedWidth - baseOffsetX;
        final int maxX = -baseOffsetX;
        final int minY = availableHeight - zoomedHeight - baseOffsetY;
        final int maxY = -baseOffsetY;

        panOffsetX = Math.max(minX, Math.min(maxX, panOffsetX));
        panOffsetY = Math.max(minY, Math.min(maxY, panOffsetY));
    }

    private void calculateScaleAndOffset() {
        final int availableWidth = getWidth();
        final int availableHeight = getHeight();

        if (availableWidth <= 0 || availableHeight <= 0) {
            return;
        }

        // Scala separatamente X e Y per riempire tutto lo spazio senza barre grigie
        baseScaleX = (double) availableWidth / ORIGINAL_BACKGROUND_WIDTH;
        baseScaleY = (double) availableHeight / ORIGINAL_BACKGROUND_HEIGHT;

        currentScaleX = baseScaleX * zoomLevel;
        currentScaleY = baseScaleY * zoomLevel;

        scaledBackgroundWidth = availableWidth;
        scaledBackgroundHeight = availableHeight;

        baseOffsetX = 0;
        baseOffsetY = 0;

        cacheScaledNodePositions();

        scaleCalculated = true;
    }

    private void cacheScaledNodePositions() {
        scaledNodePositions.clear();

        final int totalOffsetX = baseOffsetX + panOffsetX;
        final int totalOffsetY = baseOffsetY + panOffsetY;

        for (final Node node : mapInfo.getNodes().toList()) {
            final int screenX = (int) (node.getX() * currentScaleX) + totalOffsetX;
            final int screenY = (int) (node.getY() * currentScaleY) + totalOffsetY;
            scaledNodePositions.put(node.getId(), new Point2D.Double(screenX, screenY));
        }
    }

    private void loadBackgroundImage() {
        try {
            final InputStream imageStream =
                    getClass().getClassLoader().getResourceAsStream("it/unibo/scotyard/view/map/background.png");
            if (imageStream != null) {
                backgroundImage = ImageIO.read(imageStream);
            } else {
                LOGGER.log(Level.SEVERE, "Background image not found");
            }
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading background image: " + e.getMessage());
            backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        if (!scaleCalculated) {
            calculateScaleAndOffset();
        }

        final Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        drawBackground(g2d);
        drawNodes(g2d);
        drawGameStatus(g2d);
    }

    private void drawBackground(final Graphics2D g2d) {
        if (backgroundImage != null) {
            final int zoomedWidth = (int) (scaledBackgroundWidth * zoomLevel);
            final int zoomedHeight = (int) (scaledBackgroundHeight * zoomLevel);
            final int drawX = baseOffsetX + panOffsetX;
            final int drawY = baseOffsetY + panOffsetY;

            g2d.drawImage(backgroundImage, drawX, drawY, zoomedWidth, zoomedHeight, null);
        }
    }

    private void drawNodes(final Graphics2D g2d) {
        // Calcola lo scaling ridotto per i nodi
        // I nodi scalano meno del background per rimanere sempre leggibili
        final double nodeZoom = 1.0 + (zoomLevel - 1.0) * NODE_SCALE_FACTOR;
        final Font scaledFont = NODE_FONT.deriveFont((float) (NODE_LABEL_SIZE * nodeZoom));
        g2d.setFont(scaledFont);

        for (final Node node : mapInfo.getNodes().toList()) {
            drawNode(g2d, node, nodeZoom);
        }
    }

    private void drawNode(final Graphics2D g2d, final Node node, final double nodeZoom) {
        final Point2D pos = scaledNodePositions.get(node.getId());

        if (pos == null) {
            return;
        }

        final int x = (int) pos.getX();
        final int y = (int) pos.getY();

        // Usa il nodeZoom ridotto invece del zoomLevel completo
        final int scaledRadius = (int) (NODE_RADIUS * nodeZoom);

        final var transports = node.getAvailableTransports();
        final boolean hasFerry = transports.contains(TransportType.FERRY);

        final var displayTransports = transports.stream()
                .filter(t -> t != TransportType.TAXI)
                .sorted()
                .toList();

        // Ombra
        g2d.setColor(ScotColors.SHADOW_COLOR);
        g2d.fillOval(x - scaledRadius + 2, y - scaledRadius + 2, scaledRadius * 2, scaledRadius * 2);

        // Riempimento bianco
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - scaledRadius, y - scaledRadius, scaledRadius * 2, scaledRadius * 2);

        // Bordo interno
        final int borderRadius = scaledRadius - Math.max(2, (int) (2 * nodeZoom));
        g2d.setColor(Color.BLACK);
        final float strokeWidth = Math.max(2.0f, 3.0f * (float) nodeZoom);

        if (hasFerry) {
            final float dashLength = Math.max(4.0f, 6.0f * (float) nodeZoom);
            final float gapLength = Math.max(3.0f, 4.0f * (float) nodeZoom);
            final float[] dashPattern = {dashLength, gapLength};
            g2d.setStroke(new BasicStroke(
                    strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dashPattern, 0.0f));
        } else {
            g2d.setStroke(new BasicStroke(strokeWidth));
        }
        g2d.drawOval(x - borderRadius, y - borderRadius, borderRadius * 2, borderRadius * 2);

        // Archi colorati esterni
        if (!displayTransports.isEmpty()) {
            final int arcThickness = Math.max(3, (int) (4 * nodeZoom));
            final int arcRadius = scaledRadius;
            final int startAngle = 90;
            final int anglePerSegment = 360 / displayTransports.size();

            for (int i = 0; i < displayTransports.size(); i++) {
                g2d.setColor(getTransportColor(displayTransports.get(i)));
                g2d.setStroke(new BasicStroke(arcThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawArc(
                        x - arcRadius,
                        y - arcRadius,
                        arcRadius * 2,
                        arcRadius * 2,
                        startAngle - (i * anglePerSegment),
                        -anglePerSegment);
            }
        }

        // ID nodo
        g2d.setColor(Color.BLACK);
        final String label = String.valueOf(node.getId().id());
        final FontMetrics fm = g2d.getFontMetrics();
        final int textWidth = fm.stringWidth(label);
        final int textHeight = fm.getAscent();
        g2d.drawString(label, x - textWidth / 2, y + textHeight / 2 - Math.max(2, (int) (2 * nodeZoom)));
    }

    private Color getTransportColor(final TransportType transport) {
        return switch (transport) {
            case TAXI -> ScotColors.TAXI_COLOR;
            case BUS -> ScotColors.BUS_COLOR;
            case UNDERGROUND -> ScotColors.UNDERGROUND_COLOR;
            case FERRY -> ScotColors.FERRY_COLOR;
        };
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(ORIGINAL_BACKGROUND_WIDTH, ORIGINAL_BACKGROUND_HEIGHT);
    }

    /**
     * Gets the map info being rendered.
     *
     * @return the map info DTO
     */
    public MapInfo getMapInfo() {
        return this.mapInfo;
    }

    // Detective Game Mode

    /**
     * Sets the position of the Detective on the map.
     *
     * @param position the node ID where the Detective is located
     */
    public void setDetectivePosition(final NodeId position) {
        this.detectivePosition = position;
    }

    /**
     * * Sets the position of Mister X on the map.
     *
     * @param position the node ID where Mister X is located
     */
    public void setMisterXPosition(final NodeId position) {
        this.misterXPosition = position;
    }

    /**
     * Initializes the Bobby positions on the map.
     *
     * @param numberOfBobbies the total number of bobbies in the game
     */
    public void initializeBobbies(final int numberOfBobbies) {
        for (int i = 0; i < numberOfBobbies; i++) {
            this.bobbiesPositions.add(i, new NodeId(-1));
        }
    }

    /**
     * Sets a Bobby's position at a specific index.
     *
     * @param position   the node ID where the Bobby should be placed
     * @param indexBobby the index of the Bobby (0-based)
     */
    public void setBobbyPosition(final NodeId position, final int indexBobby) {
        this.bobbiesPositions.set(indexBobby, position);
    }

    /**
     * Loads the possible destinations for movement.
     *
     * @param destinations set of valid destination node IDs
     */
    public void loadPossibleDestinations(final Set<NodeId> destinations) {
        this.possibleDestinations = destinations;
    }

    /**
     * Sets the currently selected destination.
     *
     * @param destination the node ID of the selected destination
     */
    public void setSelectedDestination(final NodeId destination) {
        this.selectedDestination = destination;
    }

    /**
     * Sets the valid moves for highlighting.
     *
     * @param moves the set of valid move options
     */
    public void setValidMoves(final Set<it.unibo.scotyard.model.game.turn.TurnManagerImpl.MoveOption> moves) {
        this.validMoves = moves != null ? new HashSet<>(moves) : new HashSet<>();
    }

    /**
     * Handles node click detection. Finds which node (if any) was clicked based on
     * mouse coordinates.
     *
     * @param mouseX the mouse X coordinate
     * @param mouseY the mouse Y coordinate
     */
    private void handleNodeClick(final int mouseX, final int mouseY) {
        final double nodeZoom = 1.0 + (zoomLevel - 1.0) * NODE_SCALE_FACTOR;
        final int scaledRadius = (int) (NODE_RADIUS * nodeZoom);

        // Check if click is within node radius
        for (final Node node : mapInfo.getNodes().toList()) {
            final Point2D pos = scaledNodePositions.get(node.getId());
            if (pos != null) {
                final int nodeX = (int) pos.getX();
                final int nodeY = (int) pos.getY();

                // Distance of click from node centre
                final double distance = Math.sqrt(Math.pow(mouseX - nodeX, 2) + Math.pow(mouseY - nodeY, 2));

                if (distance <= scaledRadius) {
                    // Use nodeClickListener if set (Mr. X mode)
                    if (nodeClickListener != null) {
                        nodeClickListener.accept(node.getId());
                        return;
                    }
                    // Otherwise use possibleDestinations (Detective mode)
                    for (final NodeId possibleDestination : this.possibleDestinations) {
                        if (possibleDestination.equals(node.getId())) {
                            this.setSelectedDestination(node.getId());
                            this.gameView.destinationChosen(node.getId());
                        }
                    }
                }
            }
        }
    }

    /**
     * Draws a player marker on the map.
     *
     * @param g2d          the graphics context
     * @param playerString the player identifier string
     * @param position     the position to draw at
     * @param scaledRadius the radius of the marker
     * @param nodeZoom     the zoom level for the node
     */
    private void drawPlayer(
            final Graphics2D g2d,
            final String playerString,
            final NodeId position,
            final int scaledRadius,
            final double nodeZoom) {
        if (position.id() > 0) {
            final Point2D pos = scaledNodePositions.get(position);
            if (pos != null) {
                final int x = (int) pos.getX();
                final int y = (int) pos.getY();

                // Player circle
                if (ViewConstants.DETECTIVE_PAWN.equals(playerString)) {
                    g2d.setColor(ScotColors.DETECTIVE_COLOR);
                } else if (ViewConstants.MRX_PAWN.equals(playerString)) {
                    g2d.setColor(ScotColors.MISTER_X_COLOR);
                } else if (playerString.startsWith("B")) {
                    g2d.setColor(ScotColors.BOBBIES_COLOR);
                }
                g2d.fillOval(x - scaledRadius, y - scaledRadius, scaledRadius * 2, scaledRadius * 2);

                // Bordo colorato
                if (ViewConstants.DETECTIVE_PAWN.equals(playerString)) {
                    g2d.setColor(ScotColors.DETECTIVE_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2.0f * (float) nodeZoom));
                } else if (ViewConstants.MRX_PAWN.equals(playerString)) {
                    g2d.setColor(ScotColors.MISTER_X_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(3.0f * (float) nodeZoom));
                } else if (playerString.startsWith("B")) {
                    g2d.setColor(ScotColors.BOBBIES_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2.0f * (float) nodeZoom));
                }
                g2d.drawOval(x - scaledRadius, y - scaledRadius, scaledRadius * 2, scaledRadius * 2);

                // Player text (white)
                g2d.setColor(Color.WHITE);
                final int fontSize;
                if (ViewConstants.MRX_PAWN.equals(playerString)) {
                    fontSize = (int) (18 * nodeZoom);
                } else {
                    fontSize = (int) (16 * nodeZoom);
                }
                g2d.setFont(new Font(ScotFont.ARIAL, Font.BOLD, fontSize));
                final FontMetrics fontMetrics = g2d.getFontMetrics();
                final String label = playerString;
                final int textWidth = fontMetrics.stringWidth(label);
                final int textHeight = fontMetrics.getAscent();
                g2d.drawString(label, x - textWidth / 2, y + textHeight / 2 - 2);
            }
        }
    }

    /**
     * Sets the node click listener for interactive gameplay.
     *
     * @param listener the listener to call when a node is clicked (null to disable)
     */
    public void setNodeClickListener(final Consumer<NodeId> listener) {
        this.nodeClickListener = listener;
    }

    private void drawGameStatus(final Graphics2D g2d) {
        final double nodeZoom = 1.0 + (zoomLevel - 1.0) * NODE_SCALE_FACTOR;
        final int scaledRadius = (int) (NODE_RADIUS * nodeZoom);

        // Render valid moves (green)
        Set<NodeId> validDestinations = new HashSet<>();

        if (nodeClickListener != null) {
            // Mr. X mode: usa validMoves
            for (final var move : validMoves) {
                validDestinations.add(move.getDestinationNode());
            }
        } else {
            // Detective mode: usa possibleDestinations
            validDestinations = this.possibleDestinations;
        }

        // Render green node
        for (final NodeId nodeId : validDestinations) {
            final Point2D pos = scaledNodePositions.get(nodeId);
            if (pos != null) {
                final int x = (int) pos.getX();
                final int y = (int) pos.getY();
                g2d.setColor(ScotColors.VALID_NODE); // green semi-transparent
                g2d.fillOval(
                        x - scaledRadius - 4, y - scaledRadius - 4, (scaledRadius + 4) * 2, (scaledRadius + 4) * 2);
            }
        }

        // Render selected destination (blu border)
        if (selectedDestination.id() >= 0) {
            final Point2D pos = scaledNodePositions.get(selectedDestination);
            if (pos != null) {
                final int x = (int) pos.getX();
                final int y = (int) pos.getY();
                g2d.setColor(ScotColors.SELECTED_NODE); // blu semi-transparent
                g2d.setStroke(new BasicStroke(4.0f * (float) nodeZoom));
                g2d.drawOval(
                        x - scaledRadius - 2, y - scaledRadius - 2, (scaledRadius + 2) * 2, (scaledRadius + 2) * 2);
            }
        }

        // Render players
        this.drawPlayer(g2d, ViewConstants.DETECTIVE_PAWN, this.detectivePosition, scaledRadius, nodeZoom);
        this.drawPlayer(g2d, ViewConstants.MRX_PAWN, this.misterXPosition, scaledRadius, nodeZoom);
        for (int i = 0; i < this.bobbiesPositions.size(); i++) {
            this.drawPlayer(
                    g2d, ViewConstants.BOBBIES_PAWN + (i + 1), this.bobbiesPositions.get(i), scaledRadius, nodeZoom);
        }
    }
}
