package httpclient.engine;

public class PreferenceList {

    public static final String HTTP_REQUEST_GET = "GET";
    public static final String HTTP_REQUEST_POST = "POST";
    public static final String HTTP_REQUEST_HEAD = "HEAD";

    private final String defaultPath = "index.html";

    private String pathToPOSTFile = defaultPath;
    private String requestType = HTTP_REQUEST_GET;

    public void setPathToPOSTFile(String path){
        pathToPOSTFile = path;
    }
    public void setRequestType(String type){
        requestType = type;
    }
    public String getRequestType(){
        return requestType;
    }
    public String getPathToPOSTFile(){
        return pathToPOSTFile;
    }
}
