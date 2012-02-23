package com.sina.weibo.exception;

/**
 * Thrown when there were problems parsing the response to an API call, either
 * because the response was empty, or it was malformed.
 */
public class WeiboIOException extends Exception {

    public static final String REASON_HTTPCLIENT = "Fail to Init HttpClient";
    public static final String REASON_SERVER = "Server Error:";
    public static final String REASON_HTTP_METHOD = "Invalid HTTP method";
    public static final String REASON_POST_PARAM = "Unsupported Encoding Exception";

    private static final long serialVersionUID = 7729676731472012868L;

    public WeiboIOException() {
        super();
    }

    public WeiboIOException(String detailMessage) {
        super(detailMessage);
    }

    public WeiboIOException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeiboIOException(Throwable throwable) {
        super(throwable);
    }

}
