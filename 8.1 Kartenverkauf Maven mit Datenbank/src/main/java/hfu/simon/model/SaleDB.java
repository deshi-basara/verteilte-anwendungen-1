package hfu.simon.model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Handles sales in a persistent postgreSQL Database
 */
public class SaleDB {

    private Connection con = null;

    private String dbHost = "localhost";
    private int dbPort = 5432;
    private String dbName = "va_tickets";
    private String dbUser = "va_manager";
    private String dbPass = "verteilte";

    /**
     * Establishes a connection to the database
     */
    public SaleDB(int ticketsAvailable, String dbHost, int dbPort, String dbName,
                  String dbUser, String dbPass) {
        // set global attributes
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
            this.con = DriverManager
                    .getConnection("jdbc:postgresql://"+ this.dbHost +":"+ this.dbPort +"/" + this.dbName,
                            this.dbUser, this.dbPass);
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
