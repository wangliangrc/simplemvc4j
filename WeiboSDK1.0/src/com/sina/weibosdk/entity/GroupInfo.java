package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.exception.WeiboParseException;

public class GroupInfo extends ResponseBean {

	private static final long serialVersionUID = -3923912362855442092L;

	private int subscriber_count;
	private String description;
	private String full_name;
	private int member_count;
	private String mode;
	private UserInfo user;
	private String slug;
	private String uri;
	private long id;
	private String name;

	public GroupInfo() {}
	
	public GroupInfo(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected GroupInfo parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			this.subscriber_count = obj.optInt("subscriber_count");
			this.description = obj.optString("description");
			this.full_name = obj.optString("full_name");
			this.member_count = obj.optInt("member_count");
			this.mode = obj.optString("mode");
			String u = obj.optString("user");
			if (!TextUtils.isEmpty(u)) {
				this.user = new UserInfo(u);
			}
			this.slug = obj.optString("slug");
			this.uri = obj.optString("uri");
			this.id = obj.optLong("id");
			this.name = obj.optString("name");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	public int getSubscriberCount() {
		return subscriber_count;
	}

	public void setSubscriberCount(int subscriber_count) {
		this.subscriber_count = subscriber_count;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFullName() {
		return full_name;
	}

	public void setFullName(String full_name) {
		this.full_name = full_name;
	}

	public int getMemberCount() {
		return member_count;
	}

	public void setMemberCount(int member_count) {
		this.member_count = member_count;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		GroupInfo other = (GroupInfo) obj;

		if (!user.equals(other.getUser())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + user.hashCode();

		return result;
	}

}
