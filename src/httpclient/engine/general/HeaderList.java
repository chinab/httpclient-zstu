package httpclient.engine.general;

import java.util.ArrayList;
import java.util.HashMap;

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
    public ArrayList<String> getCookies(){
        ArrayList<String> cookies = new ArrayList<String>();
        for(Header header: headers){
            if(header.getName().equalsIgnoreCase(HTTPHeaders.HEADER_SET_COOKIE)){
                cookies.add(header.getValue());
            }
        }
        return cookies;
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