package utils;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * Created by John on 4/30/2014.
 */
public class Utils {
    public static HashMap<String, String> parseQueryString(String queryString) throws UnsupportedEncodingException {

        HashMap<String, String> parameters = new HashMap<String, String>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyVal = pair.split("=");

            String key = URLDecoder.decode(keyVal[0], "utf-8");
            String val = URLDecoder.decode(keyVal[1], "utf-8");
            parameters.put(key, val);
        }

        return parameters;
    }

    public static String lookupContentType(String name) {
        name = name.toLowerCase(); // ignore case

        if (name.endsWith(".htm") || name.endsWith(".html"))
            return "text/html";
        if (name.endsWith(".txt") || name.endsWith(".text"))
            return "text/plain";
        if (name.endsWith(".xml"))
            return "text/xml";
        if (name.endsWith(".css"))
            return "text/css";
        if (name.endsWith(".gif"))
            return "image/gif";
        if (name.endsWith(".png"))
            return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg"))
            return "image/jpeg";
        if (name.endsWith(".wav"))
            return "audio/wav";
        if (name.endsWith(".mpg") || name.endsWith(".mpeg"))
            return "video/mpeg";

        return null;
    }

    public static HashMap<String, String> getRequestCookies(String cookieHeaders){

        HashMap<String, String> requestCookies = new HashMap<>();

        if(cookieHeaders == null || !cookieHeaders.startsWith("Cookie:")){
            return requestCookies;
        }

        String cookieString = cookieHeaders.substring("Cookie:".length()).trim();

        String[] cookies = cookieString.split(";");

        for(String cookie : cookies) {
            String[] keyVal = cookie.split("=");

            String key = keyVal[0].trim();
            String value = keyVal[1].trim();

            requestCookies.put(key,value);
        }
        return null;
    }

    public static void printCookieHeaders(HashMap<String, String> responseCookies, PrintWriter out) {

        for(String cookieName: responseCookies.keySet()){

            out.print("Set-Cookie:");
            out.print(cookieName+"="+responseCookies.get(cookieName));
            out.print("; ");

        }

        out.println("Path=/");
    }
}