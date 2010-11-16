package httpclient.engine.request;

import httpclient.engine.request.html.HTMLFileParser;
import httpclient.engine.PreferenceList;
import httpclient.engine.general.HTTPHeaders;
import httpclient.engine.general.Header;
import httpclient.engine.general.HeaderList;
import httpclient.engine.general.cookie.Cookie;
import httpclient.engine.general.cookie.CookieProcessor;
import httpclient.engine.request.html.Input;
import httpclient.engine.request.html.InputList;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import sun.misc.BASE64Encoder;

public class Request implements HTTPHeaders {

    private final String BOUNDARY_LINE = "1BEF0A57BE110FD467A";

    private PreferenceList preferences;
    private URI uri;
    private OutputStream out;
    private CookieProcessor cp;
    private boolean useAuthentication = false;
    private String password;
    private String login;

    public Request(OutputStream out, URI uri, CookieProcessor cp, PreferenceList preferences){
        this.preferences = preferences;
        this.uri = uri;
        this.out = out;
        this.cp = cp;
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

    private String getRequestString() throws IOException {
        String requestType = preferences.getRequestType();
        
        // common headers
        HeaderList headerList = new HeaderList();
        headerList.addHeader(new Header(HEADER_HOST, uri.getHost()));
        headerList.addHeader(new Header(HEADER_USER_AGENT, "httpclient 0.1"));
        headerList.addHeader(new Header(HEADER_CONNECTION, "close"));

        // cookies
        String path = uri.getPath();
        if(path.isEmpty()){
            path = "/";
        }
        ArrayList<Cookie> cookies = cp.getCookies(uri.getHost(), path);
        for(Cookie cookie: cookies){
            headerList.addHeader(new Header(HEADER_COOKIE, 
                    cookie.getName() +"="+ cookie.getValue()));
        }

        if(requestType.equals(PreferenceList.HTTP_REQUEST_GET)){
            if(useAuthentication){
                BASE64Encoder encoder = new BASE64Encoder();
                String authParams = login + ":" + password;
                authParams = "Basic " + encoder.encode(authParams.getBytes());
                headerList.addHeader(new Header(HEADER_AUTHORIZATION, authParams));
            }
        }

        String entityBody = "";

        if(requestType.equals(PreferenceList.HTTP_REQUEST_POST)){
            HTMLFileParser file = new HTMLFileParser(preferences.getPathToPOSTFile());
            file.parse();
            InputList is = file.getInputList();
            // TODO specify request URL in request line
            if(is.hasFiles()){
                headerList.addHeader(new Header(HEADER_CONTENT_TYPE,
                        "multipart/form-data; boundary=" + BOUNDARY_LINE));
                entityBody = getPostFormDataRequestBody(is);
            } else {
                headerList.addHeader(new Header(HEADER_CONTENT_TYPE,
                        "multipart/x-www-form-urlencoded"));
                entityBody = getPostUrlEncodedRequestBody(is);
            }
            headerList.addHeader(new Header(HEADER_CONTENT_LENGTH,
                        String.valueOf(entityBody.length())));
        }

        String request = getRequestLine()
                        + "\r\n"
                        + headerList.toString()
                        + "\r\n"
                        + entityBody;

        // TODO remove condition
        if(requestType.equals(PreferenceList.HTTP_REQUEST_POST)){
            System.out.println(request);
            throw new IOException("post request");
        }
        System.out.println(request);
        return request;
    }

    private String getFileContent(String fileName) throws IOException {
        FileInputStream fos = new FileInputStream(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while((b=fos.read())>-1){
            baos.write(b);
        }
        return baos.toString();
    }

    private String getPostUrlEncodedRequestBody(InputList is)
            throws IOException {
        String entityBody = "";
        int count = 0;
        for(Input input: is){
            String name = URLEncoder.encode(input.getName(), "UTF-8");
            String value = URLEncoder.encode(input.getValue(), "UTF-8");
            entityBody += (count++>0 ? "&" : "")
                    + name + "=" + value;
        }
        return entityBody;
    }

    private String getPostFormDataRequestBody(InputList is) throws IOException {
        String entityBody = "";
        
        for(Input input: is){
            entityBody += "--" + BOUNDARY_LINE + "\r\n"
                    + "Content-Disposition: form-data; name=\""
                    + input.getName() + "\"";
            if(input.getType().equals("file")){
                String fileName = getResourceNameFromURI(input.getValue());
                entityBody += "; filename=\"" + fileName + "\"\r\n"
                        + "Content-Transfer-Encoding: binary";
            }
            entityBody += "\r\n\r\n";
            if(input.getType().equals("file")){
                entityBody += getFileContent(input.getValue());
            } else {
                entityBody += input.getValue() + "\r\n";
            }
        }
        entityBody += "--" + BOUNDARY_LINE + "--\r\n";
        return entityBody;
    }

    private String getResourceNameFromURI(String path){
        path = path.replace("\\", "/");
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

    public String getResourceName(){
        return getResourceNameFromURI(uri.toString());
    }

    public void setAuthenticationParameters(String login, String password){
        this.login = login;
        this.password = password;
        useAuthentication = true;
    }

    public void write() throws IOException {
        out.write(getRequestString().getBytes());
        out.flush();
    }
}
