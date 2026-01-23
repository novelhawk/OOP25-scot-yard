package it.unibo.scotyard.model.entities;

import it.unibo.scotyard.model.map.NodeId;

public record ExposedPosition(NodeId position, int round) {}
