package httpclient.engine.general.cookie;

import java.io.IOException;

public class CookieParser {

    public Cookie parse(String headerValue) throws IOException {
        String[] attributes = headerValue.split(";");
        String cookieName = "";
        String cookieValue = "";
        String expires = "";
        String domain = "";
        String maxAge = "";
        String path = "";
        boolean secure = false;
        String name, value;
        String[] temp;
        temp = attributes[0].split("=", 2);
        cookieName = temp[0];
        cookieValue = temp[1];
        for(int i=1; i<attributes.length; i++){
            temp = attributes[i].split("=", 2);
            name = temp[0].trim();
            if(temp.length>1){
                value = temp[1].trim();
            } else {
                value = "";
            }
            if(name.equalsIgnoreCase(Cookie.COOKIE_EXPIRES)){
                expires = value;
            } else
            if(name.equalsIgnoreCase(Cookie.COOKIE_DOMAIN)){
                domain = value;
            } else
            if(name.equalsIgnoreCase(Cookie.COOKIE_MAX_AGE)){
                maxAge = value;
            } else
            if(name.equalsIgnoreCase(Cookie.COOKIE_PATH)){
                path = value;
            } else
            if(name.equalsIgnoreCase(Cookie.COOKIE_SECURE)){
                secure = true;
            }
        }
        if(!cookieName.isEmpty() && !cookieValue.isEmpty()){
            return new Cookie(cookieName, cookieValue, expires, maxAge, path,
                    domain, secure);
        } else {
            throw new IOException("can't parse cookie!");
        }
    }
}
