package com.sina.weibosdk.exception;

import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.entity.ErrorMessage;
import com.sina.weibosdk.entity.VerifyCode;

/**
 * Thrown when there were problems parsing the response to an API call, either
 * because the response was empty, or it was malformed.
 */
public class WeiboApiException extends WeiboException {

    private static final long serialVersionUID = -4385856054404960519L;

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
        super("Error Code:" + err.getErrno() + ",Reason:" + err.getErrmsg());
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
            return WeiboSDKConfig.ERROR_PASSWORD_WRONG.equals(mErrMessage.getErrno());
        } else {
            return false;
        }
    }

    /**
     * 是否需要验证码
     * 
     * @return
     */
    public boolean isNeedVerifiyCode() {
        if (mErrMessage != null && mErrMessage.getVerifyCode() != null) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * 返回验证码, 如果没有返回 null
     * 
     * @return
     */
    public VerifyCode getVerifiyCode() {
        if (mErrMessage != null) {
            return mErrMessage.getVerifyCode();
        } else {
            return null;
        }
    }

}
