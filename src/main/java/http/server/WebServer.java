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

    private HttpResponse genericHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHttpProtocolVersion("HTTP/1.0");
        httpResponse.setContentType("Content-Type: text/html");
        httpResponse.setServerName("Server: Bot");

        return httpResponse;
    }

    public void sendHttpGetResponse(HttpRequest httpRequest, Socket socket, PrintWriter printWriter) throws IOException {
        printWriter.println("");

        //Load ressource
        String url = httpRequest.getUrl();
        if (url == null) {
            url = "/index.html";
        }
        url = path + url;

        HttpResponse httpResponse = genericHttpResponse();

        File file = new File(url);
        byte[] b = null;
        if (!file.isFile()) {
            httpResponse.setHttpStatus(Status.NOT_FOUND);
            httpResponse.setContent("<html>404 not found</html>");

            socket.getOutputStream().write(httpResponse.toString().getBytes());
        } else {
            httpResponse.setHttpStatus(Status.OK);

            RandomAccessFile f = new RandomAccessFile(url, "r");
            b = new byte[(int)f.length()];
            f.readFully(b);

            httpResponse.setContentType(getContentTypeFromFile(url));

            socket.getOutputStream().write(httpResponse.toString().getBytes());
            socket.getOutputStream().write(b);
        }

        // Send the HTML page

        socket.getOutputStream().flush();
        socket.getOutputStream().close();
        System.out.println("Response sent: " + httpResponse.toString());
    }

    public void sendHttpHeadResponse(HttpRequest httpRequest, Socket socket, PrintWriter printWriter) throws IOException {
        printWriter.println("");
        //Load ressource
        String url = httpRequest.getUrl();
        if (url == null) {
            url = "/index.html";
        }
        url = path + url;

        HttpResponse httpResponse = genericHttpResponse();

        String[] listeExtensions = url.split("\\.");
        String extension = listeExtensions[listeExtensions.length-1];
        if ("sh".equals(extension.toLowerCase())){
            ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c", "echo Hello > "+path+"/myfile.txt");
            pb.start();
            Runtime.getRuntime().exec("sh script.sh");
            httpResponse.setHttpStatus(Status.OK);
        }
        else{
            File file = new File(url);
            StringBuilder content = new StringBuilder();
            if (!file.isFile()) {
                httpResponse.setHttpStatus(Status.NOT_FOUND);
                httpResponse.setContent("<html>404 not found</html>");
            } else {
                httpResponse.setHttpStatus(Status.OK);
                httpResponse.setContent(httpRequest.getHeaderMap().toString());
                httpResponse.setContent(content.toString());
            }
        }

        // Send the HTML page
        printWriter.println(httpResponse.toString());
        printWriter.flush();
        System.out.println("Response sent: " + httpResponse.toString());
    }

    public void sendHttpPostResponse(HttpRequest httpRequest, Socket socket, PrintWriter printWriter) throws IOException {
        //Load ressource
        String url = httpRequest.getUrl();
        if (url == null) {
            url = "/index.html";
        }
        url = path + url;

        HttpResponse httpResponse = genericHttpResponse();

        File file = new File(url);
        String content = httpRequest.getContent();
        if (!file.isFile()) {
            try {
                file.createNewFile();
                httpResponse.setHttpStatus(Status.CREATED);
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write(httpRequest.getContent().getBytes());
            } catch (IOException e) {
                httpResponse.setHttpStatus(Status.BAD_REQUEST);
                httpResponse.setContent("<html>400: POST request failed</html>");
                e.printStackTrace();
            }
        } else {
            httpResponse.setHttpStatus(Status.OK);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write(httpRequest.getContent().getBytes());
            } catch (Exception e) {
                httpResponse.setHttpStatus(Status.INTERNAL_SERVER_ERROR);
                httpResponse.setContent("<html>500: internal server error</html>");
                e.printStackTrace();
            }
        }

        // Send the HTML page
        printWriter.println(httpResponse.toString());
        printWriter.flush();
        printWriter.close();
        System.out.println("Response sent: " + httpResponse.toString());
    }

    public void sendHttpPutResponse(HttpRequest httpRequest, PrintWriter printWriter) {
        //Load ressource
        String url = httpRequest.getUrl();
        if (url == null) {
            url = "/index.html";
        }
        url = path + url;

        HttpResponse httpResponse = genericHttpResponse();

        File file = new File(url);
        String content = httpRequest.getContent();

        if (!file.isFile()) {
            try {
                file.createNewFile();
                httpResponse.setHttpStatus(Status.CREATED);
            } catch (IOException e) {
                httpResponse.setHttpStatus(Status.BAD_REQUEST);
                httpResponse.setContent("<html>400: PUT request failed</html>");
                e.printStackTrace();
            }
        } else {
            httpResponse.setHttpStatus(Status.OK);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                fileOutputStream.write(httpRequest.getContent().getBytes());
            } catch (Exception e) {
                httpResponse.setHttpStatus(Status.INTERNAL_SERVER_ERROR);
                httpResponse.setContent("<html>500: internal server error</html>");
                e.printStackTrace();
            }
            httpResponse.setContent(content);
        }

        // Send the HTML page
        printWriter.println(httpResponse.toString());
        printWriter.flush();
        System.out.println("Response sent: " + httpResponse.toString());
    }

    public void sendHttpDeleteResponse(HttpRequest httpRequest, PrintWriter printWriter) {
        //Load ressource
        String url = httpRequest.getUrl();
        if (url == null) {
            url = "/index.html";
        }
        url = path + url;

        HttpResponse httpResponse = genericHttpResponse();

        File file = new File(url);
        String content = "";

        if (!file.isFile()) {
            httpResponse.setHttpStatus(Status.BAD_REQUEST);
            httpResponse.setContent("<html>400: DELETE request failed</html>");
        } else {
            httpResponse.setHttpStatus(Status.NO_CONTENT);
            try {
                if(file.delete()) {
                    httpResponse.setHttpStatus(Status.OK);
                    content = "<html>\n" +
                            "  <body>\n" +
                            "    <h1>File deleted.</h1> \n" +
                            "  </body>\n" +
                            "</html>";
                }
            } catch (Exception e) {
                httpResponse.setHttpStatus(Status.INTERNAL_SERVER_ERROR);
                httpResponse.setContent("<html>500: internal server error</html>");
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

    public String getContentTypeFromFile(String url){
        // lecture de l'extension pour mettre le content-type correct
        String[] listeExtensions = url.split("\\.");
        String extension = listeExtensions[listeExtensions.length-1];

        switch(extension.toLowerCase()){
            case "png":
                return "Content-Type: image/png";
            case "jpg":
                return "Content-Type: image/jpg";
            case "mp4":
                return "Content-Type: video/mp4";
            case "mp3":
                return "Content-Type: audio/mpeg";
            default:
                return "Content-Type: text/html";
        }
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
                try {
                    System.out.println("FirstLine "+str);
                    String[] firstLine = str.split(" ");
                    httpRequest.setHttpMethod(firstLine[0]);
                    httpRequest.setUrl(firstLine[1]);
                    httpRequest.setHttpProtocolVersion(firstLine[2]);
                }
                catch (Exception e){

                }

                str = in.readLine();

                while (!str.equals("")) {
                    String[] parameters = str.split(": ");
                    httpRequest.addHeader(parameters[0], parameters[1]);
                    str = in.readLine();
                }
                int contentLength = 0;

                if (httpRequest.getHeaderMap().containsKey("Content-Length")) {
                    contentLength = Integer.parseInt(httpRequest.getHeaderMap().get("Content-Length"));
                }

                String content = "";
                while (contentLength > 0) {
                    str = in.readLine();
                    content = content + str;
                    contentLength = contentLength - (str.length()+1);
                }
                httpRequest.setContent(content);
                String httpMethod = httpRequest.getHttpMethod();
                switch (httpMethod) {
                    case "GET":
                        sendHttpGetResponse(httpRequest, remote, out);
                        break;
                    case "POST":
                        sendHttpPostResponse(httpRequest, remote, out);
                        break;
                    case "PUT":
                        sendHttpPutResponse(httpRequest, out);
                        break;
                    case "HEAD":
                        sendHttpHeadResponse(httpRequest, remote, out);
                        break;
                    case "DELETE":
                        sendHttpDeleteResponse(httpRequest, out);
                }
                System.out.println(httpRequest.toString());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: "+e);
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
