package com.sina.weibosdk.requestparam;

import android.os.Bundle;

import com.sina.weibosdk.exception.WeiboApiException;

/**
 * 
 * @author zhangqi
 * 
 */
public class HotStatusReqParam extends RequestParam {

    /**
     * 是否按照当前应用信息对搜索结果进行过滤。当值为1时，仅返回通过该应用发送的微博消息。当值为0时，不过滤。
     */
    private int app_base;

    /**
     * 返回结果条数数量，默认20，最大50
     */
    private int count;

    /**
     * 返回结果是否转义。0：不转义，1：转义。
     */
    private int is_encoded;

    /**
     * 带图片微博返回的bmiddle_pic字段图片的大小，可选值 240,320,960
     */
    private int picsize;

    @Override
    public Bundle getParams() throws WeiboApiException {
        if (app_base == 0 || app_base == 1) {
            bundle.putString("app_base", String.valueOf(app_base));
        }

        if (count > 0) {
            bundle.putString("count", String.valueOf(count));
        }

        if (is_encoded == 0 || is_encoded == 1) {
            bundle.putString("is_encoded", String.valueOf(is_encoded));
        }

        if (picsize == 240 || picsize == 320 || picsize == 960) {
            bundle.putString("picsize", String.valueOf(picsize));
        }
        return bundle;
    }

    public int getAppBase() {
        return app_base;
    }

    public void setAppBase(int app_base) {
        this.app_base = app_base;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsEncoded() {
        return is_encoded;
    }

    public void setIsEncoded(int is_encoded) {
        this.is_encoded = is_encoded;
    }

    public int getPicsize() {
        return picsize;
    }

    public void setPicsize(int picsize) {
        this.picsize = picsize;
    }

}
