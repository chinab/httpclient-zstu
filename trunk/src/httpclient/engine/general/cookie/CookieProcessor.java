package httpclient.engine.general.cookie;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CookieProcessor {

    private ArrayList<Cookie> cookies;
    private final String COOKIES_FILE_PATH = "cookies.dat";

    public CookieProcessor() throws IOException {
        cookies = new ArrayList<Cookie>();
    }

    /**
     * Replaces current cookie list with cookie list from file.
     * @throws IOException
     */
    public void loadFromFile() throws IOException{
        cookies.clear();
        File cookieFile = new File(COOKIES_FILE_PATH);
        if(!cookieFile.exists()){
            return;
        }
        Scanner scanner = new Scanner(cookieFile);
        CookieParser parser = new CookieParser();
        String cookieLine;
        Cookie cookie;
        while(scanner.hasNextLine()){
            cookieLine = scanner.nextLine();
            cookie = parser.parseLine(cookieLine);
            if(cookie == null){
                throw new IOException("Can't parse cookie from file!");
            } else {
                cookies.add(cookie);
            }
        }
    }

    /**
     * Clears all expired and temporary cookies
     * and writes cookie list to file.
     * @throws IOException
     */
    public void saveToFile() throws IOException{
        File cookieFile = new File(COOKIES_FILE_PATH);
        if(!cookieFile.exists()){
            cookieFile.createNewFile();
        }
        clearAllExpired();
        FileWriter fw = new FileWriter(cookieFile);
        for(Cookie cookie: cookies){
             fw.write(cookie.toString() + "\r\n");
        }
        fw.flush();
    }

    public ArrayList<Cookie> getCookies(String domain, String path) {
        System.out.println(domain+path);
        ArrayList<Cookie> matchedCookies = new ArrayList<Cookie>();
        for(Cookie cookie: cookies){
            if(domain.endsWith(cookie.getDomain())
                    && path.startsWith(cookie.getPath())){
                matchedCookies.add(cookie);
                System.out.println(" > "+ cookie);
            }
        }
        return matchedCookies;
    }

   public void setCookies(ArrayList<Cookie> newCookies) throws IOException {
        boolean foundEqual;
        if(cookies.size()>0){
            for(Cookie cookie: newCookies){
                foundEqual = false;
                for(int i=0; i<cookies.size(); i++){
                    if(Cookie.isEqual(cookie, cookies.get(i))){
                        cookies.set(i, cookie);
                        foundEqual = true;
                        break;
                    }
                }
                if(!foundEqual) cookies.add(cookie);
            }
        } else {
            cookies.addAll(newCookies);
        }
    }
   
    public int clearAllExpired(){
        return 0;
    }
}
