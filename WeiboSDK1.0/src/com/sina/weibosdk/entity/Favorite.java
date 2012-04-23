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
 * 收藏实体类
 * 
 * @author zhangqi
 * 
 */
public class Favorite extends ResponseBean {

    private static final long serialVersionUID = -2839384537615832838L;

    private Status status;

    private List<Tag> tag = new ArrayList<Tag>();

    private String favorited_time;

    public Favorite() {}
    
    public Favorite(String json) throws WeiboParseException {
        parse(json);
    }
    
    @Override
    protected Favorite parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            
            String statusStr = null;
            if (obj != null 
            		&& !TextUtils.isEmpty(statusStr = obj.getString("status"))) {
            	status = new Status(statusStr);
            }
            
            JSONArray tagArray = obj.optJSONArray("tag");
            if (null != tagArray) {
                int size = tagArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject tagObj = tagArray.getJSONObject(i);
                    Tag tag = new Tag();
                    tag.setId(tagObj.optString("id"));
                    tag.setTag(tagObj.optString("tag"));
                    tag.setCount(tagObj.optInt("count"));
                    this.tag.add(tag);
                }
            }

            favorited_time = obj.optString("favorited_time");

        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }

        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    public String getFavorited_time() {
        return favorited_time;
    }

    public void setFavorited_time(String favorited_time) {
        this.favorited_time = favorited_time;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Favorite other = (Favorite) obj;

        if (!status.equals(other.getStatus())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + status.hashCode();

        return result;
    }

    public class Tag {
        private String id;
        private String tag;
        private int count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }

}