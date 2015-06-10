package hfu.simon.model;

import hfu.simon.helper.TimedTask;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

/**
 * Handles sales in a persistent postgreSQL Database
 */
public class SaleDB {

    /**
     * Database
     */
    private Connection connection = null;
    private String dbHost = "localhost";
    private int dbPort = 5432;
    private String dbName = "va_tickets";
    private String dbUser = "va_manager";
    private String dbPass = "verteilte";

    /**
     * Attributes
     */
    private int ticketCount;
    private PGPoolingDataSource dataSource = new PGPoolingDataSource();
    private boolean saleEnabled = true;
    private TimedTask task = null;

    /**
     * Establishes a connection to the database
     */
    public SaleDB(int ticketsAvailable, String dbHost, int dbPort, String dbName,
                  String dbUser, String dbPass) {
        // set global attributes
        this.ticketCount = ticketsAvailable;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;

        connect();
    }

    /**
     * Tries to establish a connection to the database.
     */
    private void connect() {
        System.out.println("Connection to postgreSQL: " + this.dbHost +":"+ this.dbPort +"/" + this.dbName);
        try {
            // returns the Class object associated with the class or interface with the given string name
            Class.forName("org.postgresql.Driver");
            // connect
            this.connection = DriverManager
                    .getConnection("jdbc:postgresql://"+ this.dbHost +":"+ this.dbPort +"/" + this.dbName,
                            this.dbUser, this.dbPass);
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        init();
    }

    /**
     * Initializes the database with default values, if it wasn't initialized previously.
     */
    private void init() {
        // check if tickets-table is empty
        String initSql = "SELECT * FROM tickets";
        try {
            PreparedStatement initStatement = this.connection.prepareStatement(initSql);
            ResultSet results = initStatement.executeQuery();

            // results already has values, skip initialization
            if(results.next()) {
                return;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        // initialize tickets-table
        String insertSql = "INSERT INTO tickets (id) values ";
        for(int i = 0; i < this.ticketCount; i++) {
            insertSql += "("+ i +")";

            // not last value?
            if((i+1) != this.ticketCount) {
                insertSql += ",";
            }
        }
        try {
            PreparedStatement insertStatement = this.connection.prepareStatement(insertSql);
            insertStatement.executeQuery();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Initialized database successfully");
    }

    /**
     * Tries to mark a ticket as sold, after verifying, that the ticket is
     * still available.
     * @param index
     * @throws RuntimeException
     */
    public void sellTicket(int index) throws RuntimeException {
        String sellSql = "SELECT * FROM tickets WHERE id=" + String.valueOf(index);
        System.out.println(sellSql);
        Statement sellStatement = null;
        try {
            sellStatement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet results = sellStatement.executeQuery(sellSql);

            // was there a result?
            if(results.next()) {
                int isFree = results.getInt("free");
                int isBooked = results.getInt("booked");
                int isSold = results.getInt("sold");

                // still free?
                if(isFree == 1 && isSold == 0 && isBooked == 0) {
                    results.updateInt("free", 0);
                    results.updateInt("sold", 1);
                    // update row
                    results.updateRow();
                }
            }
            else {
                throw new RuntimeException("Kein gültiger Sitzplatz");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(sellStatement != null) {
                try {
                    sellStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
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
        String bookSql = "SELECT * FROM tickets WHERE id=" + String.valueOf(index);
        Statement bookStatement = null;
        try {
            bookStatement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet results = bookStatement.executeQuery(bookSql);

            // was there a result?
            if(results.next()) {
                int isFree = results.getInt("free");
                int isBooked = results.getInt("booked");
                int isSold = results.getInt("sold");

                // still free?
                if(isFree == 1 && isSold == 0 && isBooked == 0) {
                    results.updateInt("free", 0);
                    results.updateInt("booked", 1);
                    results.updateString("owner", owner);
                    // update row
                    results.updateRow();
                }
            }
            else {
                throw new RuntimeException("Kein gültiger Sitzplatz");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(bookStatement != null) {
                try {
                    bookStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sells a ticket back, if the request was started by the ticket-owner.
     * @param index
     * @param owner
     * @throws RuntimeException
     */
    public void unbookTicket(int index, String owner) throws RuntimeException {
        String unbookSql = "SELECT * FROM tickets WHERE id=" + String.valueOf(index);
        Statement bookStatement = null;
        try {
            bookStatement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet results = bookStatement.executeQuery(unbookSql);

            // was there a result?
            if(results.next()) {
                String currentOwner = results.getString("owner");

                // is owner?
                if(currentOwner.equals(owner)) {
                    // is owner, mark as free again
                    results.updateInt("free", 1);
                    results.updateInt("booked", 0);
                    results.updateInt("sold", 0);
                    results.updateString("owner", "");
                    // update row
                    results.updateRow();
                }
                else {
                    // now the owner, throw exception
                    throw new RuntimeException("Du bist nicht der Ticket-Besitzer");
                }
            }
            else {
                throw new RuntimeException("Kein gültiger Sitzplatz");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(bookStatement != null) {
                try {
                    bookStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Resets a ticket to its default values.
     * @param index
     */
    public void unsaleTicket(int index) {
        String unsaleSql = "SELECT * FROM tickets WHERE id=" + String.valueOf(index);
        System.out.println(unsaleSql);
        Statement bookStatement = null;
        try {
            bookStatement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet results = bookStatement.executeQuery(unsaleSql);

            // was there a result?
            if(results.next()) {
                // reset ticket
                results.updateInt("free", 1);
                results.updateInt("booked", 0);
                results.updateInt("sold", 0);
                results.updateString("owner", "");
                // update row
                results.updateRow();
            }
            else {
                throw new RuntimeException("Kein gültiger Sitzplatz");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(bookStatement != null) {
                try {
                    bookStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Resets all booked tickets to unbooked.
     */
    public void resetBookings() {
        String unsaleSql = "SELECT * FROM tickets";
        Statement bookStatement = null;
        try {
            bookStatement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet results = bookStatement.executeQuery(unsaleSql);

            // loop through all results
            while(results.next()) {
                // reset ticket
                results.updateInt("free", 1);
                results.updateInt("booked", 0);
                results.updateInt("sold", 0);
                results.updateString("owner", "");
                // update row
                results.updateRow();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(bookStatement != null) {
                try {
                    bookStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Toggles the 'saleEnabled'-state.
     */
    public void toggleSaleEnabled() {
        this.saleEnabled = !this.saleEnabled;
    }

    /**
     * Disables the 'saleEnabled'-state (for timed tasks)
     */
    public void disableSaleEnabled() {
        this.saleEnabled = false;
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
        return this.ticketCount;
    }

    /**
     * Returns the TimedTask of this model.
     * @return task
     */
    public TimedTask getTimedTask() {
        return this.task;
    }

    /**
     * Fetches all tickets from the database, converts them into ticket-objects and returns the tickets-vector.
     * @return
     */
    public Vector<Ticket> getAllTickets() {
        Vector<Ticket> tickets = new Vector(this.ticketCount);

        // get all tickets
        String allSql = "SELECT * FROM tickets ORDER BY id ASC";
        Statement allStatement = null;
        try {
            allStatement = this.connection.createStatement();
            ResultSet results = allStatement.executeQuery(allSql);

            // loop through all results
            int pointer = 0;
            while(results.next()) {
                // get all needed values
                int index = results.getInt("id");
                int isEnabled = results.getInt("enabled");
                int isFree = results.getInt("free");
                int isBooked = results.getInt("booked");
                int isSold = results.getInt("sold");
                String owner = results.getString("owner");

                // create a new Ticket-model
                Ticket blueprint = new Ticket(index, isEnabled, isFree, isBooked, isSold, owner);
                //blueprint.printTicket();

                tickets.add(pointer, blueprint);
                pointer++;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(allStatement != null) {
                try {
                    allStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // reverse all vector elements to correct the wrong item order from the ResultSet
        //Collections.reverse(tickets);

        return tickets;
    }


}
