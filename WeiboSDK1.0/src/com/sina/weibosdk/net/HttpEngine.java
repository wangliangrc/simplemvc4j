package com.sina.weibosdk.net;

import java.util.List;

import android.os.Bundle;

import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.entity.FormFile;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.task.WeiboAssert;

public abstract class HttpEngine {

    /**
     * GET请求
     * 
     * @param url
     * @param params
     * @param wassert
     * @return
     * @throws WeiboException
     */
    public abstract String get(String url, Bundle params, WeiboAssert wassert)
            throws WeiboException;

    /**
     * POST请求
     * 
     * @param url
     * @param params
     * @param files
     * @param wassert
     * @return
     * @throws WeiboException
     */
    public abstract String post(String url, Bundle params, List<FormFile> files, WeiboAssert wassert)
            throws WeiboException;

    /**
     * 下载
     * 
     * @param url
     * @param strategy
     * @param callback
     * @param wassert
     * @throws WeiboException
     */
    public abstract void download(String url, CacheStrategy cs, IDownloadCallback callback, 
    		WeiboAssert wassert) throws WeiboException;
    
    	
}
