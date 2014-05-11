package myapps;

import mvows.MyWeblet;
import mvows.WebletSession;

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
/*        out.println("<html>");
        out.println("<body>");*/

        //从请求中获取名字
        String firstName = parameters.get("firstname");
        String lastName = parameters.get("lastname");

        //如果名字不完整，则返回重新输入
        if (firstName == null || lastName == null) {
            sendRedirect("/EnterName.html");

        }
        else {
            //名字完整，获取 session，将名字存入其中，然后跳转到 SessionTest 来显示名字
            WebletSession session = getSession();
            session.setAttribute("firstname",firstName);
            session.setAttribute("lastname", lastName);
            sendRedirect("/SessionTest");
        }

        //check if cookie exists
/*        String cookie = getRequestCookie("TestCookie");
        if(cookie==null){
            synchronized ((cookieLock)){
                cookie="Cookie_"+cookieNumber++;
            }

            setResponseCookies("TestCookie", cookie);

            System.out.println("Visiting browser does not have cookie");
            System.out.println("Setting cookie to: "+cookie);
        } else {
            System.out.println("Browser has cookie "+cookie);
        }*/

        //add some dynamic info
/*        out.println("<p>the time is now ");
        out.println(new Date());
        out.println("<p>the Resourse name is "+resource);
        out.println("<p>The Query String = "+queryString);
*/
/*        out.println("</body>");
        out.println("</html");*/
    }
}
