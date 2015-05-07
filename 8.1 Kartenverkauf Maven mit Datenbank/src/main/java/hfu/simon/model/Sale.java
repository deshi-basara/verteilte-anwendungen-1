package hfu.simon.model;

import hfu.simon.helper.TimedTask;

import java.util.Vector;

/**
 * Created by simon on 21.04.15.
 */
public class Sale {

    private boolean debug = true;

    /**
     * Attributes
     */
    private int ticketsAvailable = 0;
    private Vector<Ticket> tickets = null;
    private boolean saleEnabled = true;
    private TimedTask task = null;

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
    public synchronized void sellTicket(int index) throws RuntimeException {
        // get the ticket, if it exists
        Ticket ticket;
        try {
            ticket = this.tickets.get(index);
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Ticket not available");
        }

        // still free, not sold and not booked?
        if(ticket.getFree() == 1 && ticket.getSold() == 0 && ticket.getBooked() == 0) {
            // ticket still free, sell it
            ticket.setFree(0);
            ticket.setSold(1);
        }
        else {
            // ticket already sold/booked, throw exception
            throw new RuntimeException("Ticket not available for sale");
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
    public synchronized void bookTicket(int index, String owner) throws RuntimeException {
        // get the ticket, if it exists
        Ticket ticket;
        try {
            ticket = this.tickets.get(index);
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Ticket not available");
        }

        // still free, not sold and not booked?
        if(ticket.getFree() == 1 && ticket.getSold() == 0 && ticket.getBooked() == 0) {
            // ticket still free, book it
            ticket.setFree(0);
            ticket.setBooked(1);
            ticket.setOwner(owner);
        }
        else {
            // ticket already booked, throw exception
            throw new RuntimeException("Ticket not available for booking");
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
    public synchronized void unbookTicket(int index, String owner) throws RuntimeException {
        // get the ticket, if it exists
        Ticket ticket;
        try {
            ticket = this.tickets.get(index);
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Ticket not available");
        }

        // is the current user the real owner of the ticket?
        if(ticket.isOwner(owner)) {
            // is owner, mark as free again
            ticket.setBooked(0);
            ticket.setFree(1);
            ticket.setSold(0);
        }
        else {
            // now the owner, throw exception
            throw new RuntimeException("You are not the ticket-owner");
        }

        if(debug) {
            ticket.printTicket();
        }
    }

    /**
     * Resets a ticket to its default values.
     * @param index
     */
    public synchronized void unsaleTicket(int index) {
        // get the ticket, if it exists
        Ticket ticket;
        try {
            ticket = this.tickets.get(index);
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Ticket not available");
        }

        // reset all ticket values
        ticket.resetTicket();

        if(debug) {
            ticket.printTicket();
        }
    }

    /**
     * Resets all booked tickets to unbooked.
     */
    public synchronized void resetBookings() {
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
     * Toggles the 'saleEnabled'-state.
     */
    public synchronized void toggleSaleEnabled() {
        this.saleEnabled = !this.saleEnabled;

        if(debug) {
            System.out.println("Sale is enabled: " + this.saleEnabled);
        }
    }

    /**
     * Disables the 'saleEnabled'-state (for timed tasks)
     */
    public synchronized void disableSaleEnabled() {
        this.saleEnabled = false;

        if(debug) {
            System.out.println("Sale is enabled: " + this.saleEnabled);
        }
    }

    /**
     * If a TimedTask was associated to the model, it will be saved
     * as an attribute for later manipulation.
     */
    public synchronized void setTimedTask(TimedTask task) {
        this.task = task;
    }

    /**
     * Returns the 'saleEnabled'-state for disabling/enabling
     * model-calls.
     * @return
     */
    public boolean isSaleEnabled() {
        return this.saleEnabled;
    }

    /**
     * Returns the ticketCount.
     * @return
     */
    public int getTicketCount() {
        return this.ticketsAvailable;
    }

    /**
     * Returns the TimedTask of this model.
     * @return task
     */
    public TimedTask getTimedTask() {
        return this.task;
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
