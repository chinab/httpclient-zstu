package httpclient.engine.request;

import httpclient.engine.PreferenceList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;


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
               + "User-Agent: httpclient0.2\r\n\r\n";
               //+ "Accept: text/html\r\n"
               //+ "Connection: close\r\n\r\n";
        return header;
    }

    private void getFormFields() throws IOException {
        HTMLFile file = new HTMLFile(preferences.getPathToPOSTFile());
        file.parse();
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
