import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by simon on 18.04.15.
 */
@WebServlet(name = "Random")
public class Random extends HttpServlet {

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

        // send random number as html-response
        int randNum = randInt(a, b);
        //response.setContentType("text/html");
        //response.getWriter().println(randNum);
        response.sendRedirect("/random/result.jsp?result=" + randNum);
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

        // send random number as html-response
        int randNum = randInt(a, b);
        //response.setContentType("text/html");
        //response.getWriter().println(randNum);
        response.sendRedirect("/random/result.jsp?result=" + randNum);
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
