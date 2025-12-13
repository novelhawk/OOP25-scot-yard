package it.unibo.scotyard.model.command.round;

import it.unibo.scotyard.model.command.GameCommand;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.players.TicketType;

public record MoveCommand(NodeId targetNode, TicketType ticketType) implements GameCommand {}
