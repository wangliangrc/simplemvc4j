package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class MessageList extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2621523328372718030L;

	private ArrayList<Message> messageList = new ArrayList<Message>();
	
	private String previous_cursor;
	
	private String next_cursor;
	
	private int total_number;

	public MessageList() {
	}

	public MessageList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            JSONArray messageArray = obj.optJSONArray("direct_messages");
            int size = messageArray.length();
            for (int i = 0; i < size; i++) {
            	String message = messageArray.optString(i);
            	if(!TextUtils.isEmpty(message)) {
            		messageList.add(new Message(message));
            	}
            }
            total_number = obj.optInt("total_number");
            next_cursor = obj.optString("next_cursor");
            previous_cursor = obj.optString("previous_cursor");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<Message> messageList) {
		this.messageList = messageList;
	}

	public String getPrevious_cursor() {
		return previous_cursor;
	}

	public void setPrevious_cursor(String previous_cursor) {
		this.previous_cursor = previous_cursor;
	}

	public String getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(String next_cursor) {
		this.next_cursor = next_cursor;
	}

	public int getTotal_number() {
		return total_number;
	}

	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        MessageList other = (MessageList) obj;

        if (!(previous_cursor == null ? other.getPrevious_cursor() == null : previous_cursor
                .equals(other.getPrevious_cursor()))) {
            return false;
        }

        if (!(next_cursor == null ? other.getNext_cursor() == null : next_cursor.equals(other
                .getPrevious_cursor()))) {
            return false;
        }

        if (total_number != other.getTotal_number()) {
            return false;
        }

        if (!messageList.equals(other.getMessageList())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + messageList.hashCode();
        result = prime * result + ((previous_cursor == null) ? 0 : previous_cursor.hashCode());
        result = prime * result + ((next_cursor == null) ? 0 : next_cursor.hashCode());
        result = prime * result + total_number;
        return result;
    }

}
