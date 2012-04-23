package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Visible extends ResponseBean {

    private static final long serialVersionUID = 8850557261875682569L;

    private int type;

    private int list_id;

    public Visible() {
	}

	public Visible(String json) throws WeiboParseException {
		parse(json);
	}

    @Override
    protected Visible parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            type = obj.optInt("type");
            list_id = obj.optInt("list_id");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

}