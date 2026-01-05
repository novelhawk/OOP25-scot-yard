package it.unibo.scotyard.model.command.turn;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;

public record MoveCommand(NodeId targetNode, TransportType transportType) implements GameCommand {}
