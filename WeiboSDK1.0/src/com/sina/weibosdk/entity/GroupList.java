package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class GroupList extends ResponseBean {

	private static final long serialVersionUID = 6007439552778443293L;

	private String uid;
	private List<Group> lists = new ArrayList<Group>(10);
	private int total_number;

	public GroupList() {}
	
	public GroupList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected GroupList parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			this.uid = obj.optString("uid");
			this.total_number = obj.optInt("total_number");
			JSONArray array = new JSONArray(obj.optString("lists"));
			if (null != array) {
				int size = array.length();
				for (int i = 0; i < size; i++) {
					lists.add(new Group(array.getString(i)));
				}
			}
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	public List<Group> getLists() {
		return lists;
	}

	public void setLists(ArrayList<Group> lists) {
		this.lists = lists;
	}

	public int getTotalNumber() {
		return total_number;
	}

	public void setTotalNumber(int total_number) {
		this.total_number = total_number;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		GroupList other = (GroupList) obj;

		if (!lists.equals(other.getLists())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lists.hashCode();

		return result;
	}
	
}
