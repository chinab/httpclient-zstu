package httpclient.engine.response;

public class StartingLine{
    private int minorVersion;
    private int majorVersion;
    private int statusCode;
    private String reasonPhrase;

    StartingLine(int major, int minor, int status, String reason){
        minorVersion = minor;
        majorVersion = major;
        statusCode = status;
        reasonPhrase = reason;
    }
    
    public int getMinorVersion(){
        return minorVersion;
    }
    public int getMajorVersion(){
        return majorVersion;
    }
    public int getStatusCode(){
        return statusCode;
    }
    public String getReasonPhrase(){
        return reasonPhrase;
    }
}
