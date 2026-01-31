package it.unibo.scotyard.model.players;

import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.inventory.InventoryImpl;
import it.unibo.scotyard.model.map.NodeId;

public class Bobby extends AbstractPlayerImpl {
    /**
     * Creates a new AI Bobby player starting at the given position.
     *
     * @param position the starting position
     * @param brain    the AI brain
     */
    public Bobby(final NodeId position, final PlayerBrain brain) {
        super(position, brain);
        this.name = ViewConstants.BOBBY_STRING;
    }

    /**
     * Creates a new Bobby player starting at the given position.
     *
     * @param position the starting position
     */
    public Bobby(final NodeId position) {
        super(position);
        this.name = "Bobby";
    }

    public void initializeInventory(){
        this.inventory = new InventoryImpl() {
            public void initialize(){
                this.addTicket(TicketType.TAXI, MagicNumbers.NUMBER_TICKETS_TAXI);
                this.addTicket(TicketType.BUS, MagicNumbers.NUMBER_TICKETS_BUS);
                this.addTicket(TicketType.UNDERGROUND, MagicNumbers.NUMBER_TICKETS_UNDERGROUND);
                this.addTicket(TicketType.BLACK, MagicNumbers.NONE);
                this.addTicket(TicketType.DOUBLE_MOVE, MagicNumbers.NONE);
            }
        };
        this.inventory.initialize();
    }
}
