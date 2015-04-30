package hfu.simon.model;

import java.util.Vector;

/**
 * Created by simon on 21.04.15.
 */
public class Sale {

    /**
     * Attributes
     */
    private int ticketsAvailable = 0;
    private Vector<Ticket> tickets = null;
    private boolean debug = true;

    /**
     * Sale-constructor.
     * Creates a ticket-blueprint, a tickets-vector and inserts all
     * ticket-blueprints in the tickets-vector.
     */
    public Sale(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;

        System.out.println("Creating " + this.ticketsAvailable + " tickets in Sale-Model.");

        // create vector, which holds all tickets
        this.tickets = new Vector(this.ticketsAvailable);

        // foreach available ticket, create a ticket-object and add it to our ticket-vector
        for(int i = 0; i < this.ticketsAvailable; i++) {
            Ticket blueprint = new Ticket(i);

            this.addTicket(i, blueprint);
        }
    }

    /**
     * Returns the tickets-vector.
     * @return
     */
    public Vector<Ticket> getAllTickets() {
        return this.tickets;
    }

    /**
     * Tries to mark a ticket as sold, after verifying, that the ticket is
     * still available.
     * @param index
     * @throws RuntimeException
     */
    public void sellTicket(int index) throws RuntimeException {
        // get the ticket, if it exists
        Ticket ticket = this.tickets.get(index);

        // still free, not sold and not booked?
        if(ticket.getFree() == 1 && ticket.getSold() == 0 && ticket.getBooked() == 0) {
            // ticket still free, sell it
            ticket.setFree(0);
            ticket.setSold(1);
        }
        else {
            // ticket already sold, throw exception
            throw new RuntimeException("Ticket already sold");
        }

        if(debug) {
            ticket.printTicket();
        }
    }

    /**
     * Tries to mark a ticket as booked, after verifying that the ticket is
     * still available for booking.
     * @param index
     * @param owner
     * @throws RuntimeException
     */
    public void bookTicket(int index, String owner) throws RuntimeException {
        // get the ticket, if it exists
        Ticket ticket = this.tickets.get(index);

        // still free, not sold and not booked?
        if(ticket.getFree() == 1 && ticket.getSold() == 0 && ticket.getBooked() == 0) {
            // ticket still free, book it
            ticket.setFree(0);
            ticket.setBooked(1);
            ticket.setOwner(owner);
        }
        else {
            // ticket already booked, throw exception
            throw new RuntimeException("Ticket already booked");
        }

        if(debug) {
            ticket.printTicket();
        }
    }

    /**
     * Sells a ticket back, if the request was started by the ticket-owner.
     * @param index
     * @param owner
     * @throws RuntimeException
     */
    public void unbookTicket(int index, String owner) throws RuntimeException {
        // get the ticket, if it exists
        Ticket ticket = this.tickets.get(index);

        // is the current user the real owner of the ticket?
        if(ticket.isOwner(owner)) {
            // is owner, mark as free again
            ticket.setBooked(0);
            ticket.setFree(1);
            ticket.setSold(0);
        }
        else {
            // now the owner, throw exception
            throw new RuntimeException("Not the ticket-owner");
        }

        if(debug) {
            ticket.printTicket();
        }
    }

    /**
     * Resets a ticket to its default values.
     * @param index
     */
    public void unsaleTicket(int index) {
        // get the ticket, if it exists
        Ticket ticket = this.tickets.get(index);

        // reset all ticket values
        ticket.resetTicket();

        if(debug) {
            ticket.printTicket();
        }
    }

    /**
     * Resets all booked tickets to unbooked.
     */
    public void resetBookings() {
        // loop through all tickets and check if they are booked
        for(Ticket currentTicket : this.tickets) {

            // booked?
            if(currentTicket.getBooked() == 1) {
                // remove booking-status
                currentTicket.resetBooking();
            }

            if(debug) {
                currentTicket.printTicket();
            }
        }
    }

    /**
     * Returns the ticketCount.
     * @return
     */
    public int getTicketCount() {
        return this.ticketsAvailable;
    }

    /**
     * Adds a new ticket to the ticket-vector.
     * @param index
     * @param ticketBlueprint
     */
    private void addTicket(int index, Ticket ticketBlueprint) {
        this.tickets.add(index, ticketBlueprint);
    }

}
