package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class BlackList extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7861290201689264052L;

	private List<Black> blackList = new ArrayList<Black>();
	
	private int total_number; 
	
	public BlackList() {}
	
	public BlackList(String json) throws WeiboParseException {
		parse(json);
	}
	
	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
        	JSONArray blackArray = obj.optJSONArray("users");
        	if(blackArray != null) {
	            int size = blackArray.length();
	            for (int i = 0; i < size; i++) {
	            	String black = blackArray.optString(i);
	            	if(!TextUtils.isEmpty(black)) {
	            		blackList.add(new Black(black));
	            	}
	            }
        	}
            total_number = obj.optInt("total_number");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
	}

	public List<Black> getBlackList() {
		return blackList;
	}

	public void setBlackList(List<Black> blackList) {
		this.blackList = blackList;
	}

	public int getTotal_number() {
		return total_number;
	}

	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        BlackList other = (BlackList) obj;

        if (total_number != other.getTotal_number()) {
            return false;
        }
        if (!blackList.equals(other.getBlackList())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + blackList.hashCode();
        result = prime * result + total_number;
        return result;
    }
	
}
