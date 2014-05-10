package mvows;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by John on 4/30/2014.
 */
public class WebletProcessor {



    //输出头信息然后调用 MyWeblet 输出response 体
    public void process(Class cls, PrintWriter out, String resource, String queryString, String cookieHeaderLine) throws IllegalAccessException, InstantiationException, UnsupportedEncodingException {

        Object instance = cls.newInstance();

        MyWeblet myWeblet = (MyWeblet)instance;

        //暂存容器
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        PrintWriter tempOut = new PrintWriter(byteArray);
        System.out.println("Ready to output");

        HashMap<String, String> parameters = Utils.parseQueryString(queryString);

        //传入 cookies
        myWeblet.requestCookies = Utils.getRequestCookies(cookieHeaderLine);
        //把 weblet 输出信息暂存起来
        myWeblet.doRequest(resource, queryString, parameters, tempOut);

        //向客户端输出头信息
        if (myWeblet.newUrl!=null){
            //有跳转需求，处理
            out.println("HTTP/1.0 302 FOUND");
            out.println("Location: "+myWeblet.newUrl);
            Utils.printCookieHeaders(myWeblet.responseCookies,out);
            out.println();
        }else if(myWeblet.errorCode!=0) {
            //有错误代码
            out.println("HTTP/1.0 "+myWeblet.errorCode+" "+myWeblet.description);
            Utils.printCookieHeaders(myWeblet.responseCookies,out);
            out.println();

        } else {
            out.println("HTTP/1.0 200 OK");

            if(myWeblet.contentType!=null){
                out.println("Content-Type: "+myWeblet.contentType);
            } else {
                out.println("Content-Type: text/html");

            }
            Utils.printCookieHeaders(myWeblet.responseCookies,out);
            out.println();
        }




        //把暂存信息刷新到暂存容器
        tempOut.close();

        //把暂存容器中的信息输出到客户端
        out.println(byteArray.toString());

        out.close();
    }
}
