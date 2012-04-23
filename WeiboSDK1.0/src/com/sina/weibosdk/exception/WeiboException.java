package com.sina.weibosdk.exception;

/**
 * weibosdk中exception的父类
 * 
 * @author zhangqi
 * 
 */
public class WeiboException extends Exception {

    private static final long serialVersionUID = 7150272868136781718L;

    public WeiboException() {
        super();
    }

    public WeiboException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeiboException(String detailMessage) {
        super(detailMessage);
    }

    public WeiboException(Throwable throwable) {
        super(throwable);
    }

}
