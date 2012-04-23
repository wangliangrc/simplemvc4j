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
 * 微博结构体list
 * 
 * @author zhangqi
 * 
 */
public class StatusList extends ResponseBean {

    private static final long serialVersionUID = -8232610983036258954L;

    private ArrayList<Status> statusList = new ArrayList<Status>(20);

    private boolean hasvisible;

    private String previous_cursor;

    private String next_cursor;

    private int total_number;

    public StatusList() {
	}

	public StatusList(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected StatusList parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray stausArray = obj.optJSONArray("statuses");
            if (null != stausArray) {
                int size = stausArray.length();
                for (int i = 0; i < size; i++) {
                	String statusStr = stausArray.getString(i);
                	if (!TextUtils.isEmpty(statusStr)) {
                		statusList.add(new Status(statusStr));
                	}
                }
            }
            hasvisible = obj.optBoolean("hasvisible");
            previous_cursor = obj.optString("previous_cursor");
            next_cursor = obj.optString("next_cursor");
            total_number = obj.optInt("total_number");

        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(ArrayList<Status> statusList) {
        this.statusList = statusList;
    }

    public boolean isHasvisible() {
        return hasvisible;
    }

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
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

        StatusList other = (StatusList) obj;

        if (hasvisible != other.isHasvisible()) {
            return false;
        }

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

        if (!statusList.equals(other.getStatusList())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + statusList.hashCode();
        result = prime * result + (hasvisible ? 0 : 1);
        result = prime * result + ((previous_cursor == null) ? 0 : previous_cursor.hashCode());
        result = prime * result + ((next_cursor == null) ? 0 : next_cursor.hashCode());
        result = prime * result + total_number;

        return result;
    }

}
