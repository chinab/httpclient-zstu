package httpclient.engine.general;

public class MIMETypes {

    public static final String MIME_HTML = "text/html";
    public static final String MIME_CSS = "text/css";
    public static final String MIME_TXT = "text/plain";
    public static final String MIME_URL = "application/x-www-form-urlencoded";
    public static final String MIME_FILE_DATA = "multipart/form-data";

    public static boolean isTextType(String type){
        if(type.startsWith(MIME_HTML)
                || type.startsWith(MIME_CSS)
                || type.startsWith(MIME_TXT)){
            return true;
        }
        return false;
    }
}
