package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Result extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7117211958325320836L;

	private int result;
	
	public Result(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			String r = obj.optString("result");
			if(!TextUtils.isEmpty(r)) {
				if(r.equals("true") || r.equals("1")) {
					result = 1;
				}
			}
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
		return this;
	}

	public boolean isSuccessed() {
		return result == 1 ? 
				true : false;
	}

}
