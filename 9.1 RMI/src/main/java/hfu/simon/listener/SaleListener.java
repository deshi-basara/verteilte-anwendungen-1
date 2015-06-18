package hfu.simon.listener;

import hfu.simon.model.SaleDB;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by simon on 22.04.15.
 */
@WebListener
public class SaleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
         final int REGISTRY_PORT = 1099;

         // get init-parameters
         ServletContext sc = event.getServletContext();

         int ticketsAvailable = Integer.parseInt(sc.getInitParameter("tickets-available"));
         String databaseHost = sc.getInitParameter("database-host");
         int databasePort = Integer.parseInt(sc.getInitParameter("database-port"));
         String databaseName = sc.getInitParameter("database-name");
         String databaseUser = sc.getInitParameter("database-user");
         String databasePassword = sc.getInitParameter("database-password");
         boolean useHeroku = Boolean.valueOf(sc.getInitParameter("use-heroku"));

         // create a database connection and remote-object
        SaleDB saleDBModel;
         try {
             saleDBModel = new SaleDB(ticketsAvailable, databaseHost, databasePort,
                     databaseName, databaseUser, databasePassword, useHeroku);
             sc.setAttribute("saleModel", saleDBModel);
         } catch(RemoteException e) {
             e.printStackTrace();

             return;
         }

        // register the remote-object
        try {
            LocateRegistry.createRegistry(REGISTRY_PORT);

            Registry registry = LocateRegistry.getRegistry(REGISTRY_PORT);
            registry.rebind("saleModel", saleDBModel);
        } catch(RemoteException e) {
            e.printStackTrace();

            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // nothing to do here (yet)
    }

}
