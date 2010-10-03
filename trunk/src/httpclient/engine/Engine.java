package httpclient.engine;

import httpclient.engine.response.BrowserWindow;
import httpclient.engine.response.InfoWindow;
import httpclient.engine.response.Response;

import java.io.*;
import java.net.*;


public class Engine implements Runnable
{
    private URI uri;
    private BrowserWindow browserWindow;
    private InfoWindow infoWindow;
    private String address;
    private PreferenceList preferences;
    
    public Engine(String address, PreferenceList preferences,
            BrowserWindow browserWindow, InfoWindow infoWindow){
        this.address = address;
        this.browserWindow = browserWindow;
        this.infoWindow = infoWindow;
        this.preferences = preferences;
    }

    public void run() {
        
        String host;
        int port;
        String path;
        String query;
        
        browserWindow.setProgressValue(0);
        
        try {
            uri = new URI(address);
        } catch (URISyntaxException exc) {
            System.out.println(exc.getMessage());
        }
        host = uri.getHost();
        if(host.isEmpty()){
           // throw new IOException("no host specified");
        }

        port = uri.getPort();
        if(port < 1){
            port = 80;
        }
        
        path = uri.getPath();
        if(path.isEmpty()){
            path = "/";
        }
        
        query = uri.getQuery();
        if(query!=null){
            query = "?" + query;
        } else {
            query = "";
        }
        try {
            
            Socket socket = new Socket(host, port);
            // функция по формированию запроса
            // URLEncoder.encode
            String header = preferences.getRequestType() + " "
                   + path + query + " HTTP/1.1\r\n"
                   + "Host: " + host + "\r\n"
                   + "User-Agent: httpclient0.2\r\n\r\n";
                   //+ "Accept: text/html\r\n"
                   //+ "Connection: close\r\n\r\n";
                   
            // пишем в сокет HTTP request
            socket.getOutputStream().write(header.getBytes());
            socket.getOutputStream().flush();
            
            // сколько ждем ответ сервера
            // socket.setSoTimeout(5000);
           
            // принимаем ответ сервера
            InputStream is = socket.getInputStream();

            Response response = new Response(is, browserWindow, infoWindow);
            response.read();

            socket.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
            browserWindow.setTextPaneValue(e.getMessage());
        }
        //return null;
    }
}
