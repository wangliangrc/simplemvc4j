package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class GroupArray extends ResponseBean {

	private static final long serialVersionUID = 878951004495822347L;

	private List<GroupList> list = new ArrayList<GroupList>(10);

	public GroupArray() {}
	
	public GroupArray(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected GroupArray parse(String json) throws WeiboParseException {
		try {
			JSONArray array = new JSONArray(json);
			if (null != array) {
				int size = array.length();
				for (int i = 0; i < size; i++) {
					list.add(new GroupList(array.getString(i)));
				}
			}
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	public List<GroupList> getList() {
		return list;
	}

	public void setList(ArrayList<GroupList> list) {
		this.list = list;
	}

}
