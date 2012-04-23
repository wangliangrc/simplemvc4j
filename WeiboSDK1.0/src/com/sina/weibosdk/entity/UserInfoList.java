package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class UserInfoList extends ResponseBean {

	private static final long serialVersionUID = -1559403736674644944L;

	/**
	 * 返回的用户列表
	 */
	private List<UserInfo> users = new ArrayList<UserInfo>(10);

	private boolean hasvisible;

	private String previous_cursor;

	private String next_cursor;

	/**
	 * 返回的用户总数
	 */
	private int total_number;

	public UserInfoList() {}
	
	public UserInfoList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected UserInfoList parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray stausArray = obj.optJSONArray("users");
			int size = stausArray.length();
			for (int i = 0; i < size; i++) {
				users.add(new UserInfo(stausArray.getString(i)));
			}
			this.hasvisible = obj.optBoolean("hasvisible");
			this.previous_cursor = obj.optString("previous_cursor");
			this.next_cursor = obj.optString("next_cursor");
			this.total_number = obj.optInt("total_number");
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	public List<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<UserInfo> users) {
		this.users = users;
	}

	public boolean isHasvisible() {
		return hasvisible;
	}

	public void setHasvisible(boolean hasvisible) {
		this.hasvisible = hasvisible;
	}

	public String getPrevious_cursor() {
		return previous_cursor;
	}

	public void setPrevious_cursor(String previous_cursor) {
		this.previous_cursor = previous_cursor;
	}

	public String getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(String next_cursor) {
		this.next_cursor = next_cursor;
	}

	public int getTotalNumber() {
		return total_number;
	}

	public void SetTotalNumber(int total_number) {
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

		UserInfoList other = (UserInfoList) obj;

		if (!users.equals(other.getUsers())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + users.hashCode();

		return result;
	}

}
