package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class UserInfo extends ResponseBean {

	private static final long serialVersionUID = -8321062108927910402L;

	/**
	 * id
	 */
	private String id;

	/**
	 * 昵称
	 */
	private String screen_name;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 头像url
	 */
	private String profile_image_url;

	/**
	 * 粉丝数
	 */
	private int followers_count;

	/**
	 * 用户是否认证
	 */
	private boolean verified;

	/**
	 * 用户认证种类，-1为非认证用户
	 */
	private int verified_type;

	/**
	 * 作用未知，难道是达人等级？？？
	 */
	private int level;

	/**
	 * 用户所在省份ID
	 */
	private String province;

	/**
	 * 用户所在城市ID
	 */
	private String city;

	/**
	 * 用户所在城市(例如:北京)
	 */
	private String location;
	
	/**
	 * 关注数
	 */
	private int friends_count;

	/**
	 * 微博数
	 */
	private int statuses_count;

	/**
	 * 收藏数
	 */
	private int favourites_count;

	/**
	 * 创建时间
	 */
	private String created_at;

	/**
	 * 最近的一条微博
	 */
	private Status status;

	/**
	 * 个人描述信息
	 */
	private String description;

	/**
	 * 友好显示名称
	 */
	private String name;
	
	/**
	 * 未知？？？
	 */
	private String nickname;

	/**
	 * 用户的个性化域名
	 */
	private String domain;

	/**
	 * 当前登录用户是否已关注该用户
	 */
	private boolean following;

	/**
	 * 是否允许所有人给我发私信
	 */
	private boolean allow_all_act_msg;

	/**
	 * 
	 */
	private String remark;

	/**
	 * 是否允许带有地理信息
	 */
	private boolean geo_enabled;

	/**
	 * 是否允许所有人对我的微博进行评论
	 */
	private boolean allow_all_comment;

	/**
	 * 用户大头像地址
	 */
	private String avatar_large;

	/**
	 * 认证原因
	 */
	private String verified_reason;

	/**
	 * 该用户是否关注当前登录用户
	 */
	private boolean follow_me;

	/**
	 * 用户的在线状态，0：不在线、1：在线
	 */
	private int online_status;

	/**
	 * 
	 */
	private String status_id;

	/**
	 * 用户的互粉数
	 */
	private int bi_followers_count;

	/**
	 * 语言
	 */
	private String lang;
	
	/**
	 * 分组ID(如果一个用户可以对应多个分组)
	 */
	private ArrayList<String> gids = new ArrayList<String>(); 
	
	private int distance;
	
	public UserInfo() {
	}

	public UserInfo(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected UserInfo parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			this.id = obj.optString("id");
			this.screen_name = obj.optString("screen_name");
			this.gender = obj.optString("gender");
			this.profile_image_url = obj.optString("profile_image_url");
			this.followers_count = obj.optInt("followers_count");
			this.verified = obj.optBoolean("verified");
			this.verified_type = obj.optInt("verified_type");
			this.level = obj.optInt("level");
			this.province = obj.optString("province");
			this.city = obj.optString("city");
			this.location = obj.optString("location");
			this.friends_count = obj.optInt("friends_count");
			this.statuses_count = obj.optInt("statuses_count");
			this.favourites_count = obj.optInt("favourites_count");
			this.created_at = obj.optString("created_at");
			String statusJson = obj.optString("status");
			if (!TextUtils.isEmpty(statusJson)) {
				this.status = new Status(statusJson);
			}
			this.description = obj.optString("description");
			this.name = obj.optString("name");
			this.nickname = obj.optString("nickname");
			this.domain = obj.optString("domain");
			this.following = obj.optBoolean("following");
			this.allow_all_act_msg = obj.optBoolean("allow_all_act_msg");
			this.remark = obj.optString("remark");
			this.geo_enabled = obj.optBoolean("geo_enabled");
			this.allow_all_comment = obj.optBoolean("allow_all_comment");
			this.avatar_large = obj.optString("avatar_large;");
			this.verified_reason = obj.optString("verified_reason");
			this.follow_me = obj.optBoolean("follow_me");
			this.online_status = obj.optInt("online_status");
			this.status_id = obj.optString("status_id");
			this.bi_followers_count = obj.optInt("bi_followers_count");
			this.lang = obj.optString("lang");
			this.distance = obj.optInt("distance");
			
			String gidStr = obj.optString("gidstr");
			if (!TextUtils.isEmpty(gidStr)) {
				gids.addAll(Arrays.asList(gidStr.split(",")));
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

	public String getScreenName() {
		return screen_name;
	}

	public void setScreenName(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProfileImageUrl() {
		return profile_image_url;
	}

	public void setProfileImageUrl(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public int getFollowersCount() {
		return followers_count;
	}

	public void setFollowersCount(int followers_count) {
		this.followers_count = followers_count;
	}

	public boolean getVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerifiedType() {
		return verified_type;
	}

	public void setVerifiedType(int verified_type) {
		this.verified_type = verified_type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getOnlineStatus() {
		return online_status;
	}

	public void setOnlineStatus(int online_status) {
		this.online_status = online_status;
	}

	public int getBiFollowersCount() {
		return bi_followers_count;
	}

	public void setBiFollowersCount(int bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getFriendsCount() {
		return friends_count;
	}

	public void setFriendsCount(int friends_count) {
		this.friends_count = friends_count;
	}

	public int getStatusesCount() {
		return statuses_count;
	}

	public void setStatusesCount(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public int getFavouritesCount() {
		return favourites_count;
	}

	public void setFavouritesCount(int favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNickName() {
		return nickname;
	}
	
	public void setNickName(String nickname) {
		this.nickname = nickname;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAvatarLarge() {
		return avatar_large;
	}

	public void setAvatarLarge(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public String getVerifiedReason() {
		return verified_reason;
	}

	public void setVerifiedReason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public boolean getFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean getAllowAllActMsg() {
		return allow_all_act_msg;
	}

	public void setAllowAllActMsg(boolean allow_all_act_msg) {
		this.allow_all_act_msg = allow_all_act_msg;
	}

	public boolean getGeoEnabled() {
		return geo_enabled;
	}

	public void setGeoEnabled(boolean geo_enabled) {
		this.geo_enabled = geo_enabled;
	}

	public boolean getAllowAllComment() {
		return allow_all_comment;
	}

	public void setAllowAllComment(boolean allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}

	public boolean getFollowMe() {
		return follow_me;
	}

	public void setFollowMe(boolean follow_me) {
		this.follow_me = follow_me;
	}

	public String getStatusId() {
		return status_id;
	}

	public void setStatusId(String status_id) {
		this.status_id = status_id;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
    public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public ArrayList<String> getGids() {
		return gids;
	}

	public void setGids(ArrayList<String> gids) {
		this.gids = gids;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        UserInfo other = (UserInfo) obj;

        if (!id.equals(other.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();

        return result;
    }
	
}
