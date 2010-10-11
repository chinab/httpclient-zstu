package httpclient.engine.request;

import httpclient.engine.PreferenceList;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class Request {

    private PreferenceList preferences;
    private URI uri;
    private OutputStream out;

    public Request(OutputStream out, URI uri, PreferenceList preferences){
        this.preferences = preferences;
        this.uri = uri;
        this.out = out;
    }

    private String getHeaderString(){
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
        String header = preferences.getRequestType() + " "
               + path + query + " HTTP/1.1\r\n"
               + "Host: " + uri.getHost() + "\r\n"
               + "User-Agent: httpclient 0.1\r\n\r\n";
               //+ "Accept: text/html\r\n"
               //+ "Connection: close\r\n\r\n";
        return header;
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

    public void write() throws IOException {
        if(this.preferences.getRequestType().equals("POST")){
            getFormFields();
            return;
        }
            out.write(getHeaderString().getBytes());
            out.flush();
    }
}
