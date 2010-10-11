package httpclient.engine;

import httpclient.engine.request.Request;
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
        browserWindow.enableGoButton(false);
        browserWindow.setProgress(0);
        
        try {
            uri = new URI(address);
        } catch (URISyntaxException exc) {
            System.out.println(exc.getMessage());
        }
        host = uri.getHost();
        port = uri.getPort();
        if(port < 1){
            port = 80;
        }
        try {
            
            Socket socket = new Socket(host, port);
            String resourceName;
            // while(!<Headers.Connection>="closed"){...}

            // send request
            OutputStream out = socket.getOutputStream();
            Request request = new Request(out, uri, preferences);
            request.write();
            resourceName = request.getResourceName();
            // сколько ждем ответ сервера
            // socket.setSoTimeout(5000);
           
            // receive response
            InputStream is = socket.getInputStream();

            Response response = new Response(is, resourceName, preferences,
                    browserWindow, infoWindow);
            response.read();

            socket.close();
            
        } catch(UnknownHostException e){
            browserWindow.showError("Unknown Host: " + e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            browserWindow.showError(e.getMessage());
        } finally {
            browserWindow.enableGoButton(true);
        }
    }
}
