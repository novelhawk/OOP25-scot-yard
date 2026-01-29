package it.unibo.scotyard.model.inventory;

import java.util.EnumMap;
import java.util.Map;

import it.unibo.scotyard.model.players.Bobby;
import it.unibo.scotyard.model.players.Detective;
import it.unibo.scotyard.model.players.MisterX;
import it.unibo.scotyard.model.players.Player;
import it.unibo.scotyard.model.players.TicketType;

public class InventoryImpl implements Inventory{

    protected static final int NONE = 0;
    protected static final int INFINITE = -1;
    private static final int NUMBER_TICKETS_TAXI = 11;
    private static final int NUMBER_TICKETS_BUS = 8;
    private static final int NUMBER_TIKCETS_UNDERGROUND = 4;
    private static final int NUMBER_TICKETS_BLACK = 5;
    private static final int NUMBER_TICKETS_DOUBLE_MOVE = 2;

    protected Map<TicketType, Integer> ticketsMap;

    public InventoryImpl(){
        this.ticketsMap = new EnumMap<>(TicketType.class);
    }

    @Override
    public void initialize(Player player) {
        if(player instanceof Detective){
            this.ticketsMap.put(TicketType.TAXI, NUMBER_TICKETS_TAXI);
            this.ticketsMap.put(TicketType.BUS, NUMBER_TICKETS_BUS);
            this.ticketsMap.put(TicketType.UNDERGROUND, NUMBER_TIKCETS_UNDERGROUND);
            this.ticketsMap.put(TicketType.BLACK, NONE);
            this.ticketsMap.put(TicketType.DOUBLE_MOVE, NONE);
        }
        if(player instanceof Bobby){
            this.ticketsMap.put(TicketType.TAXI, INFINITE);
            this.ticketsMap.put(TicketType.BUS, INFINITE);
            this.ticketsMap.put(TicketType.UNDERGROUND, INFINITE);
            this.ticketsMap.put(TicketType.BLACK, NONE);
            this.ticketsMap.put(TicketType.DOUBLE_MOVE, NONE);
        }
        if(player instanceof MisterX){
            this.ticketsMap.put(TicketType.TAXI, INFINITE);
            this.ticketsMap.put(TicketType.BUS, INFINITE);
            this.ticketsMap.put(TicketType.UNDERGROUND, INFINITE);
            this.ticketsMap.put(TicketType.BLACK, NUMBER_TICKETS_BLACK);
            this.ticketsMap.put(TicketType.DOUBLE_MOVE, NUMBER_TICKETS_DOUBLE_MOVE);
        }
    }

    @Override
    public int getNumberTickets(TicketType ticketType) {
        return this.ticketsMap.get(ticketType);
    }

    @Override
    public boolean containsTicket(TicketType ticketType){
        return this.ticketsMap.containsKey(ticketType) && (this.ticketsMap.get(ticketType)>0 || this.ticketsMap.get(ticketType)==INFINITE);
    }

    @Override
    public void decrementTickets(TicketType ticketType) {
        final int currentTickets = this.ticketsMap.get(ticketType);
        if(ticketType.equals(TicketType.BLACK)){
            System.out.println(currentTickets);
        }
        if(currentTickets!=INFINITE){
            this.ticketsMap.put(ticketType, currentTickets - 1);
        }
    }
    
}
