package com.sina.weibo.exception;

import com.sina.weibo.models.ErrorMessage;

/**
 * Thrown when there were problems parsing the response to an API call, either
 * because the response was empty, or it was malformed.
 */
public class WeiboApiException extends Exception {

    private static final long serialVersionUID = -5143101071713313135L;

    // 错误信息
    private ErrorMessage mErrMessage;

    public ErrorMessage getErrMessage() {
        return mErrMessage;
    }

    public WeiboApiException() {
        super();
    }

    public WeiboApiException(String detailMessage) {
        super(detailMessage);
    }

    public WeiboApiException(ErrorMessage err) {
        super("Error Code:" + err.errno + ",Reason:" + err.errmsg);
        mErrMessage = err;
    }

    public WeiboApiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeiboApiException(Throwable throwable) {
        super(throwable);
    }

    public boolean isWrongPassword() {
        if (mErrMessage != null) {
            return mErrMessage.isWrongPassword();
        } else {
            return false;
        }
    }

}
