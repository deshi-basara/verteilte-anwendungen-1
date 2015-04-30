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
        handleCmd(cmd, request, response);

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
     * @param response
     */
    private void handleCmd(String cmd, HttpServletRequest request, HttpServletResponse response) {

        // get saleModel
        ServletContext sc = request.getServletContext();
        Sale saleModel = (Sale) sc.getAttribute("saleModel");

        // which cmd should be executed
        switch(cmd) {
            case "book":
                // get needed form values and book
                int bookIndex = Integer.parseInt(request.getParameter("index"));
                String bookOwner = request.getParameter("owner");
                saleModel.bookTicket(--bookIndex, bookOwner);

                break;
            case "sell":
                // get needed form values and sell
                int sellIndex = Integer.parseInt(request.getParameter("index"));
                saleModel.sellTicket(--sellIndex);

                break;
            case "unbook":
                // get needed form values and sell
                int unbookIndex = Integer.parseInt(request.getParameter("index"));
                String unbookOwner = request.getParameter("owner");
                saleModel.unbookTicket(--unbookIndex, unbookOwner);

                break;
            case "unsell":
                int unsaleIndex = Integer.parseInt(request.getParameter("index"));
                saleModel.unsaleTicket(--unsaleIndex);

                break;
            case "unbookall":
                saleModel.resetBookings();

                break;
        }
    }
}
