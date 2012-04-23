package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.exception.WeiboParseException;

public class ErrorMessage extends ResponseBean {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4719031342977962885L;
	/**
     * 错误码
     */
    private String errno;
    /**
     * 错误信息
     */
    private String errmsg;
    /**
     * 接口返回的验证码信息
     */
    private VerifyCode verifyCode;
    
    public ErrorMessage() {
    }

    public ErrorMessage(String json) throws WeiboParseException {
        parse(json);
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public VerifyCode getVerifyCode() {
		return verifyCode;
	}

	@Override
    protected ErrorMessage parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            this.errno = obj.optString("errno");
            this.errmsg = obj.optString("errmsg");
            /**
             * 是否包含返回的验证码信息
             */
            if(WeiboSDKConfig.ERROR_VERIFICATION_CODE_WRONG
            		.equals(errno)) {
            	String annotations = obj.optString("annotations");
            	verifyCode = new VerifyCode(annotations);
            }
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);

        }
        return this;
    }

}
