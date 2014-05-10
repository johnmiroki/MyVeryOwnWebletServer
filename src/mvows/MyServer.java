package mvows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by John on 4/30/2014.
 *  * 服务器类，程序入口，功能是：
 * 1 创建服务器
 * 2 对每个连接请求创建 socket
 * 3 建立新线程，把 socket 传入其中处理
 */
public class MyServer {

    public static void main(String[] args) throws IOException {

    ServerSocket server = new ServerSocket(80);

    //建立 socket
    while(true) {
        Socket s = server.accept();
        //把 socket 传入新线程
        System.out.println("client connected");
        SocketHandler sh = new SocketHandler(s);

        sh.start();

    }
    }

}

/**
 * 处理 socket 的类，可以看做 Weblet 的容器，功能有
 * 1 从 socket 中获取输入流和输出流并分别包装
 * 2 从输入流中解析出 resource 和 parameter
 * 3 调用 WebletProcessor 把 resource parameter 输入流 输出流 传入其中，由它制造输出
 */
class SocketHandler extends Thread{
    private Socket client;

    public SocketHandler(Socket client){
        this.client=client;

    }

    @Override
    public void run() {

        System.out.println("new thread!");
        //获取输入流并包装
        //获取输出流并包装
        try(BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream()) ) {

            //获取客户端传入的第一行头信息
            String firstLine = in.readLine();

            //把第一行解析为数组，取第二部分
            String resource = firstLine.split(" ")[1];

            //参数列表
//            HashMap<String,String> parameters = null;

            //Class
            Class cls = null;
            //把第二部分由问号处分为两部分,第一部分为 resource 第二部分为 query字符串（如果有）
            int queryIndex = resource.indexOf("?");
            String queryString = null;
            if (queryIndex>0) {
                queryString = resource.substring(queryIndex+1);
                resource=resource.substring(0,queryIndex);

                //如果有 queryString 生成 参数map
//                if (queryString!=null){
//                    parameters = Utils.parseQueryString(queryString);
//                }
            }

            String cookieHeaderLine = null;
            while(true) {
                String line = in.readLine();
                if (line.startsWith("Cookie")) {
                    cookieHeaderLine=line;
                    break;
                }

                if (line.equals(""));
                    break;
            }

            //根据 resource 找到对应的 class
            for(WebletConfigs config: WebletConfigs.webletConfigs) {
                if (config.url.equalsIgnoreCase(resource)){

                    System.out.println("resource matched!");
                    WebletProcessor wp = new WebletProcessor();

                    try {
                        wp.process(config.cls, out, resource,queryString,cookieHeaderLine);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }

                    out.close();
                    in.close();
                    client.close();

                    return;
                }
            }

            //如果以上没有 return 说明没有对应的 weblet，但要继续处理静态页面
            File dir = new File("C:\\Users\\John\\IdeaProjects\\mvows\\src");
            File file = new File(dir, resource);
            //如果 resource 结果是目录 说明是默认根目录 那么返回默认 index
            if (file.exists() && file.isDirectory()) {
                file = new File(file,"index.html");
            }

            if(!file.exists()) {
                //请求的目标不存在
                out.println("HTTP/1.0 404 Not Found");
                out.println(); // The empty line

            } else {
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type："+Utils.lookupContentType(resource));
                out.println(); // The empty line

                //输出文件内容
                FileInputStream inFile = new FileInputStream(file);

                int c = 0;

                while ((c=inFile.read())>=0){
                    out.write(c);
                }

                inFile.close();
            }



            out.close();
            in.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}