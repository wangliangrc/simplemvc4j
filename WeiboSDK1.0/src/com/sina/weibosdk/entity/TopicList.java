package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class TopicList extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2180666090762844823L;

	private List<Topic> topicList = new ArrayList<Topic>();
	
	public TopicList() {
		
	}

	public TopicList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
			JSONArray jarray = new JSONArray(json);
			for(int i = 0; i < jarray.length(); i++) {
				String topic = jarray.getString(i);
				if(!TextUtils.isEmpty(topic)) {
					Topic temp = new Topic(topic); 
					topicList.add(temp);
				}
			}
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
	}

	public List<Topic> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<Topic> topicList) {
		this.topicList = topicList;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        TopicList other = (TopicList) obj;
        if (!topicList.equals(other.getTopicList())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + topicList.hashCode();
        return result;
    }


}
