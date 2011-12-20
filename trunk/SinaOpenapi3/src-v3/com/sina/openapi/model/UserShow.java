package com.sina.openapi.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class UserShow implements Serializable, Cloneable, Comparable<UserShow> {

    private static final long serialVersionUID = 4872305119211958949L;

    private Status status;
    /**
     * 用户UID
     */
    private String id;
    /**
     * 微博昵称
     */
    private String screen_name;
    /**
     * 友好显示名称，如Bill Gates(此特性暂不支持)
     */
    private String name;
    /**
     * 省份编码（参考省份编码表）
     */
    private String province;
    /**
     * 城市编码（参考城市编码表）
     */
    private String city;
    /**
     * 地址
     */
    private String location;
    /**
     * 个人描述
     */
    private String description;
    /**
     * 用户博客地址
     */
    private String url;
    /**
     * 自定义头像
     */
    private String profile_image_url;
    /**
     * 用户个性化URL
     */
    private String domain;
    /**
     * 性别,1--m--男，2--f--女,0--n--未知
     */
    private String gender;
    /**
     * 粉丝数
     */
    private int followers_count;
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
     * 是否已关注(此特性暂不支持)
     */
    private boolean following;

    private boolean allow_all_act_msg;

    private boolean geo_enabled;

    /**
     * 加V标示，是否微博认证用户
     */
    private boolean verified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public Date getCreated_at() throws ParseException {
        return Utils.parserWeiboDate(created_at);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isAllow_all_act_msg() {
        return allow_all_act_msg;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserShow [status=").append(status).append(", id=")
                .append(id).append(", screen_name=").append(screen_name)
                .append(", name=").append(name).append(", province=")
                .append(province).append(", city=").append(city)
                .append(", location=").append(location)
                .append(", description=").append(description).append(", url=")
                .append(url).append(", profile_image_url=")
                .append(profile_image_url).append(", domain=").append(domain)
                .append(", gender=").append(gender)
                .append(", followers_count=").append(followers_count)
                .append(", friends_count=").append(friends_count)
                .append(", statuses_count=").append(statuses_count)
                .append(", favourites_count=").append(favourites_count)
                .append(", created_at=").append(created_at)
                .append(", following=").append(following)
                .append(", allow_all_act_msg=").append(allow_all_act_msg)
                .append(", geo_enabled=").append(geo_enabled)
                .append(", verified=").append(verified).append("]\n")
                .append(super.toString());
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserShow other = (UserShow) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(UserShow o) {
        if (!equals(o)) {
            try {
                return getCreated_at().compareTo(o.getCreated_at()) > 0 ? -1
                        : 1;
            } catch (ParseException e) {
            }
        }
        return 0;
    }

    @Override
    public Object clone() {
        try {
            UserShow clone = (UserShow) super.clone();
            clone.status = (Status) status.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }
}
