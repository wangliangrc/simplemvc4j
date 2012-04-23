package com.sina.weibosdk.requestparam;

import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibosdk.exception.WeiboApiException;

/**
 * 微博接口下行接口请求参数封装
 * 
 * @author zhangqi
 * 
 */
public class StatusReqDownParam extends RequestParam {

    /**
     * 用户分组id。注意，不是gsid
     */
    private String gid;

    /**
     * 页码。注意：最多返回200条分页内容。默认值1。
     */
    private int page;

    /**
     * 指定每页返回的记录条数。默认值50，最大值200。
     */
    private int count;

    /**
     * 若指定此参数，则只返回ID比since_id大的微博消息
     */
    private String since_id;

    /**
     * 若指定此参数，则返回ID小于或等于max_id的微博消息
     */
    private String max_id;

    /**
     * user_timeline接口. 微博类型，0全部，1原创，2图片，3视频，4音乐,5商品. 返回指定类型的微博信息内容。
     */
    private int filter;

    /**
     * user_timeline接口 .用户id值，默认为当前登录用户
     */
    private String uid;

    /**
     * statuses/mentions. 过滤类型ID （0：所有用户、1：关注的人）默认为0。
     */
    private int filter_by_author;

    /**
     * statuses/mentions. 过滤类型ID （0：所有微博、1：原创的微博）默认为0
     */
    private int filter_by_type;

    /**
     * 过滤类型ID （0：全部、1：原创、2：图片、3：视频、4：音乐、5：商品） 默认为 0
     */
    private int feature;
    
	@Override
    public Bundle getParams() throws WeiboApiException {
        if (!TextUtils.isEmpty(gid)) {
            bundle.putString("gid", gid);
        }

        if (page > 0) {
            bundle.putString("page", String.valueOf(page));
        }

        if (count > 0) {
            bundle.putString("count", String.valueOf(count));
        }

        if (!TextUtils.isEmpty(since_id)) {
            bundle.putString("since_id", since_id);
        }

        if (!TextUtils.isEmpty(max_id)) {
            bundle.putString("max_id", max_id);
        }

        if (!TextUtils.isEmpty(uid)) {
            bundle.putString("uid", uid);
        }

        if (filter > 0) {
            bundle.putString("filter", String.valueOf(filter));
        }

        if (filter_by_author > 0) {
            bundle.putString("filter_by_author", String.valueOf(filter_by_author));
        }

        if (filter_by_type > 0) {
            bundle.putString("filter_by_type", String.valueOf(filter_by_type));
        }

        if (feature > 0) {
        	bundle.putString("feature", String.valueOf(feature));
        }
        return bundle;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSince_id() {
        return since_id;
    }

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getFilter_by_author() {
        return filter_by_author;
    }

    public void setFilter_by_author(int filter_by_author) {
        this.filter_by_author = filter_by_author;
    }

    public int getFilter_by_type() {
        return filter_by_type;
    }

    public void setFilter_by_type(int filter_by_type) {
        this.filter_by_type = filter_by_type;
    }
    
    public int getFeature() {
		return feature;
	}

	public void setFeature(int feature) {
		this.feature = feature;
	}

}
