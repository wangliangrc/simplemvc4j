package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 微博结构体
 * 
 * @author zhangqi
 * 
 */
public class Status extends ResponseBean {

    private static final long serialVersionUID = -3073873686983157651L;

    /**
     * 生成时间
     */
    private String created_at;

    /**
     * 微博id
     */
    private String id;

    /**
     * 微博MID
     */
    private String mid;

    /**
     * 字符串型的微博ID
     */
    private String idstr;

    /**
     * 微博信息内容
     */
    private String text;

    /**
     * 微博来源
     */
    private String source;

    /**
     * 是否已收藏
     */
    private boolean favorited;

    /**
     * 是否被截断
     */
    private boolean truncated;

    /**
     * 回复ID
     */
    private String in_reply_to_status_id;

    /**
     * 回复人UID
     */
    private String in_reply_to_user_id;

    /**
     * 回复人昵称
     */
    private String in_reply_to_screen_name;

    /**
     * 缩略图片地址(小图)
     */
    private String thumbnail_pic;

    /**
     * bmiddle_pic(中图)
     */
    private String bmiddle_pic;

    /**
     * 原始图片地址(原图)
     */
    private String original_pic;

    /**
     * 地理位置信息
     */
    private Geo geo;

    /**
     * 微博作者的用户信息字段
     */
    private UserInfo user;

    /**
     * 转发博文。该字段不一定有
     */
    private Status retweeted_status;

    /**
     * 转发次数
     */
    private int reposts_count;

    /**
     * 评论次数
     */
    private int comments_count;

    /**
     * 
     */
    private int mlevel;

    /**
     * 
     */
    private Visible visible;
    
    private int distance;
    
    public Status() {
	}

	public Status(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected Status parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            created_at = obj.optString("created_at");
            id = obj.optString("id");
            mid = obj.optString("mid");
            idstr = obj.optString("idstr");
            text = obj.optString("text");
            source = obj.optString("source");
            favorited = obj.optBoolean("favorited");
            truncated = obj.optBoolean("truncated");
            in_reply_to_status_id = obj.optString("in_reply_to_status_id");
            in_reply_to_user_id = obj.optString("in_reply_to_user_id");
            in_reply_to_screen_name = obj.optString("in_reply_to_screen_name");
            thumbnail_pic = obj.optString("thumbnail_pic");
            bmiddle_pic = obj.optString("bmiddle_pic");
            original_pic = obj.optString("original_pic");
            JSONObject geoObj = obj.optJSONObject("geo");
            if (geoObj != null) {
                geo = new Geo(geoObj.toString());
            }
            
            JSONObject userObj = obj.optJSONObject("user");
            String userInfoStr = null;
            if (userObj != null 
            		&& !TextUtils.isEmpty(userInfoStr = userObj.toString())) {
                user = new UserInfo(userInfoStr);
            }
            
            JSONObject retweeted_statusObj = obj.optJSONObject("retweeted_status");
            String reStatusStr = null;
            if (retweeted_statusObj != null 
            		&& !TextUtils.isEmpty(reStatusStr = retweeted_statusObj.toString())) {
                retweeted_status = new Status(reStatusStr);
            }
            reposts_count = obj.optInt("reposts_count");
            comments_count = obj.optInt("comments_count");
            mlevel = obj.optInt("mlevel");
            distance = obj.optInt("distance");
            JSONObject visibleObj = obj.optJSONObject("visible");
            if (visibleObj != null) {
                visible = new Visible(visibleObj.toString());
            }

        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    /**
     * 去除信息来源的html超链接标签
     * 
     * @return
     */
    public String getFormatSourceDesc() {
        return Util.getFormatSourceDesc(source);
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Status getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Status retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getMlevel() {
        return mlevel;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Visible getVisible() {
        return visible;
    }

    public void setVisible(Visible visible) {
        this.visible = visible;
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
        Status other = (Status) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
