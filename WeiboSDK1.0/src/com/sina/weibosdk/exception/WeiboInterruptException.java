package com.sina.weibosdk.exception;

public class WeiboInterruptException extends WeiboException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4456350392210563900L;

    public static final String INTERRUPT_ERROR = "task has be canceled !"; 
    
    public WeiboInterruptException() {
        super();
    }

    public WeiboInterruptException(String detailMessage) {
        super(detailMessage);
    }

    public WeiboInterruptException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeiboInterruptException(Throwable throwable) {
        super(throwable);
    }

}
