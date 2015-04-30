package hfu.simon.controller;

import hfu.simon.model.Sale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.IllegalFormatException;

/**
 * Created by simon on 22.04.15.
 */
//@WebServlet(name = "SaleController")
public class SaleController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get execution cmd
        String cmd = request.getParameter("cmd");

        // handle cmd
        handleCmd(cmd, request);

        // redirect user back
        RequestDispatcher resultView = request.getRequestDispatcher("index.jsp");
        resultView.forward(request, response);
    }

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

        // is the sale still enabled?
        if(saleModel.isSaleEnabled() || cmd.equals("unbookall")) {

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
            }

            request.setAttribute("success", "success");
        }
    }
}
