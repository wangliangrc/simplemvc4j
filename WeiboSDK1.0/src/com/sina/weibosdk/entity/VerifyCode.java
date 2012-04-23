package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class VerifyCode extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7830161394566951631L;

	private String cpt;
	private String pic;
	private String q;
	
	public VerifyCode() {}
	
	public VerifyCode(String json) throws WeiboParseException {
        parse(json);
    }
	
	public String getCpt() {
		return cpt;
	}

	public void setCpt(String cpt) {
		this.cpt = cpt;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            cpt = obj.optString("cpt");
            pic = obj.optString("pic");
            q = obj.optString("q");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
	}

	
	
}
