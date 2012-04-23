package com.sina.weibosdk.requestparam;

import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibosdk.exception.WeiboApiException;

/**
 * 微博接口上行请求参数
 * 
 * @author zhangqi
 * 
 */
public class StatusReqUpParam extends RequestParam {

    /**
     * 要发布的微博消息文本内容，必须做URLEncode,信息内容不超过140个汉字。
     */
    private String status;

    /**
     * 微博的可见性，0：所有人能看，1：仅自己可见，默认为0。
     */
    private int visible;

    /**
     * 纬度。有效范围：-90.0到+90.0，+表示北纬。默认为0.0
     */
    private double lat;

    /**
     * 经度。有效范围：-180.0到+180.0，+表示东经。默认为0.0
     */
    private double lon;

    /**
     * 元数据
     */
    private String annotations;

    /**
     * 返回结果是否转义。0：不转义，1：转义。默认0.
     */
    private int is_encoded;

    /**
     * 要转发的微博ID
     */
    private String id;

    /**
     * 是否在转发的同时发表评论。0表示不发表评论，1表示发表评论给当前微博，2表示发表评论给原微博，3是1、2都发表。默认为0。
     */
    private int is_comment;

    @Override
    public Bundle getParams() throws WeiboApiException {

        if (!TextUtils.isEmpty(status)) {
            bundle.putString("status", status);
        }

        if (visible == 0 || visible == 1) {
            bundle.putString("visible", String.valueOf(visible));
        }

        if (Math.abs(lat - 0.) > 0.0001) {
            bundle.putString("lat", String.valueOf(lat));
        }

        if (Math.abs(lon - 0.) > 0.0001) {
            bundle.putString("long", String.valueOf(lon));
        }

        if (!TextUtils.isEmpty(annotations)) {
            bundle.putString("annotations", annotations);
        }

        if (is_encoded == 0 || is_encoded == 1) {
            bundle.putString("is_encoded", String.valueOf(is_encoded));
        }

        if (!TextUtils.isEmpty(id)) {
            bundle.putString("id", id);
        }

        if (is_comment > 0) {
            bundle.putString("is_comment", String.valueOf(is_comment));
        }

        return bundle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public int getIsEncoded() {
        return is_encoded;
    }

    public void setIsEncoded(int is_encoded) {
        this.is_encoded = is_encoded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsComment() {
        return is_comment;
    }

    public void setIsComment(int is_comment) {
        this.is_comment = is_comment;
    }

}
