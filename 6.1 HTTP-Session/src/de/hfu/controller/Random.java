package de.hfu.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by simon on 21.04.15.
 */
@WebServlet(name = "Random")
public class Random extends HttpServlet {

    /**
     * Attribute keys
     */
    private final String randomAttrKey = "randomNumber";
    private final String counterAttrKey = "sessionCounter";

    /**
     * Incoming POST-requests.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int a = 0;
        int b = 0;

        // get needed request-attributes
        try {
            a = Integer.parseInt(request.getParameter("a"));
            b = Integer.parseInt(request.getParameter("b"));
        } catch(NumberFormatException e) {
            // not a valid integer number, redirect to error-page
            response.sendRedirect("/random/invalidvalues.html");

            return;
        }

        // check session counter
        handleSessionCounter(request);

        // get random number and attach it as attribute
        int randNum = randInt(a, b);
        request.setAttribute(randomAttrKey, new Integer(randNum));

        // get the result-View and forward connected client
        RequestDispatcher resultView = request.getRequestDispatcher("/random/result.jsp");
        resultView.forward(request, response);
    }

    /**
     * Incoming GET-request.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int a = 0;
        int b = 0;

        // get needed request-attributes
        try {
            a = Integer.parseInt(request.getParameter("a"));
            b = Integer.parseInt(request.getParameter("b"));
        } catch(NumberFormatException e) {
            // not a valid integer number, redirect to error-page
            response.sendRedirect("/random/invalidvalues.html");

            return;
        }

        // check session counter
        handleSessionCounter(request);

        // get random number and attach it as attribute
        int randNum = randInt(a, b);
        request.setAttribute(randomAttrKey, new Integer(randNum));

        // get the result-View and forward connected client
        RequestDispatcher resultView = request.getRequestDispatcher("/random/result.jsp");
        resultView.forward(request, response);
    }

    /**
     * Checks if the connected client has already a session running.
     * If false, the counterAttrKey-value will be initiated as integer 1.
     * If true, the counterAttrKey-value will be incremented and set again.
     * @param request
     */
    private void handleSessionCounter(HttpServletRequest request) {

        // get the client's session
        HttpSession session = request.getSession();

        // new session?
        if(session.isNew()) {
            // new session, set session-counter
            session.setAttribute(counterAttrKey, new Integer(1));
        }
        else {
            // not new, get current value, increment it and set it again
            Integer counterValue = (Integer) session.getAttribute(counterAttrKey);
            session.setAttribute(counterAttrKey, new Integer(++counterValue));
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     */
    private int randInt(int min, int max) {

        int randomNum = -1;

        // valid range?
        if(min < max) {
            randomNum = (int) (Math.random() * (max - min)) + min;
        }

        return randomNum;
    }

}
