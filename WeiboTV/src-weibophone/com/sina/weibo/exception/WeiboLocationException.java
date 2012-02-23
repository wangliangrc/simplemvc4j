package com.sina.weibo.exception;

/**
 * Thrown when there were problems parsing the response to an API call, either
 * because the response was empty, or it was malformed.
 */
public class WeiboLocationException extends Exception {

    private static final long serialVersionUID = 7315480381828283190L;

    public WeiboLocationException() {
        super();
    }

    public WeiboLocationException(String detailMessage) {
        super(detailMessage);
    }

    public WeiboLocationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeiboLocationException(Throwable throwable) {
        super(throwable);
    }

}
