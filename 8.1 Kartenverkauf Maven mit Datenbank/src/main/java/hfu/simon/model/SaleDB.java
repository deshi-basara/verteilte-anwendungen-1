package hfu.simon.model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Handles sales in a persistent postgreSQL Database
 */
public class SaleDB {

    private Connection con = null;
    private final String dbUser = "va_manager";
    private final String dbPass = "verteilte";
    private final String dbHost = "localhost";
    private final int dbPort = 5432;
    private final String dbName = "va_tickets";

    /**
     * Establishes a connection to the database
     */
    public SaleDB() {
        connect();
    }

    /**
     * Tries to establish a connection to the database.
     */
    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager
                    .getConnection("jdbc:postgresql://"+ this.dbHost +":"+ this.dbPort +"/" + this.dbName,
                            this.dbUser, this.dbPass);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
