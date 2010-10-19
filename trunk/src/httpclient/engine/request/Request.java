package httpclient.engine.request;

import httpclient.engine.PreferenceList;
import httpclient.engine.general.HTTPHeaders;
import httpclient.engine.general.Header;
import httpclient.engine.general.HeaderList;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import sun.misc.BASE64Encoder;

public class Request implements HTTPHeaders {

    private PreferenceList preferences;
    private URI uri;
    private OutputStream out;
    private boolean useAuthentication = false;
    private String password;
    private String login;

    public Request(OutputStream out, URI uri, PreferenceList preferences){
        this.preferences = preferences;
        this.uri = uri;
        this.out = out;
    }

    private String getRequestLine(){
        String path = uri.getPath();
        if(path.isEmpty()){
            path = "/";
        }
        String query = uri.getQuery();
        if(query!=null){
            query = "?" + query;
        } else {
            query = "";
        }
        return preferences.getRequestType() + " " + path + query + " HTTP/1.1";
    }

    private String getRequestString(){
        // create headers
        HeaderList headerList = new HeaderList();
        headerList.addHeader(new Header(HEADER_HOST, uri.getHost()));
        headerList.addHeader(new Header(HEADER_USER_AGENT, "httpclient 0.1"));

        if(useAuthentication){
            BASE64Encoder encoder = new BASE64Encoder();
            String authParams = login + ":" + password;
            authParams = "Basic " + encoder.encode(authParams.getBytes());
            headerList.addHeader(new Header(HEADER_AUTHORIZATION, authParams));
        }
        String request = getRequestLine()
                        + "\r\n"
                        + headerList.toString()
                        + "\r\n";
                      //+ message body (if POST)

        return request;
    }

    private void getFormFields() throws IOException {
        HTMLFile file = new HTMLFile(preferences.getPathToPOSTFile());
        file.parse();
        System.out.println(file.getAction());
        System.out.println(file.getArguments());
    }

    public String getResourceName(){
        String path = uri.toString();
        path.replace("\\", "/");
        int index = path.lastIndexOf("/");
        if(index>0){
            if(uri.getHost().endsWith(path.substring(index+1))){
                return "";
            } else {
                return path.substring(index+1);
            }
        }
        return "";
    }

    public void setAuthenticationParameters(String login, String password){
        this.login = login;
        this.password = password;
        useAuthentication = true;
    }

    public void write() throws IOException {
        if(this.preferences.getRequestType().equals("POST")){
            getFormFields();
            // change!!
            throw new IOException("post request");
        }
        out.write(getRequestString().getBytes());
        out.flush();
    }
}
