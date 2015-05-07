package hfu.simon.controller;

import hfu.simon.helper.TimedTask;
import hfu.simon.model.Sale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by simon on 22.04.15.
 */
public class SaleController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get execution cmd
        String cmd = request.getParameter("cmd");

        // handle cmd
        handleCmd(cmd, request);

        // redirect user back
        RequestDispatcher resultView = request.getRequestDispatcher("index.jsp");
        resultView.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // do nothing
    }

    /**
     * Verifies which html-form was submitted and executes saleModel-operations
     * if the 'submitted'-cmd is available.
     * @param cmd
     * @param request
     */
    private void handleCmd(String cmd, HttpServletRequest request) {

        // get saleModel
        ServletContext sc = request.getServletContext();
        Sale saleModel = (Sale) sc.getAttribute("saleModel");

        // which cmd should be executed
        switch(cmd) {
            case "sell":
                try {
                    // get needed form values and sell
                    int sellIndex;
                    try {
                        sellIndex = Integer.parseInt(request.getParameter("index"));
                    } catch(NumberFormatException e) {
                        throw new RuntimeException("Please enter a seat number");
                    }
                    saleModel.sellTicket(--sellIndex);
                } catch(RuntimeException e) {
                    // error during model manipulation
                    request.setAttribute("error", e.getMessage());

                    return;
                }

                break;
            case "book":
                // booking still enabled?
                if(saleModel.isSaleEnabled()) {
                    try {
                        // get needed form values and book
                        int bookIndex;
                        String bookOwner;
                        try {
                            bookIndex = Integer.parseInt(request.getParameter("index"));
                            bookOwner = request.getParameter("owner").trim();

                            if(bookOwner.equals("")) {
                                throw new RuntimeException("Please enter a ticket-owner");
                            }
                        } catch(NumberFormatException e) {
                            throw new RuntimeException("Please enter a seat number");
                        }

                        saleModel.bookTicket(--bookIndex, bookOwner);
                    } catch(RuntimeException e) {
                        // error during model manipulation
                        request.setAttribute("error", e.getMessage());

                        return;
                    }
                }

                break;
            case "unbook":
                try {
                    // get needed form values and sell
                    int unbookIndex;
                    String unbookOwner;
                    try {
                        unbookIndex = Integer.parseInt(request.getParameter("index"));
                        unbookOwner = request.getParameter("owner");

                        if(unbookOwner.equals("")) {
                            throw new RuntimeException("Please enter a ticket-owner");
                        }
                    } catch(NumberFormatException e) {
                        throw new RuntimeException("Please enter a seat number");
                    }

                    saleModel.unbookTicket(--unbookIndex, unbookOwner);
                } catch(RuntimeException e) {
                    // error during model manipulation
                    request.setAttribute("error", e.getMessage());

                    return;
                }

                break;
            case "unsell":
                try {
                    int unsaleIndex;
                    try {
                        unsaleIndex = Integer.parseInt(request.getParameter("index"));
                    } catch(NumberFormatException e) {
                        throw new RuntimeException("Please enter a seat number");
                    }
                    saleModel.unsaleTicket(--unsaleIndex);
                } catch(RuntimeException e) {
                    // error during model manipulation
                    request.setAttribute("error", e.getMessage());

                    return;
                }

                break;
            case "unbookall":
                // reset and toggle active state
                saleModel.resetBookings();
                saleModel.toggleSaleEnabled();

                break;
            case "disablebooking":
                try {
                    String eventDate = request.getParameter("date");
                    String stopTime = request.getParameter("time");

                    if(eventDate.equals("") || stopTime.equals("")) {
                        throw new RuntimeException("Please enter a valid deactivation time");
                    }

                    // try to parse the date
                    Date date;
                    try {
                        // create a new date-format
                        SimpleDateFormat html5Date = new SimpleDateFormat("d MMMM, yyyy hh:mm", Locale.ENGLISH);
                        date = html5Date.parse(eventDate + ' ' + stopTime);
                    } catch(ParseException e) {
                        throw new RuntimeException("Coud not parse handed date and time");
                    }

                    // we have a valid date, check if the was already a timer defined for the model
                    TimedTask modelTask = saleModel.getTimedTask();
                    if(modelTask != null) {
                        // already a timer defined, cancel it first
                        modelTask.stopExecution();
                    }

                    // start new timer and set it in the model
                    TimedTask task = new TimedTask(date, saleModel);
                    saleModel.setTimedTask(task);

                } catch(RuntimeException e) {
                    request.setAttribute("error", e.getMessage());

                    return;
                }

                break;
            default:
                // not a valid 'cmd' handed, stop execution
                return;
        }

        // everything went well, show default success message on forward
        request.setAttribute("success", "success");
    }
}
