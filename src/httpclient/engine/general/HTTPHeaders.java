package httpclient.engine.general;

public interface HTTPHeaders {
    // server headers
    public static final String HEADER_MIME_VERSION = "mime-version";
    public static final String HEADER_TRANSFER_ENCODING = "transfer-encoding";
    public static final String HEADER_UPGRADE = "upgrade";
    public static final String HEADER_ACCEPT_RANGE = "accept_range";
    public static final String HEADER_AGE = "age";
    public static final String HEADER_LOCATION = "location";
    public static final String HEADER_PROXY_AUTHENTICATE = "proxy_authenticate";
    public static final String HEADER_RETRY_AFTER = "retry_after";
    public static final String HEADER_SERVER = "server";
    public static final String HEADER_WWW_AUTHENTICATE = "www-authenticate";
    public static final String HEADER_SET_COOKIE = "set-cookie";
    // client headers
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_USER_AGENT = "User-Agent";
    // common headers
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";

}
