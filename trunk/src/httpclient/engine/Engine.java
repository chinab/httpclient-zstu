package httpclient.engine;

import httpclient.engine.request.Request;
import httpclient.engine.ui.BrowserWindow;
import httpclient.engine.general.HTTPStatusCodes;
import httpclient.engine.general.cookie.CookieProcessor;
import httpclient.engine.ui.InfoWindow;
import httpclient.engine.response.Response;
import httpclient.engine.ui.AuthorisationForm;
import httpclient.gui.handlers.AuthParametersHandler;

import java.io.*;
import java.net.*;


public class Engine implements Runnable, AuthParametersHandler
{
    private URI uri;
    private BrowserWindow browserWindow;
    private InfoWindow infoWindow;
    private AuthorisationForm authForm;
    private String address;
    private PreferenceList preferences;

    private String login = "";
    private String password = "";
    private boolean authParamsSpecified;
    
    public Engine(String address, PreferenceList preferences,
            BrowserWindow browserWindow, InfoWindow infoWindow,
            AuthorisationForm authForm){
        this.address = address;
        this.browserWindow = browserWindow;
        this.authForm = authForm;
        this.infoWindow = infoWindow;
        this.preferences = preferences;
    }

    public void setAuthParameters(String login, String password){
         authParamsSpecified = true;
        this.login = login;
        this.password = password;
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

            //TODO: move cp creation to mainwindow (better - create new main class)
            String resourceName;
            CookieProcessor cp = new CookieProcessor();
            cp.loadFromFile();

            while(true){

                // create socket
                Socket socket = new Socket(host, port);
                OutputStream out = socket.getOutputStream();
                InputStream is = socket.getInputStream();

                // send request
                Request request = new Request(out, uri, cp, preferences);
                if(authParamsSpecified){
                    request.setAuthenticationParameters(login, password);
                    authParamsSpecified = false;
                }
                request.write();
                resourceName = request.getResourceName();
                // сколько ждем ответ сервера
                // socket.setSoTimeout(5000);

                // receive response
                

                Response response = new Response(is, resourceName, cp, preferences,
                        browserWindow, infoWindow);
                response.read();

                int code = response.getStatusCode();
                if(code!=HTTPStatusCodes.HTTP_UNAUTHORIZED){
                    break;
                } else {
                    authForm.setVisible(Engine.this, response.getRealm());

                    if(!authParamsSpecified){
                        break;
                    }
                }

                socket.close();
            }

            cp.saveToFile();
            
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
