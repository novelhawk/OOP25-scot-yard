package it.unibo.scotyard.model;

import static org.junit.jupiter.api.Assertions.*;

import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.model.inventory.Inventory;
import it.unibo.scotyard.model.inventory.InventoryImpl;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.TicketType;
import org.junit.jupiter.api.Test;

public class InventoryTest {
    private final NodeId node = new NodeId(3);

    @Test
    public void testInitializeDetective() {
        final Inventory inventory = new InventoryImpl();
        inventory.initialize(new Detective(node));
        assertEquals(inventory.getNumberTickets(TicketType.TAXI), MagicNumbers.NUMBER_TICKETS_TAXI);
        assertEquals(inventory.getNumberTickets(TicketType.BUS), MagicNumbers.NUMBER_TICKETS_BUS);
        assertEquals(inventory.getNumberTickets(TicketType.UNDERGROUND), MagicNumbers.NUMBER_TICKETS_UNDERGROUND);
        assertEquals(inventory.getNumberTickets(TicketType.BLACK), MagicNumbers.NONE);
        assertEquals(inventory.getNumberTickets(TicketType.DOUBLE_MOVE), MagicNumbers.NONE);
    }

    @Test
    public void testInitializeBobby() {
        final Inventory inventory = new InventoryImpl();
        inventory.initialize(new Bobby(node));
        assertEquals(inventory.getNumberTickets(TicketType.TAXI), MagicNumbers.INFINITE);
        assertEquals(inventory.getNumberTickets(TicketType.BUS), MagicNumbers.INFINITE);
        assertEquals(inventory.getNumberTickets(TicketType.UNDERGROUND), MagicNumbers.INFINITE);
        assertEquals(inventory.getNumberTickets(TicketType.BLACK), MagicNumbers.NONE);
        assertEquals(inventory.getNumberTickets(TicketType.DOUBLE_MOVE), MagicNumbers.NONE);
    }

    @Test
    public void testInitializeMisterX() {
        final Inventory inventory = new InventoryImpl();
        inventory.initialize(new MisterX(node));
        assertEquals(inventory.getNumberTickets(TicketType.TAXI), MagicNumbers.INFINITE);
        assertEquals(inventory.getNumberTickets(TicketType.BUS), MagicNumbers.INFINITE);
        assertEquals(inventory.getNumberTickets(TicketType.UNDERGROUND), MagicNumbers.INFINITE);
        assertEquals(inventory.getNumberTickets(TicketType.BLACK), MagicNumbers.NUMBER_TICKETS_BLACK);
        assertEquals(inventory.getNumberTickets(TicketType.DOUBLE_MOVE), MagicNumbers.NUMBER_TICKETS_DOUBLE_MOVE);
    }

    @Test
    public void testDecrementTickets() {
        final Inventory inventory = new InventoryImpl();
        inventory.initialize(new Detective(node));
        inventory.decrementTickets(TicketType.TAXI);
        assertEquals(inventory.getNumberTickets(TicketType.TAXI), MagicNumbers.NUMBER_TICKETS_TAXI - 1);
        inventory.decrementTickets(TicketType.BUS);
        inventory.decrementTickets(TicketType.UNDERGROUND);
        assertEquals(inventory.getNumberTickets(TicketType.BUS), MagicNumbers.NUMBER_TICKETS_BUS - 1);
        assertEquals(inventory.getNumberTickets(TicketType.UNDERGROUND), MagicNumbers.NUMBER_TICKETS_UNDERGROUND - 1);
        inventory.decrementTickets(TicketType.BLACK);
        assertEquals(inventory.getNumberTickets(TicketType.BLACK), MagicNumbers.NONE);
        inventory.decrementTickets(TicketType.DOUBLE_MOVE);
        assertEquals(inventory.getNumberTickets(TicketType.DOUBLE_MOVE), MagicNumbers.NONE);
        inventory.initialize(new MisterX(node));
        inventory.decrementTickets(TicketType.TAXI);
        assertEquals(inventory.getNumberTickets(TicketType.TAXI), MagicNumbers.INFINITE);
    }

    @Test
    public void testContainsTicket() {
        final Inventory inventory = new InventoryImpl();
        inventory.initialize(new MisterX(node));
        assertTrue(inventory.containsTicket(TicketType.TAXI));
        assertTrue(inventory.containsTicket(TicketType.BUS));
        assertTrue(inventory.containsTicket(TicketType.UNDERGROUND));
        assertTrue(inventory.containsTicket(TicketType.BLACK));
        assertTrue(inventory.containsTicket(TicketType.DOUBLE_MOVE));
        for (int i = 0; i < MagicNumbers.NUMBER_TICKETS_DOUBLE_MOVE; i++) {
            inventory.decrementTickets(TicketType.DOUBLE_MOVE);
        }
        assertFalse(inventory.containsTicket(TicketType.DOUBLE_MOVE));
        for (int i = 0; i < MagicNumbers.NUMBER_TICKETS_BLACK; i++) {
            inventory.decrementTickets(TicketType.BLACK);
        }
        assertFalse(inventory.containsTicket(TicketType.BLACK));
    }
}
