package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.exception.WeiboParseException;

public class GroupUserList extends ResponseBean {

	private static final long serialVersionUID = -8601330709170961605L;

	private UserInfoList users;
	private GroupList groups;

	public GroupUserList() {}
	
	public GroupUserList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected GroupUserList parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			this.users = new UserInfoList(obj.optString("userlist"));
			this.groups = new GroupList(obj.optString("grouplist"));
		} catch (JSONException e) {
			e.printStackTrace();
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	public UserInfoList getUsers() {
		return users;
	}

	public void setUsers(UserInfoList users) {
		this.users = users;
	}

	public GroupList getGroups() {
		return groups;
	}

	public void setGroups(GroupList groups) {
		this.groups = groups;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		GroupUserList other = (GroupUserList) obj;

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
