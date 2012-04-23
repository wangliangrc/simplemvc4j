package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class AtUsers extends ResponseBean {

	private static final long serialVersionUID = -4328694918350651445L;

	/**
	 * 返回的用户列表
	 */
	private List<UserInfo> users = new ArrayList<UserInfo>(10);

	public AtUsers() {}
	
	public AtUsers(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected AtUsers parse(String json) throws WeiboParseException {
		try {
			JSONArray array = new JSONArray(json);
			int size = array.length();
			for (int i = 0; i < size; i++) {
				users.add(new UserInfo(array.getString(i)));
			}
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		AtUsers other = (AtUsers) obj;

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
