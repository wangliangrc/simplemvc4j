package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Black extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4454639937331682678L;

	private UserInfo userInfo;
	/**
	 * 加入黑名单的时间
	 */
	private String created_at;
	
	public Black() {}
	
	public Black(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            String user = obj.optString("user");
            if(!TextUtils.isEmpty(user)) {
            	this.userInfo = new UserInfo(user);
            }
            this.created_at = obj.optString("created_at");
            
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Black other = (Black) obj;
        if (!userInfo.getId().equals(
        		other.userInfo.getId())) {
        	return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + userInfo.hashCode();
        return result;
    }
}
