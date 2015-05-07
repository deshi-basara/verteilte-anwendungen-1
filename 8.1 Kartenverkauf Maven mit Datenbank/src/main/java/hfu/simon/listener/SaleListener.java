package hfu.simon.listener;

import hfu.simon.model.Sale;
import hfu.simon.model.SaleDB;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by simon on 22.04.15.
 */
@WebListener
public class SaleListener implements ServletContextListener {

     @Override
     public void contextInitialized(ServletContextEvent event) {
         // get init-parameters
         ServletContext sc = event.getServletContext();

         int ticketsAvailable = Integer.parseInt(sc.getInitParameter("tickets-available"));
         String databaseHost = sc.getInitParameter("database-host");
         int databasePort = Integer.parseInt(sc.getInitParameter("database-port"));
         String databaseName = sc.getInitParameter("database-name");
         String databaseUser = sc.getInitParameter("database-user");
         String databasePassword = sc.getInitParameter("database-password");

         // create sale-model and set it as context-attribute
         Sale saleModel = new Sale(ticketsAvailable);
         sc.setAttribute("saleModel", saleModel);

         // create a database connection
         SaleDB saleDBModel = new SaleDB(ticketsAvailable, databaseHost, databasePort,
                 databaseName, databaseUser, databasePassword);
         sc.setAttribute("saleDBModel", saleDBModel);
     }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // nothing to do here (yet)
    }

}
