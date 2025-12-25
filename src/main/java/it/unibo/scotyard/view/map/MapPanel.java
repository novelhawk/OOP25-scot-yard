package it.unibo.scotyard.view.map;

import it.unibo.scotyard.commons.dtos.map.MapInfo;
import it.unibo.scotyard.commons.dtos.map.Node;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.awt.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public final class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int NODE_RADIUS = 14;
    private static final int NODE_LABEL_SIZE = 10;
    private static final Font NODE_FONT = new Font("Arial", Font.BOLD, NODE_LABEL_SIZE);

    // Dimensioni originali del background
    private static final int ORIGINAL_BACKGROUND_WIDTH = 2570;
    private static final int ORIGINAL_BACKGROUND_HEIGHT = 1926;

    private static final Color BACKGROUND_COLOR = new Color(170, 170, 170);
    private static final Color NODE_FILL_COLOR = new Color(255, 255, 255);
    private static final Color NODE_TEXT_COLOR = new Color(33, 33, 33);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);

    // Zoom settings
    private static final double MIN_ZOOM = 1.0;
    private static final double MAX_ZOOM = 10.0;
    private static final double ZOOM_STEP = 0.1; // Ridotto per trackpad più fluido

    // Scaling factors per i nodi durante lo zoom
    // I nodi scalano meno del background per rimanere leggibili
    private static final double NODE_SCALE_FACTOR = 0.5; // I nodi scalano al 50% rispetto allo zoom

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

    /**
     * Creates a new MapPanel with the given map info DTO.
     *
     * @param mapInfo the map info DTO to render
     * @throws NullPointerException if mapInfo is null
     */
    public MapPanel(final MapInfo mapInfo) {
        this.mapInfo = Objects.requireNonNull(mapInfo, "Map info cannot be null");
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
        final String label = String.valueOf(node.getId().id());
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
}
