package it.unibo.scotyard.model.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class MapNodeTest {

    @Test
    void testCreateNodeWithNodeId() {
        final NodeId nodeId = new NodeId(1);
        final MapNode node = new MapNode(nodeId, 100, 200);

        assertEquals(nodeId, node.getId());
        assertEquals(100, node.getX());
        assertEquals(200, node.getY());
    }

    @Test
    void testNodeEqualityBasedOnId() {
        final NodeId id = new NodeId(10);
        final MapNode node1 = new MapNode(id, 100, 200);
        final MapNode node2 = new MapNode(id, 300, 400);

        assertEquals(node1, node2);
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void testNodesWithDifferentIdsNotEqual() {
        final MapNode node1 = new MapNode(new NodeId(1), 100, 200);
        final MapNode node2 = new MapNode(new NodeId(2), 100, 200);

        assertNotEquals(node1, node2);
    }
}
