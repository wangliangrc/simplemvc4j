package com.sina.weibo.exception;

/**
 * Thrown when there were problems parsing the response to an API call, either
 * because the response was empty, or it was malformed.
 */
public class WeiboParseException extends Exception {

    private static final long serialVersionUID = 3132128578218204998L;

    public WeiboParseException() {
        super();
    }

    public WeiboParseException(String detailMessage) {
        super(detailMessage);
    }

    public WeiboParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeiboParseException(Throwable throwable) {
        super(throwable);
    }

}
