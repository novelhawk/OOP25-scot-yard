package it.unibo.scotyard.view.map;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.dtos.map.Node;
import it.unibo.scotyard.model.map.TransportType;
import it.unibo.scotyard.view.game.GameView;

import java.awt.*;
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
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public final class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int NODE_RADIUS = 14;
    private static final int NODE_LABEL_SIZE = 10;
    private static final Font NODE_FONT = new Font("Arial", Font.BOLD, NODE_LABEL_SIZE);
    private static final Font PLAYERS_FONT = new Font("Arial", Font.BOLD, 10);

    // Dimensioni originali del background
    private static final int ORIGINAL_BACKGROUND_WIDTH = 2570;
    private static final int ORIGINAL_BACKGROUND_HEIGHT = 1926;

    private static final Color BACKGROUND_COLOR = new Color(170, 170, 170);
    private static final Color NODE_FILL_COLOR = new Color(255, 255, 255);
    private static final Color NODE_TEXT_COLOR = new Color(33, 33, 33);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);
    private static final Color MISTER_X_COLOR = new Color(0, 0, 0); // black
    private static final Color DETECTIVE_COLOR = new Color(0, 0, 128); // navy blue
    private static final Color BOBBIES_COLOR = new Color(255, 95, 31); // neon orange
    private static final Color POSSIBLE_DESTINATIONS_COLOR = new Color(152, 251, 152); // mint green
    private static final Color POSSIBLE_DESTINATIONS_TEXT_COLOR = new Color(34, 139, 34); // forest green
    private static final Color SELECTED_DESTINATION_COLOR = new Color(15, 255, 8); // neon green

    // Zoom settings
    private static final double MIN_ZOOM = 1.0;
    private static final double MAX_ZOOM = 10.0;
    private static final double ZOOM_STEP = 0.1; // Ridotto per trackpad più fluido

    // Scaling factors per i nodi durante lo zoom
    // I nodi scalano meno del background per rimanere leggibili
    private static final double NODE_SCALE_FACTOR = 0.5; // I nodi scalano al 50% rispetto allo zoom

    // Game State (Detective Mode)
    private static final int POSITION_NOT_SET = -1;

    private final MapInfo mapInfo;
    private BufferedImage backgroundImage;

    private double baseScaleX = 1.0;
    private double baseScaleY = 1.0;
    private double zoomLevel = 1.0;
    private double currentScaleX = 1.0;
    private double currentScaleY = 1.0;

    private int baseOffsetX = 0;
    private int baseOffsetY = 0;
    private int panOffsetX = 0;
    private int panOffsetY = 0;

    private int scaledBackgroundWidth = 0;
    private int scaledBackgroundHeight = 0;
    private boolean scaleCalculated = false;

    private final Map<Integer, Point2D> scaledNodePositions = new HashMap<>();

    // Pan dragging
    private Point dragStartPoint;
    private int dragStartPanX;
    private int dragStartPanY;

    // Game State (Detective mode)
    private int misterXPosition;
    private int detectivePosition;
    private List<Integer> bobbiesPositions;
    private Set<Integer> possibleDestinations;
    private int selectedDestination;

    GameView gameView;

    /**
     * Creates a new MapPanel with the given map info DTO.
     *
     * @param mapInfo the map info DTO to render
     * @throws NullPointerException if mapInfo is null
     */
    public MapPanel(final MapInfo mapInfo, final GameView view) {
        this.mapInfo = Objects.requireNonNull(mapInfo, "Map info cannot be null");
        this.gameView = view;
        this.detectivePosition = POSITION_NOT_SET;
        this.bobbiesPositions = new ArrayList<>();
        this.possibleDestinations = new HashSet<Integer>();
        this.selectedDestination = POSITION_NOT_SET;
        setupPanel();
    }

    private void setupPanel() {
        setOpaque(true);
        setBackground(BACKGROUND_COLOR);
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

        if (Math.abs(oldZoom - zoomLevel) > 0.001) {
            Point zoomCenter;
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

        if (Math.abs(zoomLevel - MIN_ZOOM) < 0.001) {
            zoomLevel = MIN_ZOOM;
            panOffsetX = 0;
            panOffsetY = 0;
        } else if (Math.abs(oldZoom - zoomLevel) > 0.001) {
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
        return zoomLevel > MIN_ZOOM + 0.001;
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
                System.err.println("Background image not found");
            }
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
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
        g2d.setColor(SHADOW_COLOR);
        g2d.fillOval(x - scaledRadius + 2, y - scaledRadius + 2, scaledRadius * 2, scaledRadius * 2);

        // Riempimento bianco
        g2d.setColor(NODE_FILL_COLOR);
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
        g2d.setColor(NODE_TEXT_COLOR);
        final String label = String.valueOf(node.getId());
        final FontMetrics fm = g2d.getFontMetrics();
        final int textWidth = fm.stringWidth(label);
        final int textHeight = fm.getAscent();
        g2d.drawString(label, x - textWidth / 2, y + textHeight / 2 - Math.max(2, (int) (2 * nodeZoom)));
    }

    private Color getTransportColor(final TransportType transport) {
        return switch (transport) {
            case TAXI -> new Color(255, 255, 85);
            case BUS -> new Color(58, 132, 36);
            case UNDERGROUND -> new Color(200, 43, 29);
            case FERRY -> new Color(0, 0, 0);
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

    /** Sets the detective position. */
    public void setDetectivePosition(int position) {
        this.detectivePosition = position;
    }

    /** Sets Mister X position. */
    public void setMisterXPosition(int position) {
        this.misterXPosition = position;
    }

    /** Initializes bobbies list of positions. */
    public void initializeBobbies(int numberOfBobbies){
        for(int i=0; i<numberOfBobbies; i++){
            this.bobbiesPositions.add(i, -1);
        }
    }

    /** Sets bobby position. */
    public void setBobbyPosition(int position, int indexBobby) {
        this.bobbiesPositions.set(indexBobby, position);
    }

    /** Loads the possible destinations for current player. */
    public void loadPossibleDestinations(Set<Integer> destinations) {
        this.possibleDestinations = destinations;
    }

    /** Sets the selected destination. */
    private void setSelectedDestination(int destination){
        this.selectedDestination = destination;
    }

    /**
     * Handles node click detection. Finds which node (if any) was clicked based on mouse coordinates.
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
                    for(Integer nodeIdPossibleDest : this.possibleDestinations){
                        if(nodeIdPossibleDest==node.getId()){
                            this.setSelectedDestination(node.getId());
                            this.gameView.movePlayer(node.getId());
                        }
                    }
                }
            }
        }
    }

    /** Draw the player given as input on the map. */
    private void drawPlayer(Graphics2D g2d, String playerString, int position, int scaledRadius) {
        if (position > 0) {
            final Point2D pos = scaledNodePositions.get(position);
            if (pos != null) {
                final int x = (int) pos.getX();
                final int y = (int) pos.getY();

                // Player circle
                if ("D".equals(playerString)) {
                    g2d.setColor(DETECTIVE_COLOR);
                }
                if ("X".equals(playerString)) {
                    g2d.setColor(MISTER_X_COLOR);
                }
                if (playerString.startsWith("B")) {
                    g2d.setColor(BOBBIES_COLOR);
                }
                g2d.fillOval(x - scaledRadius, y - scaledRadius, scaledRadius * 2, scaledRadius * 2);

                // Player text (white)
                g2d.setColor(Color.WHITE);
                g2d.setFont(PLAYERS_FONT);
                final FontMetrics fontMetrics = g2d.getFontMetrics();
                final String label = playerString;
                final int textWidth = fontMetrics.stringWidth(label);
                final int textHeight = fontMetrics.getAscent();
                g2d.drawString(label, x - textWidth / 2, y + textHeight / 2 - 2);
            }
        }
    }

    /** Draw the possible destinations. */
    private void drawDestinations(Graphics2D g2d, int scaledRadius, double nodeZoom) {
        for (Integer dest : this.possibleDestinations) {
            final Point2D pos = scaledNodePositions.get(dest);
            if (pos != null) {
                final int x = (int) pos.getX();
                final int y = (int) pos.getY();
                g2d.setColor(POSSIBLE_DESTINATIONS_COLOR);
                g2d.fillOval(x - scaledRadius, y - scaledRadius, scaledRadius * 2, scaledRadius * 2);
                g2d.setColor(POSSIBLE_DESTINATIONS_TEXT_COLOR);
                final String label = String.valueOf(dest);
                final FontMetrics fm = g2d.getFontMetrics();
                final int textWidth = fm.stringWidth(label);
                final int textHeight = fm.getAscent();
                g2d.drawString(label, x - textWidth / 2, y + textHeight / 2 - Math.max(2, (int) (2 * nodeZoom)));
            }
        }
    }

    /** Draw selected destination (the clicked one) */
    private void drawSelectedDestination(Graphics g2d, int scaledRadius, double nodeZoom){
        int dest = this.selectedDestination;
        final Point2D pos = scaledNodePositions.get(dest);
        if (pos != null) {
            final int x = (int) pos.getX();
            final int y = (int) pos.getY();
            g2d.setColor(SELECTED_DESTINATION_COLOR);
            g2d.fillOval(x - scaledRadius, y - scaledRadius, scaledRadius * 2, scaledRadius * 2);
            g2d.setColor(POSSIBLE_DESTINATIONS_TEXT_COLOR);
            final String label = String.valueOf(dest);
            final FontMetrics fm = g2d.getFontMetrics();
            final int textWidth = fm.stringWidth(label);
            final int textHeight = fm.getAscent();
            g2d.drawString(label, x - textWidth / 2, y + textHeight / 2 - Math.max(2, (int) (2 * nodeZoom)));
        }
    }

    private void drawGameStatus(final Graphics2D g2d) {
        final double nodeZoom = 1.0 + (zoomLevel - 1.0) * NODE_SCALE_FACTOR;
        final int scaledRadius = (int) (NODE_RADIUS * nodeZoom);

        this.drawDestinations(g2d, scaledRadius, nodeZoom);
        this.drawPlayer(g2d, "D", this.detectivePosition, scaledRadius);
        this.drawPlayer(g2d, "X", this.misterXPosition, scaledRadius);
        for (int i = 0; i < this.bobbiesPositions.size(); i++) {
            int bobbyIndex = i + 1;
            this.drawPlayer(g2d, "B" + bobbyIndex, this.bobbiesPositions.get(i), scaledRadius);
        }
        this.drawSelectedDestination(g2d, scaledRadius, nodeZoom);
    }
}
