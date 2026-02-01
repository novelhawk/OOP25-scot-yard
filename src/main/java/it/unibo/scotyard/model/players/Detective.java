package it.unibo.scotyard.model.players;

import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.commons.patterns.ViewConstants;
import it.unibo.scotyard.model.ai.PlayerBrain;
import it.unibo.scotyard.model.inventory.InventoryImpl;
import it.unibo.scotyard.model.map.NodeId;

public class Detective extends AbstractPlayerImpl {

    /**
     * Creates a new AI Detective player starting at the given position.
     *
     * @param position the starting position
     * @param brain    the AI brain
     */
    public Detective(final NodeId position, final PlayerBrain brain) {
        super(position, brain);
        this.name = ViewConstants.DETECTIVE_STRING;
    }

    /**
     * Creates a new Detective player starting at the given position.
     *
     * @param position the starting position
     */
    public Detective(final NodeId position) {
        super(position);
        this.name = ViewConstants.DETECTIVE_STRING;
    }

    public void initializeInventory() {
        this.inventory = new InventoryImpl() {
            public int getInitialTickets(TicketType ticket) {
                switch (ticket) {
                    case TAXI:
                        return MagicNumbers.NUMBER_TICKETS_TAXI;
                    case BUS:
                        return MagicNumbers.NUMBER_TICKETS_BUS;
                    case UNDERGROUND:
                        return MagicNumbers.NUMBER_TICKETS_UNDERGROUND;
                    case BLACK:
                    case DOUBLE_MOVE:
                    default:
                        return MagicNumbers.NONE;
                }
            }
        };
        this.inventory.initialize();
    }
}
