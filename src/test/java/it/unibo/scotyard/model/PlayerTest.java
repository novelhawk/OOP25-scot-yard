package it.unibo.scotyard.model;

import static org.junit.jupiter.api.Assertions.*;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.commons.patterns.CommonCostants;
import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.ai.SkipTurnBrain;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    final NodeId nodeDetective = new NodeId(3);
    final NodeId nodeMisterX = new NodeId(5);
    final NodeId nodeBobby = new NodeId(21);

    @Test
    public void testCreatePlayerWithNoBrain() {
        final Player detective = new Detective(nodeDetective);
        assertTrue(detective.isHuman());
        assertEquals(detective.getName(), CommonCostants.DETECTIVE_FULL_STRING);
        assertEquals(detective.getPosition(), nodeDetective);
        assertNotEquals(detective.getPosition(), nodeMisterX);
        assertNotEquals(detective.getPosition(), nodeBobby);

        final Player misterX = new MisterX(nodeMisterX);
        assertTrue(misterX.isHuman());
        assertEquals(misterX.getName(), CommonCostants.MRX_FULL_STRING);
        assertEquals(misterX.getPosition(), nodeMisterX);
        assertNotEquals(misterX.getPosition(), nodeDetective);
        assertNotEquals(misterX.getPosition(), nodeBobby);

        final Player bobby = new Bobby(nodeBobby);
        assertTrue(bobby.isHuman());
        assertEquals(bobby.getName(), CommonCostants.BOBBY_FULL_STRING);
        assertEquals(bobby.getPosition(), nodeBobby);
        assertNotEquals(bobby.getPosition(), nodeDetective);
        assertNotEquals(bobby.getPosition(), nodeMisterX);
    }

    @Test
    public void testCreatePlayerWithBrain() {
        final PlayerBrain brain = new SkipTurnBrain();

        final Player detective = new Detective(nodeDetective, brain);
        assertFalse(detective.isHuman());
        assertEquals(detective.getBrain(), Optional.ofNullable(brain));
        assertEquals(detective.getName(), CommonCostants.DETECTIVE_STRING);
        assertEquals(detective.getPosition(), nodeDetective);
        assertNotEquals(detective.getPosition(), nodeMisterX);
        assertNotEquals(detective.getPosition(), nodeBobby);

        final Player misterX = new MisterX(nodeMisterX);
        assertTrue(misterX.isHuman());
        assertEquals(misterX.getBrain(), Optional.empty());
        assertEquals(misterX.getName(), CommonCostants.MRX_STRING);
        assertEquals(misterX.getPosition(), nodeMisterX);
        assertNotEquals(misterX.getPosition(), nodeDetective);
        assertNotEquals(misterX.getPosition(), nodeBobby);

        final Player bobby = new Bobby(nodeBobby, brain);
        assertFalse(bobby.isHuman());
        assertEquals(bobby.getBrain(), Optional.ofNullable(brain));
        assertEquals(bobby.getName(), CommonCostants.BOBBY_STRING);
        assertEquals(bobby.getPosition(), nodeBobby);
        assertNotEquals(bobby.getPosition(), nodeDetective);
        assertNotEquals(bobby.getPosition(), nodeMisterX);
    }

    @Test
    public void testInitializeInventoryPlayer() {
        final Player player = new Detective(nodeDetective);
        assertEquals(player.getNumberTickets(TicketType.TAXI), Constants.NUMBER_TICKETS_TAXI);
        assertEquals(player.getNumberTickets(TicketType.BUS), Constants.NUMBER_TICKETS_BUS);
        assertEquals(player.getNumberTickets(TicketType.UNDERGROUND), Constants.NUMBER_TICKETS_UNDERGROUND);
        assertEquals(player.getNumberTickets(TicketType.BLACK), Constants.NONE);
        assertEquals(player.getNumberTickets(TicketType.DOUBLE_MOVE), Constants.NONE);
    }

    @Test
    public void testChangePositionPlayer() {
        final Player player = new Bobby(nodeBobby);
        assertEquals(player.getPosition(), nodeBobby);
        player.setPosition(nodeDetective);
        assertEquals(player.getPosition(), nodeDetective);
        assertNotEquals(player.getPosition(), nodeBobby);
    }

    @Test
    public void testChangeNamePlayer() {
        final Player player = new Bobby(nodeBobby);
        assertEquals(player.getName(), CommonCostants.BOBBY__STRING);
        player.setName(CommonCostants.BOBBIES_PAWN);
        assertEquals(player.getName(), CommonCostants.BOBBIES_PAWN);
        assertNotEquals(player.getName(), CommonCostants.BOBBY_STRING);
    }

    @Test
    public void testUseTicket() {
        final Player detective = new Detective(nodeDetective);
        // Finite number of tickets (taxi for Detective)
        assertEquals(detective.getNumberTickets(TicketType.TAXI), Constants.NUMBER_TICKETS_TAXI);
        assertTrue(detective.useTicket(TicketType.TAXI));
        assertEquals(detective.getNumberTickets(TicketType.TAXI), Constants.NUMBER_TICKETS_TAXI - 1);
        // No tickets (double move for Detective)
        assertFalse(detective.useTicket(TicketType.DOUBLE_MOVE));
        assertEquals(detective.getNumberTickets(TicketType.DOUBLE_MOVE), Constants.NONE);

        final Player misterX = new MisterX(nodeBobby);
        // Infinite tickets (taxi for Mister X)
        assertEquals(misterX.getNumberTickets(TicketType.TAXI), Constants.INFINITE);
        assertTrue(misterX.useTicket(TicketType.TAXI));
        assertEquals(misterX.getNumberTickets(TicketType.TAXI), Constants.INFINITE);
        // Finite number of tickets (double move for Mister X)
        assertEquals(misterX.getNumberTickets(TicketType.DOUBLE_MOVE), Constants.NUMBER_TICKETS_DOUBLE_MOVE);
        assertTrue(misterX.useTicket(TicketType.DOUBLE_MOVE));
        assertEquals(misterX.getNumberTickets(TicketType.DOUBLE_MOVE), Constants.NUMBER_TICKETS_DOUBLE_MOVE - 1);
    }

    // TODO : test per hasTransportModeTransport()
    /* Questo metodo può essere tolto : già useTicket() fa questo controllo
     */
}
