package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Group extends ResponseBean {

	private static final long serialVersionUID = 8687268842900715253L;

	private String id;
	private String idstr;
	private String name;
	private String mode;
	private int visible;
	private int like_count;
	private int member_count;
	private String description;
	private List<String> tags = new ArrayList<String>(10);
	private String profile_image_url;
	private String created_at;
	private UserInfo user;

	public Group() {}
	
	public Group(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected Group parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			this.id = obj.optString("id");
			this.idstr = obj.optString("idstr");
			this.name = obj.optString("name");
			this.mode = obj.optString("mode");
			this.visible = obj.optInt("visible");
			this.like_count = obj.optInt("like_count");
			this.member_count = obj.optInt("member_count");
			this.description = obj.optString("description");
			String groupTags = obj.optString("tags");
			if (null != groupTags && groupTags.trim().length() > 0) {
				JSONArray array = new JSONArray(groupTags);
				if (null != array) {
					int size = array.length();
					for (int i = 0; i < size; i++) {
						tags.add(array.getString(i));
					}
				}
			}
			this.profile_image_url = obj.optString("profile_image_url");
			this.created_at = obj.optString("created_at");
			String u = obj.optString("user");
			if (!TextUtils.isEmpty(u)) {
				this.user = new UserInfo(u);
			}
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdStr() {
		return idstr;
	}

	public void setIdStr(String idstr) {
		this.idstr = idstr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getLikeCount() {
		return like_count;
	}

	public void setLikeCount(int like_count) {
		this.like_count = like_count;
	}

	public int getMemberCount() {
		return member_count;
	}

	public void setMemberCount(int member_count) {
		this.member_count = member_count;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfileImageUrl() {
		return profile_image_url;
	}

	public void setProfileImageUrl(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Group other = (Group) obj;

        if (!idstr.equals(other.getIdStr())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idstr.hashCode();

        return result;
    }

}
