package com.sina.openapi;

public class WeiboOpenAPIException extends Exception {
    private static final long serialVersionUID = 1720252751234195605L;
    private int statusCode = -1;

    public WeiboOpenAPIException(String msg) {
        super(msg);
    }

    public WeiboOpenAPIException(Exception cause) {
        super(cause);
    }

    public WeiboOpenAPIException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;

    }

    public WeiboOpenAPIException(String msg, Exception cause) {
        super(msg, cause);
    }

    public WeiboOpenAPIException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;

    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
