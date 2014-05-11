package myapps;

import mvows.MyWeblet;
import mvows.WebletSession;

import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by John on 5/11/2014.
 */
public class SessionTest extends MyWeblet {
    @Override
    public void doRequest(String resource, String queryString, HashMap<String, String> parameters, PrintWriter out) {
        WebletSession session = getSession();
        String firstName = (String) session.getAttribute("firstname");
        if (firstName == null) {
            //名字不存在，跳转
            sendRedirect("/EnterName.html");
        }
        else {
            String lastName = (String) session.getAttribute("lastname");
            out.println("<html>");
            out.println("<body>");
            out.println("Welcome: " + firstName + " " + lastName);
            out.println("</body>");
            out.println("</html>");
        }



    }
}
