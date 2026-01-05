package it.unibo.scotyard.model.entities;

import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;

public record MoveAction(NodeId destination, TransportType transportType) {}
