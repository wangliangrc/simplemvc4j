package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 
 * @author zhangqi
 * 
 */
public class User extends ResponseBean {

    private static final long serialVersionUID = 290724255022055575L;

    /**
     * 用户gsid
     */
    private String gsid;

    /**
     * 1为已经完善资料，0为未完善资料
     */
    private int status;

    /**
     * 用户UID
     */
    private String uid;

    /**
     * 用户昵称
     */
    private String screen_name;

    /**
     * 用户微博客URL
     */
    private String url;

    /**
     * 用户收件箱URL
     */
    private String msg_url;

    /**
     * oauth_token(只在用户名密码方式登录时返回)
     */
    private String oauth_token;

    /**
     * oauth_token_secret(只在用户名密码方式登录时返回)
     */
    private String oauth_token_secret;

    /**
     * user结构体。请求参数trim_user为0时才返回
     */
    private UserInfo userInfo;

    public User() {}
    
    public User(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected User parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            this.gsid = obj.optString("gsid");
            this.status = obj.optInt("status");
            this.uid = obj.optString("uid");
            this.screen_name = obj.optString("screen_name");
            this.url = obj.optString("url");
            this.msg_url = obj.optString("msg_url");
            this.oauth_token = obj.optString("oauth_token");
            this.oauth_token_secret = obj.optString("oauth_token_secret");
            String userInfoStr = obj.optString("user");
            if (!TextUtils.isEmpty(userInfoStr)) {
                this.userInfo = new UserInfo(userInfoStr);
            }
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }

        return this;
    }

    public String getGsid() {
        return gsid;
    }

    public void setGsid(String gsid) {
        this.gsid = gsid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg_url() {
        return msg_url;
    }

    public void setMsg_url(String msg_url) {
        this.msg_url = msg_url;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }

    public String getOauth_token_secret() {
        return oauth_token_secret;
    }

    public void setOauth_token_secret(String oauth_token_secret) {
        this.oauth_token_secret = oauth_token_secret;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        User other = (User) obj;

        if (!(gsid == null ? other.getGsid() == null : gsid.equals(other.getGsid()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (gsid == null ? 0 : gsid.hashCode());
        return result;
    }

}
