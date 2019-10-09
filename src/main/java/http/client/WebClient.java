package http.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;

public class WebClient {

    public static void sendHttpGetRequest(PrintWriter printWriter) {
        String params = "";
        try {
            params = URLEncoder.encode("param1", "UTF-8")
                    + "=" + URLEncoder.encode("value1", "UTF-8");
            params += "&" + URLEncoder.encode("param2", "UTF-8")
                    + "=" + URLEncoder.encode("value2", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        printWriter.println("GET /index.html HTTP/1.1");
        printWriter.println("Content-Length: "+params.length());
        printWriter.println("Content-Type: application/x-www-form-urlencodedrn");
        printWriter.println("");
        printWriter.println(params);
        printWriter.flush();
    }

    public static void sendPostRequest(PrintWriter printWriter){
        String params = "{\n" +
                "  \"this\" : \"that\"\n" +
                "}";
        printWriter.println("POST /info/info.json HTTP/1.1");
        printWriter.println("Content-Length: "+params.length());
        printWriter.println("Content-Type: json");
        printWriter.println("");
        printWriter.println(params);
        printWriter.flush();
    }

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage java WebClient <server host name> <server port number>");
            return;
        }

        String httpServerHost = args[0];
        int httpServerPort = Integer.parseInt(args[1]);

        try {
            InetAddress addr;
            Socket sock = new Socket(httpServerHost, httpServerPort);
            addr = sock.getInetAddress();
            System.out.println("Connected to " + addr);
            PrintWriter out = new PrintWriter(sock.getOutputStream());
            sendHttpGetRequest(out);
            //sendPostRequest(out);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String str = in.readLine();
            str = ".";

            while (str.length() != 0) {
                str = in.readLine();
                System.out.println(str);
            }
            str = in.readLine();
            System.out.println(str);

            sock.close();
        } catch (java.io.IOException e) {
            System.out.println("Can't connect to " + httpServerHost + ":" + httpServerPort);
            System.out.println(e);
        }
    }
}