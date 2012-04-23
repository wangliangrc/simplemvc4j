package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Remind extends ResponseBean {

    private static final long serialVersionUID = 7786680522733469633L;
    private int status;
    private int follower;
    private int cmt;
    private int dm;
    private int mention_status;
    private int mention_cmt;
    private int group;
    private int private_group;
    private int notice;
    private int invite;
    private int badge;
    private int photo;

    public Remind() {}
    
    public Remind(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected Remind parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            this.status = obj.optInt("status");
            this.follower = obj.optInt("follower");
            this.cmt = obj.optInt("cmt");
            this.dm = obj.optInt("dm");
            this.mention_status = obj.optInt("mention_status");
            this.mention_cmt = obj.optInt("mention_cmt");
            this.group = obj.optInt("group");
            this.private_group = obj.optInt("private_group");
            this.notice = obj.optInt("notice");
            this.invite = obj.optInt("invite");
            this.badge = obj.optInt("badge");
            this.photo = obj.optInt("photo");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFollower() {
        return follower;
    }

    public void setFtatus(int follower) {
        this.follower = follower;
    }

    public int getCmt() {
        return cmt;
    }

    public void setCmt(int cmt) {
        this.cmt = cmt;
    }

    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }

    public int getMentionStatus() {
        return mention_status;
    }

    public void setMentionStatus(int mention_status) {
        this.mention_status = mention_status;
    }

    public int getMentionCmt() {
        return mention_cmt;
    }

    public void setMentionCmt(int mention_cmt) {
        this.mention_cmt = mention_cmt;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPrivateGroup() {
        return private_group;
    }

    public void setPrivateGroup(int private_group) {
        this.private_group = private_group;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public int getInvite() {
        return invite;
    }

    public void setInvite(int invite) {
        this.invite = invite;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

}
