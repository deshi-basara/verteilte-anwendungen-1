package hfu.simon.model;

/**
 * Created by simon on 21.04.15.
 */
public class Ticket {

    /**
     * Attributes
     */
    private int enabled = 1;
    private int free = 1;
    private int booked = 0;
    private int sold = 0;
    private String owner = "";
    private int index = -1;

    /**
     * Ticket-constructor.
     * Sets the index of the ticket.
     * @param index
     */
    public Ticket(int index) {
        this.index = index;
    }

    /**
     * Sets a ticket-owner.
     * @param name
     */
    public void setOwner(String name) {
        this.owner = name;
    }

    /**
     * Sets if the ticket is enabled or not.
     */
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets if the ticket is still available or not.
     * @param free
     */
    public void setFree(int free) {
        this.free = free;
    }

    /**
     * Sets if the ticket is already booked or not.
     * @param booked
     */
    public void setBooked(int booked) {
        this.booked = booked;
    }

    /**
     * Sets if the ticket is already sold or not.
     * @param sold
     */
    public void setSold(int sold) {
        this.sold = sold;
    }

    /**
     * Returns if the ticket is enabled or not.
     * @return
     */
    public int getEnabled() {
        return this.enabled;
    }

    /**
     * Returns if the ticket is still available or not.
     * @return
     */
    public int getFree() {
        return this.free;
    }

    /**
     * Returns if the ticket is already booked or not.
     * @return
     */
    public int getBooked() {
        return this.booked;
    }

    /**
     * Returns if the ticket is already sold or not.
     * @return
     */
    public int getSold() {
        return this.sold;
    }

    /**
     * Returns the index of the ticket.
     * @return
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the ticket-owner's name.
     * @return
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Checks if a handed name is the ticket-owner.
     * @param name
     * @return
     */
    public boolean isOwner(String name) {
        return this.owner.equals(name);
    }

    /**
     * Resets the ticket to its default values.
     */
    public void resetTicket() {
        this.enabled = 1;
        this.free = 1;
        this.booked = 0;
        this.sold = 0;
        this.owner = "";
    }

    /**
     * Resets the booking status of ticket.
     */
    public void resetBooking() {
        this.booked = 0;
        this.free = 1;
        this.owner = "";
    }

    /**
     * Prints the ticket's model data (for debugging).
     */
    public void printTicket() {
        System.out.println("==============================");
        System.out.println("Ticket Number: " + this.index);
        System.out.println("Enabled: " + this.enabled);
        System.out.println("Free: " + this.free);
        System.out.println("Booked: " + this.booked);
        System.out.println("Sold: " + this.sold);
        System.out.println("Owner: " + this.owner);
        System.out.println("==============================");
    }

}
