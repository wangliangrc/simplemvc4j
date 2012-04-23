package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class MessageUserList extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 921613296609134166L;

	private List<UserMessage> userMessageList = new ArrayList<UserMessage>();
	
	private int total_number;
	
	public MessageUserList() {}
	
	public MessageUserList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            JSONArray userMessageArray = obj.optJSONArray("user_list");
            int size = userMessageArray.length();
            for (int i = 0; i < size; i++) {
            	userMessageList.add(new UserMessage(
            			userMessageArray.optString(i)));
            }
            total_number = obj.optInt("total_number");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
		return this;
	}

	public List<UserMessage> getUserMessageList() {
		return userMessageList;
	}

	public void setUserMessageList(List<UserMessage> userMessageList) {
		this.userMessageList = userMessageList;
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

        MessageUserList other = (MessageUserList) obj;

        if (total_number != other.getTotal_number()) {
            return false;
        }
        if (!userMessageList.equals(other.getUserMessageList())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + userMessageList.hashCode();
        result = prime * result + total_number;

        return result;
    }
	

	public class UserMessage extends ResponseBean {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1414381661959384153L;
		private UserInfo user;
		private Message message;
		private int unreadNum;
		
		public UserMessage() {}
		
		public UserMessage(String json) throws WeiboParseException {
			parse(json);
		}

		@Override
		protected ResponseBean parse(String json) throws WeiboParseException {
			try {
				JSONObject obj = new JSONObject(json);
				String user = obj.optString("user");
				String message = obj.optString("direct_message");
				if(!TextUtils.isEmpty(user)) {
					this.user = new UserInfo(user);
				}
				if(!TextUtils.isEmpty(message)) {
					this.message = new Message(message);
				}
	            this.unreadNum = obj.optInt("unread_count");
	        } catch (JSONException e) {
	        	Util.loge(e.getMessage(), e);
	            throw new WeiboParseException(e);
	        }
			return this;
		}
		
		public UserInfo getUser() {
			return user;
		}

		public void setUser(UserInfo user) {
			this.user = user;
		}

		public Message getMessage() {
			return message;
		}
		public void setMessage(Message message) {
			this.message = message;
		}
		public int getUnreadNum() {
			return unreadNum;
		}
		public void setUnreadNum(int unreadNum) {
			this.unreadNum = unreadNum;
		}
		
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((user == null) ? 0 : user.hashCode());
	        result = prime * result + ((message == null) ? 0 : message.hashCode());
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
	        
	        UserMessage other = (UserMessage) obj;
	        if (user == null) {
	            if (other.user != null)
	                return false;
	        } else {
	            if(!user.equals(other.user))
	            	return false;
	        }
	        
	        if(message == null) {
	        	if(other.message != null)
	        		return false;
	        } else {
	        	if(!message.equals(other.message))
	        		return false;
	        }
	        
	        if(unreadNum != other.unreadNum) {
	        	return false;
	        }
	        return true;
	    }
		
	}
	
}
