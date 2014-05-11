package mvows;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;

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
    HashMap<String, String> requestCookies = new HashMap<String,String>();
    HashMap<String, String> responseCookies = new HashMap<String,String>();
    //for session
    static int sessionCookieId = 1;
    static Object sessionCookieLock = new Object();
    //储存 cookieid 和 session 之间的关系
    static Hashtable<String, WebletSession> sessionMap = new Hashtable<>();

    protected WebletSession getSession() {
        //检查请求中是否有 session cookie，没有就返回一个给浏览器
        String sessionCookie = getRequestCookie("webletsessionid");

        if(sessionCookie==null){
            //浏览器中没有 sessionid，创建一个
            int id;
            //制作 cookie
            synchronized (sessionCookieLock){
                id = sessionCookieId++;
            }

            sessionCookie = String.valueOf(id);

            setResponseCookies("webletsessionid", sessionCookie);
        }

        //检查有没有 session 对象，没有就创建一个，并添加进 sessionMap
        WebletSession session = sessionMap.get(sessionCookie);

        if(session==null) {
            session = new WebletSession();
            sessionMap.put(sessionCookie,session);
        }

        session.lastUsed = System.currentTimeMillis();
        return session;
    }

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
