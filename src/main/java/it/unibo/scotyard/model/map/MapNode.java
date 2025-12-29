package it.unibo.scotyard.model.map;

import java.util.Objects;

/** node (location) on the game map. unique identifier and x, y coordinates for rendering. */
public class MapNode {
    private final NodeId id;
    private final int x;
    private final int y;

    /**
     * Creates a new map node with the specified coordinates.
     *
     * @param id the unique identifier for this node
     * @param x the x-coordinate for rendering
     * @param y the y-coordinate for rendering
     */
    public MapNode(final NodeId id, final int x, final int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the unique identifier of this node.
     *
     * @return the node ID
     */
    public NodeId getId() {
        return id;
    }

    /**
     * Returns the x-coordinate of this node.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this node.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MapNode mapNode = (MapNode) o;
        return id == mapNode.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MapNode{id=" + id + ", x=" + x + ", y=" + y + '}';
    }
}
