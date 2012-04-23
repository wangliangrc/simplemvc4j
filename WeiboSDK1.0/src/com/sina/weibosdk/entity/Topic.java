package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Topic extends ResponseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9138779814343406504L;

	/**
	 * 话题数目
	 */
	private String num;
    
    /**
     * 话题内容
     */
    private String hotword;
    
    /**
     * 话题ID
     */
    private String trend_id;
    
    
    public Topic() {}

	public Topic(String json) throws WeiboParseException {
        parse(json);
    }
    
	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            String id = obj.optString("trend_id");
            if(!TextUtils.isEmpty(id)) {
            	this.trend_id = id;
            }else {
            	this.trend_id = obj.optString("topicid");
            }
            this.hotword = obj.optString("hotword");
            this.num = obj.optString("num");

		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
	}
	
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getHotword() {
		return hotword;
	}

	public void setHotword(String hotword) {
		this.hotword = hotword;
	}

	public String getTrend_id() {
		return trend_id;
	}

	public void setTrend_id(String trend_id) {
		this.trend_id = trend_id;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((trend_id == null) ? 0 : trend_id.hashCode());
        result = prime * result + ((hotword == null) ? 0 : hotword.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
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
        
        Topic other = (Topic) obj;
        if (trend_id == null) {
            if (other.trend_id != null)
                return false;
        } else if (!trend_id.equals(other.trend_id)) {
            return false;
        }
        
        if (hotword == null) {
            if (other.hotword != null)
                return false;
        } else if (!hotword.equals(other.hotword)) {
            return false;
        }
        
        if (num == null) {
            if (other.num != null)
                return false;
        } else if (!num.equals(other.num)) {
            return false;
        }
        return true;
    }
}
