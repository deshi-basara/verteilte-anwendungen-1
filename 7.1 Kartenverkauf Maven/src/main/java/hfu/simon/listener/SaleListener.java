package hfu.simon.listener;

import hfu.simon.model.Sale;

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
         int ticketsAvailable = Integer.parseInt(sc.getInitParameter("ticketsAvailable"));

         // create sale-model and set it as context-attribute
         Sale saleModel = new Sale(ticketsAvailable);
         sc.setAttribute("saleModel", saleModel);
     }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // nothing to do here (yet)
    }

}
