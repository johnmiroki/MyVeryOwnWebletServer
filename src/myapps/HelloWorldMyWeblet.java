package myapps;

import mvows.MyWeblet;

import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by John on 4/30/2014.
 */
public class HelloWorldMyWeblet extends MyWeblet{

    @Override
    public void doRequest(String resource, String queryString, HashMap<String, String> parameters, PrintWriter out) {
        //设置文档类型
        setContentType("text/plain");

        //跳转
        sendRedirect("http://163.com");

        //输出文档内容
        out.println("<html>");
        out.println("<body>");
        out.println("<h2>Hello, Stella!</h2>");
        out.println("Hello from my first Weblet");
        out.println("</body");
        out.println("</html>");
    }

}
