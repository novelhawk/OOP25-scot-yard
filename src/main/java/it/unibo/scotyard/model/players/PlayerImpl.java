package it.unibo.scotyard.model.players;

import java.util.Map;

import it.unibo.scotyard.model.map.MapNode;

public abstract class PlayerImpl implements Player{

    protected static final int NONE = 0;
    protected static final int INFINITE = -1;

    protected MapNode currentPosition;
    protected Map<TicketType,Integer> tickets;

    public PlayerImpl(){
        this.currentPosition = setInitialPosition();
        this.tickets = this.setInitialTickets();
    }

    @Override
    public abstract Map<TicketType, Integer> setInitialTickets();

    @Override
    public MapNode setInitialPosition() {
        return new MapNode(0, 0, 0);
        // TODO : generate random initial position 
        //throw new UnsupportedOperationException("Unimplemented method 'getInitialPosition'");
    }

    @Override
    public MapNode getCurrentPosition(){
        return this.currentPosition;
    }

    @Override
    public int getNumberTickets(TicketType ticketType){
        return this.tickets.get(ticketType);
    }

    @Override
    public boolean useTicket(TicketType ticket){
        if(this.tickets.containsKey(ticket) && this.tickets.get(ticket)>NONE){
            if(this.tickets.get(ticket)!=INFINITE){
                this.tickets.put(ticket, this.tickets.get(ticket)-1);
            }
            return true;
        }
        return false;
    }

    
}
