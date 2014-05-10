package myapps;

import mvows.MyWeblet;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by John on 5/2/2014.
 */
public class NameProcessor extends MyWeblet {

    static int cookieNumber = 1;
    static Object cookieLock = new Object();

    @Override
    public void doRequest(String resource, String queryString, HashMap<String, String> parameters, PrintWriter out) {
        out.println("<html>");
        out.println("<body>");

        //write out names
        out.println("Hello "+parameters.get("firstname")+" "+parameters.get("lastname"));

        //check if cookie exists
        String cookie = getRequestCookie("TestCookie");
        if(cookie==null){
            synchronized ((cookieLock)){
                cookie="Cookie_"+cookieNumber++;
            }

            setResponseCookies("TestCookie", cookie);

            System.out.println("Visiting browser does not have cookie");
            System.out.println("Setting cookie to: "+cookie);
        } else {
            System.out.println("Browser has cookie "+cookie);
        }

        //add some dynamic info
        out.println("<p>the time is now ");
        out.println(new Date());
        out.println("<p>the Resourse name is "+resource);
        out.println("<p>The Query String = "+queryString);

        out.println("</body>");
        out.println("</html");
    }
}
