package mvows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * Created by John on 4/30/2014.
 * 服务器类，程序入口，功能是：
 * 1 创建服务器
 * 2 对每个连接请求创建 socket
 * 3 建立新线程，把 socket 传入其中处理
 */
public class MyServer {

    public static void main(String[] args) throws IOException {

    ServerSocket server = new ServerSocket(80);

    //建立 socket
    while(true) {
        Socket socket = server.accept();
        //把 socket 传入新线程
        System.out.println("client connected");
        SocketHandler sh = new SocketHandler(socket);

        sh.start();

    }
    }

}

/**
 * 接收以多线程方式处理每个 socket
 * 基本思路是：
 * 1. 解析 http 请求的第一行第二部分 分析其中用户请求的资源和附带的参数（Get方法时）
 * 2 分析请求的资源，如果是请求存在的 Weblet，则调用 WebletProcessor 来处理；如果请求的是静态页面，则负责返回页面；如果
 *   无资源，则返回默认 index.html 页面；如果请求的资源不存在，则返回 404 状态以及 404 页面（可选）
 * 3 分析请求中所带的参数，放在 map 中传给 WebletProcessor 以便处理
 *
 */
class SocketHandler extends Thread{

    //储存传入的 socket
    private Socket socket = null;
    String rootDirectory = "C:\\Users\\John\\IdeaProjects\\mvows\\src";

    public SocketHandler(Socket socket){
        this.socket=socket;
    }

    private void fileOutput(PrintWriter out, File file) {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(file));
            String line;
            while((line = fileReader.readLine())!= null){
                out.println(line);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //继承多线程 @link run 方法，处理每个 socket
    public void run(){

        try(
            //从 socket 中获取输入和输出流并包装以便操作
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream())
         ) {
            //收到的第一行
            String firstLine = in.readLine();

            //第一行的第二部分 就是资源和参数
            String resource = firstLine.split(" ")[1];

            //把资源和参数部分分开
            int queryIndex = resource.indexOf("?");
            String queryString = null;
            if (queryIndex>0) {
                //存在参数时
                queryString = resource.substring(queryIndex+1);
                resource = resource.substring(0,queryIndex);
            } //不存在参数的话，就不用分了

            //检查是否有 cookie
            String cookieHeaderLine = null;
            while(true){
                String line = in.readLine();
                if(line==null){
                    break;
                }
                else if(line.startsWith("Cookie:")){
                    //找到 cookie 行，取回 Cookie: 后面实际的 cookie 键值对
                    cookieHeaderLine = line.substring("Cookie:".length()+1);
                    break;
                }
                else if(line.equals("")){
                    //空行
                    break;
                }
            }

            //根据 resource 情况，决定如何处理
            //检查是否与 WebletConfigs 中注册的 Weblet 匹配
            for(WebletConfigs config:WebletConfigs.webletConfigs){
                if(config.url.equalsIgnoreCase(resource)){
                    //找到匹配，调用 WeblProcessor 处理
                    WebletProcessor wp = new WebletProcessor();
                    wp.process(config.cls,out,resource,queryString,cookieHeaderLine);

                    //执行完毕，退出整个流程
                   return;
                }
            }

            //只有上述没有执行，才会走到这一步，说明没有找到对应的 Weblet，该判断是要访问静态页面

             File requestedFile = new File(rootDirectory,resource);
            //对三种情况分别处理：存在且是目录，存在且是文件，不存在
            if (requestedFile.exists()==false) {
                //不存在，返回 404
                out.println("HTTP/1.0 404 NOT FOUND");
                out.println();

                socket.close();
                return;
            }
            else if(requestedFile.isDirectory()){
                //存在且目录，返回默认 index 文件
                requestedFile = new File(requestedFile,"index.html");
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println();

                fileOutput(out,requestedFile);

                socket.close();
                return;
            }
            else {
                //存在且是文件
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: "+Utils.lookupContentType(resource));
                out.println();

                fileOutput(out,requestedFile);
                socket.close();
                return;
            }





        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}