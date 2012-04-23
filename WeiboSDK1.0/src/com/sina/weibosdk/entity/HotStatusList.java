package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 热门转发列表
 * 
 * @author zhangqi
 * 
 */
public class HotStatusList extends ResponseBean {

    private static final long serialVersionUID = -4404063971077360467L;

    private List<Status> statusList = new ArrayList<Status>();

    public HotStatusList() {}
    
    public HotStatusList(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected HotStatusList parse(String json) throws WeiboParseException {
        try {
            JSONArray array = new JSONArray(json);
            int size = array.length();
            for (int i = 0; i < size; i++) {
            	String statusStr = array.getString(i);
            	if (!TextUtils.isEmpty(statusStr)) {
            		statusList.add(new Status(statusStr));
            	}
            }
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        HotStatusList other = (HotStatusList) obj;

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

        return result;
    }

}
