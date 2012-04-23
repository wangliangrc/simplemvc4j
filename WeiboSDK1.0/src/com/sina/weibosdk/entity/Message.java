package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 私信信息
 */
public class Message extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6390322969636761086L;

	/**
	 * 私信ID
	 */
	private String id;
	
	/**
     * 字符串型的评论ID
     */
    private String idstr;
    
    /**
     * 私信创建时间
     */
    private String create_at;
	
    /**
     * 私信内容
     */
    private String text;
    
    /**
     * 发送者ID
     */
    private String sender_id;
    
    private String recipient_id;
    
    private String sender_screen_name;
    
    private String recipient_screen_name;
    
    private String mid;
    
    private String status_id;
    
    /**
     * 附件的ID
     */
    private ArrayList<String> att_ids;
    
    private Geo geo;
    
    private UserInfo sender;
    
    private UserInfo recipient;
    
    
	public Message() {
	}

	public Message(String json) throws WeiboParseException {
		parse(json);
	}
	
	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            this.id = obj.optString("id");
            this.idstr = obj.optString("idstr");
            this.create_at = obj.optString("create_at");
            if (TextUtils.isEmpty(this.create_at)) {
            	this.create_at = obj.optString("created_at");
            }
            this.text = obj.optString("text");
            this.sender_id = obj.optString("sender_id");
            this.recipient_id = obj.optString("recipient_id");
            this.sender_screen_name = obj.optString("sender_screen_name");
            this.recipient_screen_name = obj.optString("recipient_screen_name");
            this.mid = obj.optString("mid");
            String geo = obj.optString("geo");
            if(!TextUtils.isEmpty(geo) && !"null".equals(geo)) {
            	this.geo = new Geo(geo);
            }
            String sender = obj.optString("sender");
            if(!TextUtils.isEmpty(sender)) {
            	this.sender = new UserInfo(sender);
            }
            String recipient = obj.optString("recipient");
            if(!TextUtils.isEmpty(recipient)) {
            	this.recipient = new UserInfo(recipient);
            }
            this.status_id = obj.optString("status_id");
            JSONArray idArray;
            if((idArray = obj.optJSONArray("att_ids")) != null) {
            	this.att_ids = new ArrayList<String>();
            	for(int i = 0; i < idArray.length(); i++) {
            		att_ids.add(idArray.optString(i));
            	}
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

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSender_id() {
		return sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}

	public String getRecipient_id() {
		return recipient_id;
	}

	public void setRecipient_id(String recipient_id) {
		this.recipient_id = recipient_id;
	}

	public String getSender_screen_name() {
		return sender_screen_name;
	}

	public void setSender_screen_name(String sender_screen_name) {
		this.sender_screen_name = sender_screen_name;
	}

	public String getRecipient_screen_name() {
		return recipient_screen_name;
	}

	public void setRecipient_screen_name(String recipient_screen_name) {
		this.recipient_screen_name = recipient_screen_name;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public List<String> getAtt_ids() {
		return att_ids;
	}

	public void setAtt_ids(ArrayList<String> att_ids) {
		this.att_ids = att_ids;
	}

	public String getStatus_id() {
		return status_id;
	}

	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	public UserInfo getSender() {
		return sender;
	}

	public void setSender(UserInfo sender) {
		this.sender = sender;
	}

	public UserInfo getRecipient() {
		return recipient;
	}

	public void setRecipient(UserInfo recipient) {
		this.recipient = recipient;
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
        Message other = (Message) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
