package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 微博评论和转发数 list
 * 
 * @author zhangqi
 * 
 */
public class StatusCRNumList extends ResponseBean {

    private static final long serialVersionUID = 3183866396909105551L;

    private List<StatusCRNum> statusCRNums = new ArrayList<StatusCRNumList.StatusCRNum>();

    public StatusCRNumList() {}
    
    public StatusCRNumList(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected StatusCRNumList parse(String json) throws WeiboParseException {
        try {
            JSONArray array = new JSONArray(json);
            int size = array.length();
            for (int i = 0; i < size; i++) {
                statusCRNums.add(new StatusCRNum(array.getString(i)));
            }
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

    public List<StatusCRNum> getStatusCRNums() {
        return statusCRNums;
    }

    public void setStatusCRNums(List<StatusCRNum> statusCRNums) {
        this.statusCRNums = statusCRNums;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        StatusCRNumList other = (StatusCRNumList) obj;

        if (!statusCRNums.equals(other.getStatusCRNums())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + statusCRNums.hashCode();
        return result;
    }

    public class StatusCRNum extends ResponseBean {

        private static final long serialVersionUID = -1502912322793603373L;

        private String id;

        private int comments;

        private int reposts;

        public StatusCRNum(String json) throws WeiboParseException {
            parse(json);
        }

        @Override
        protected StatusCRNum parse(String json) throws WeiboParseException {
            try {
                JSONObject obj = new JSONObject(json);
                id = obj.optString("id");
                comments = obj.optInt("comments");
                reposts = obj.optInt("reposts");
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

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getReposts() {
            return reposts;
        }

        public void setReposts(int reposts) {
            this.reposts = reposts;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            StatusCRNum other = (StatusCRNum) obj;

            if (!(id == null ? other.getId() == null : id.equals(other.getId()))) {
                return false;
            }

            if (comments != other.getComments() || reposts != other.getReposts()) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            result = prime * result + comments;
            result = prime * result + reposts;

            return result;
        }

    }

}
