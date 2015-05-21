package hfu.simon.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles sales in a persistent postgreSQL Database
 */
public class SaleDB {

    private Connection connection = null;

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
        init(ticketsAvailable);
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
    }

    /**
     * Initializes all databases fields and inserts all seats (if they
     * do not exist already)
     */
    private void init(int ticketsAvailable) {
        String saleSql = "CREATE TABLE IF NOT EXISTS sale (" +
                            "ID SERIAL PRIMARY KEY NOT NULL," +
                            "enabled integer DEFAULT 1," +
                            "free integer DEFAULT 1," +
                            "booked integer DEFAULT 0," +
                            "sold integer DEFAULT 0," +
                            "owner varchar(50)" +
                        ")";

        // initialize the "sale"-Table
        try {
            // create a new statement and execute the sql-query
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(saleSql);
            statement.close();
        }
        catch(SQLException e) {
            e.printStackTrace();

            return;
        }

        System.out.println("sale-Table successfully initialized.");

        // insert all seats in the "sale"-Table
        if(ticketsAvailable > 0) {
            String ticketSql = "INSERT INTO sale (owner) VALUES ";
            // add all seats to the sql-query
            for(int i = 1; i <= ticketsAvailable; i++) {
                ticketSql += "('')";

                // add a comma, if there is another value available
                if(i != ticketsAvailable) {
                    ticketSql += ',';
                }
            }

            // initialize tickets in the "sale"-Table
            try {
                // create a new statement and execute the sql-query
                Statement statement = this.connection.createStatement();
                statement.executeUpdate(ticketSql);
                statement.close();
            }
            catch(SQLException e) {
                e.printStackTrace();

                return;
            }

            System.out.println("tickets successfully initialized in sale-Table.");
        }
    }
}
