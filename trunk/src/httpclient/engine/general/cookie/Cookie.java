package httpclient.engine.general.cookie;

public class Cookie {

    public static final String COOKIE_EXPIRES = "Expires";
    public static final String COOKIE_DOMAIN = "Domain";
    public static final String COOKIE_MAX_AGE = "Max-Age";
    public static final String COOKIE_PATH = "Path";
    public static final String COOKIE_SECURE = "Secure";
    //public static final String COOKIE_VERSION = "Version";

    private String name;
    private String value;
    private String expires;
    private String maxAge;
    private String path;
    private String domain;
    private boolean secure;

    public Cookie(String name, String value, String expires, String maxAge, 
            String path, String domain, boolean secure){
        this.domain = domain;
        this.expires = expires;
        this.name = name;
        this.maxAge = maxAge;
        this.path = path;
        this.secure = secure;
        this.value = value;
    }
    
    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }
    public String getExpires(){
        return expires;
    }
    public String getMaxAge(){
        return maxAge;
    }
    public String getPath(){
        return path;
    }
    public String getDomain(){
        return domain;
    }
    public boolean isSecure(){
        return secure;
    }
}
