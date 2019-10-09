///A Simple Web Server (WebServer.java)

package http.server;

import domain.HttpRequest;
import domain.HttpResponse;

import javax.ws.rs.core.Response.Status;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * <p>
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

    private static String path;

    public void sendHttpGetResponse(HttpRequest httpRequest, Socket socket, PrintWriter printWriter) throws IOException {
        printWriter.println("");

        //Load ressource
        String url = httpRequest.getUrl();
        if (url == null) {
            url = "/index.html";
        }
        url = path + url;

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHttpProtocolVersion("HTTP/1.0");
        httpResponse.setContentType("Content-Type: text/html");
        httpResponse.setServerName("Server: Bot");

        File file = new File(url);
        String content = "";
        if (!file.isFile()) {
            httpResponse.setHttpStatus(Status.NOT_FOUND);
            httpResponse.setContent("<html>404 not found</html>");
        } else {
            httpResponse.setHttpStatus(Status.OK);
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(url));
                String line = bufferedReader.readLine();
                while (line != null) {
                    content += line;
                    line = bufferedReader.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpResponse.setContent(content);
        }

        // Send the HTML page
        printWriter.println(httpResponse.toString());
        printWriter.flush();
        socket.close();
        System.out.println("Response sent: " + httpResponse.toString());
        printWriter.close();
    }

    public void sendHttpPostResponse(HttpRequest httpRequest, Socket socket, PrintWriter printWriter){
        //Load ressource
        String url = httpRequest.getUrl();
        if (url == null) {
            url = "/index.html";
        }
        url = path + url;

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHttpProtocolVersion("HTTP/1.0");
        httpResponse.setContentType("Content-Type: text/html");
        httpResponse.setServerName("Server: Bot");

        File file = new File(url);
        String content = httpRequest.getContent();
        if (!file.isFile()) {
            httpResponse.setHttpStatus(Status.NOT_FOUND);
            httpResponse.setContent("<html>404 not found</html>");
        } else {
            httpResponse.setHttpStatus(Status.OK);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file,false);
                fileOutputStream.write(httpRequest.getContent().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpResponse.setContent(content);
        }

        // Send the HTML page
        printWriter.println(httpResponse.toString());
        printWriter.flush();
        System.out.println("Response sent: " + httpResponse.toString());
        printWriter.close();
    }

    /**
     * WebServer constructor.
     */
    protected void start() {
        ServerSocket s;

        System.out.println("Webserver starting up on port 80");
        System.out.println("(press ctrl-c to exit)");
        try {
            // create the main server socket
            s = new ServerSocket(3000);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        System.out.println("Waiting for connection");
        for (; ; ) {
            try {
                // wait for a connection
                Socket remote = s.accept();
                // remote is now the connected socket
                System.out.println("Connection, sending data.");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        remote.getInputStream()));
                PrintWriter out = new PrintWriter(remote.getOutputStream());
                // read the data sent. We basically ignore it,
                // stop reading once a blank line is hit. This
                // blank line signals the end of the client HTTP
                // headers.
                String str = ".";
                HttpRequest httpRequest = new HttpRequest();
                str = in.readLine();
                String[] firstLine = str.split(" ");
                httpRequest.setHttpMethod(firstLine[0]);
                httpRequest.setUrl(firstLine[1]);
                httpRequest.setHttpProtocolVersion(firstLine[2]);

                str = in.readLine();

                while (!str.equals("")) {
                    String[] parameters = str.split(": ");
                    httpRequest.addHeader(parameters[0], parameters[1]);
                    str = in.readLine();
                }
                int contentLength = 0;

                if(httpRequest.getHeaderMap().containsKey("Content-Length")) {
                    contentLength = Integer.parseInt(httpRequest.getHeaderMap().get("Content-Length"));
                }

                String content = "";
                while (contentLength > 0) {
                    str = in.readLine();
                    content = content + str;
                    contentLength = contentLength - content.getBytes().length;
                }
                httpRequest.setContent(content);
                if ("GET".equals(httpRequest.getHttpMethod())){
                    sendHttpGetResponse(httpRequest, remote, out);
                } else if ("POST".equals(httpRequest.getHttpMethod())){
                    sendHttpPostResponse(httpRequest, remote, out);
                }
                System.out.println(httpRequest.toString());

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: ");
            }
        }
    }

    /**
     * Start the application.
     *
     * @param args Command line parameters are not used.
     */
    public static void main(String args[]) {
        path = args[0];
        WebServer ws = new WebServer();
        ws.start();
    }
}
