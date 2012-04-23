package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 评论信息
 */
public class Comment extends ResponseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6492733407723260294L;

	/**
	 * 评论ID
	 */
	private String id;
    
    /**
     * 评论内容
     */
    private String text;
    
    private String source;
    
    /**
     * 评论创建时间
     */
    private String create_at;
    
    /**
     * 评论MID
     */
    private String mid;
    
    /**
     * 字符串型的评论ID
     */
    private String idstr;
    
    /**
     * 发表评论的用户信息
     */
    private UserInfo user;
    
    /**
     * 被评论微博
     */
    private Status status;
    
    /**
     * 当评论是一个回复评论
     */
    private Comment reply_comment;

    
    public Comment() {}

	public Comment(String json) throws WeiboParseException {
		parse(json);
	}
    
	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            this.id = obj.optString("id");
            this.text = obj.optString("text");
            this.source = obj.optString("source");
            this.create_at = obj.optString("created_at");
            String user = obj.optString("user");
            if(!TextUtils.isEmpty(user)) {
            	this.user = new UserInfo(user);
            }
            this.mid = obj.optString("mid");
            this.idstr = obj.optString("idstr");
            String status = obj.optString("status");
            if(!TextUtils.isEmpty(status)) {
            	this.status = new Status(status);
            }
            String reply_comment = obj.optString("reply_comment");
            if(!TextUtils.isEmpty(reply_comment)) {
            	this.reply_comment = new Comment(reply_comment);
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
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

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Comment getReply_comment() {
		return reply_comment;
	}

	public void setReply_comment(Comment reply_comment) {
		this.reply_comment = reply_comment;
	}	
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Comment other = (Comment) obj;
        if (!id.equals(other.getId())) {
        	return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
    	final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
	
	
}
