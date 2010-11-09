package httpclient.engine.general;

import httpclient.engine.general.cookie.Cookie;
import httpclient.engine.general.cookie.CookieParser;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains header names and values of HTTP request.<br>
 * Cookie values are stored in separate list and should be accessed
 * through method <b>getCookies()</b>.
 */
public class HeaderList {

    private ArrayList<Header> headers;

    public HeaderList(){
        headers = new ArrayList<Header>();
    }

    public void addHeader(Header h){
        headers.add(h);
    }

    public boolean hasHeader(String name){
        for(Header header: headers){
            if(header.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public Header getHeader(String name){
        for(Header header: headers){
            if(header.getName().equalsIgnoreCase(name)){
                return header;
            }
        }
        return null;
    }

    public Header[] getAllHeaders(){
        Header[] h = new Header[headers.size()];
        for(int i=0;i<headers.size(); i++){
            h[i] = headers.get(i);
        }
        return h;
    }

    /**
     * Gets cookie list.
     * @return List of cookies;
     */
    public ArrayList<Cookie> getCookies() throws IOException{
        ArrayList<Cookie> cookies = new ArrayList<Cookie>();
        for(Header header: headers){
            if(header.getName().equalsIgnoreCase(HTTPHeaders.HEADER_SET_COOKIE)){
                CookieParser parser = new CookieParser();
                cookies.add(parser.parse(header.getValue()));
            }
        }
        return cookies;
    }

    public String getCharset(){
        if(this.hasHeader(HTTPHeaders.HEADER_CONTENT_TYPE)){
            Header header = this.getHeader(HTTPHeaders.HEADER_CONTENT_TYPE);
            String[] attributes = header.getValue().split(";");
            for(int i=0; i<attributes.length; i++){
                String nameValue[] = attributes[i].split("=", 2);
                if(nameValue[0].trim().equalsIgnoreCase("charset")){
                    return nameValue[1];
                }
            }
        }
        return "";
    }

    @Override
    public String toString(){
        String result = "";
        for(Header h: getAllHeaders()){
            result += h.getName() + ": " + h.getValue() + "\r\n";
        }
        return result;
    }
}
