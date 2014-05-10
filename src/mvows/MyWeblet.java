package mvows;

import java.io.PrintWriter;
import java.util.HashMap;

/**
 * MyWeblet 抽象类，提供接口，供 MyWeblet 作者继承、调用
 * @author John
 * Created by John on 4/30/2014.

 */
public abstract class MyWeblet {

    String contentType=null;
    String description=null;
    int errorCode=0;
    String newUrl=null;
    HashMap<String, String> requestCookies = null;
    HashMap<String, String> responseCookies = new HashMap<String,String>();

    //
    public abstract void doRequest(
            String resource,
            String queryString,
            HashMap<String,String> parameters,
            PrintWriter out
    );

    protected void setContentType(String contentType){
        this.contentType=contentType;
    }

    protected void setError(int errorCode,String description){
        this.errorCode=errorCode;
        this.description=description;
    }

    protected void sendRedirect(String newUrl){
        this.newUrl=newUrl;
    }

    public String getRequestCookie(String cookieName) {
        return requestCookies.get(cookieName);
    }

    protected void setResponseCookies(String cookieName, String cookieValue) {
        responseCookies.put(cookieName,cookieValue);
    }
}
