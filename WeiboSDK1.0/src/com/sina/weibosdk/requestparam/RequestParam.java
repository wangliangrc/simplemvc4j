package com.sina.weibosdk.requestparam;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.entity.FormFile;
import com.sina.weibosdk.exception.WeiboApiException;
import com.sina.weibosdk.exception.WeiboIOException;

/**
 * 请求参数抽象类
 * 
 * @author zhangqi
 * 
 */
public abstract class RequestParam {

    /**
     * from值
     */
    protected String from;

    /**
     * WM值
     */
    protected String wm;

    /**
     * c值，必选参数
     */
    protected String c;

    /**
     * s值，必选参数
     */
    protected String s;

    /**
     * User agent信息
     */
    protected String ua;

    /**
     * 语言
     */
    protected String lang;

    /**
     * 用户gsid。凡是需要登录的接口，都需要传递gsid值
     */
    protected String gsid;

    protected Bundle bundle;

    protected List<FormFile> files = new ArrayList<FormFile>(1);

    public RequestParam() {
        WeiboSDKConfig config = WeiboSDKConfig.getInstance();
        from = config.getString(WeiboSDKConfig.KEY_FROM);
        wm = config.getString(WeiboSDKConfig.KEY_WM);
        c = config.getString(WeiboSDKConfig.KEY_C);
        s = config.getString(WeiboSDKConfig.KEY_S);
        ua = config.getString(WeiboSDKConfig.KEY_UA);
        lang = config.getString(WeiboSDKConfig.KEY_LANG);
        gsid = config.getString(WeiboSDKConfig.KEY_GSID);
        bundle = new Bundle();
        if (!TextUtils.isEmpty(from)) {
            bundle.putString("from", from);
        }
        if (!TextUtils.isEmpty(wm)) {
            bundle.putString("wm", wm);
        }
        bundle.putString("c", c);
        bundle.putString("s", s);
        bundle.putString("ua", ua);
        bundle.putString("lang", lang);

        if (!TextUtils.isEmpty(gsid)) {
            bundle.putString("gsid", gsid);
        }
    }

    public abstract Bundle getParams() throws WeiboApiException;

    public void addParam(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            bundle.putString(key, value);
        }
    }

    public String getParam(String key) {
        return bundle.getString(key);
    }

    public void addFile(String picPath) throws WeiboIOException {
    	if (!TextUtils.isEmpty(picPath)) {
    		addFile(picPath, "pic");
    	}
    }

    public void addFile(String picPath, String formName) throws WeiboIOException {
    	if (!TextUtils.isEmpty(picPath) 
    			&& !TextUtils.isEmpty(formName)) {
    		files.add(new FormFile(picPath, formName, Util.getImageContentType(picPath)));
    	}
    }

    public List<FormFile> getFiles() {
        return files;
    }

    public void setFiles(List<FormFile> files) {
        this.files = files;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWm() {
        return wm;
    }

    public void setWm(String wm) {
        this.wm = wm;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getGsid() {
        return gsid;
    }

    public void setGsid(String gsid) {
        this.gsid = gsid;
    }

}
