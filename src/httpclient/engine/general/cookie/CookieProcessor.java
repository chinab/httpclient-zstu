package httpclient.engine.general.cookie;

import httpclient.engine.general.HeaderList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CookieProcessor {

    public CookieProcessor(HeaderList headerList){

    }

    public ArrayList<Cookie> getCookies(String domain, String path) throws
        FileNotFoundException {

        return null;
    }

    public int clearAllExpired(){
        return 0;
    }

    public boolean hasExpired(Cookie cookie){
        return false;
    }

    public void setCookies(ArrayList<Cookie> cookies) throws
        IOException {
        if(!cookies.isEmpty()){
            File cookieFile = new File("cookies.dat");
            if(!cookieFile.exists()){
                cookieFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(cookieFile, true);
            
            for(Cookie cookie: cookies){

            }

        } else {
            System.out.println("no cookies from server!");
        }
    }
}
