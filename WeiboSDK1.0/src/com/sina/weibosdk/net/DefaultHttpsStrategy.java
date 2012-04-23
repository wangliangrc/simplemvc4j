package com.sina.weibosdk.net;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.entity.FormFile;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.exception.WeiboIOException;
import com.sina.weibosdk.task.WeiboAssert;

/**
 * Http, Https切换策略
 * 
 * 1、支持https：
 *    逻辑：如果在wifi下，并且请求的链接是https开头的，则启用https支持，
 *    否则，修改请求链接为http，使用普通http方式完成网络请求
 * 
 * 2、在wifi下，如果链接是https且请求失败，则替换为http，
 *    替换为https再次请求。如果请求成功将状态设置为1
 *    如果失败将不修改状态值。
 *    在同样情况下，再次请求，在将https替换为http之后如果成功
 *    将状态设置为2.
 *    如果在wifi下，链接是https第一次请求失败，在替换为http之后成功，
 *    在第二次请求设置链接https直接成功，将状态值设置为0
 *    在每一次启动程序之后，在weiboapplication中将相应的状态设置为0
 *    
 */
public class DefaultHttpsStrategy extends HttpUrlConnectionEngine {

	private Context mContext;
	
	public DefaultHttpsStrategy(Context context) {
		super(context);
		this.mContext = context;
	}
	
	@Override
	public String get(String url, Bundle params, WeiboAssert wassert) throws WeiboException {
		return execute("get", url, params, null, wassert);
    }
        
	@Override
	public String post(String url, Bundle params, List<FormFile> files,
			WeiboAssert wassert)  throws WeiboException {
		return execute("post", url, params, files, wassert);
	}

	@Override
	public void download(String url, CacheStrategy cs,
			IDownloadCallback callback, WeiboAssert wassert)
			throws WeiboException {

		NetworkInfo netInfo = Util.getNetwrokInfo(mContext);
        if(null == netInfo || !netInfo.isAvailable()) {
        	throw new WeiboIOException("NoSignalException");
        }
		
		/**
         * 如果当前不在wifi下，
         * 或者当前是http请求，
         * 则走下面正常流程
         */
        if(ConnectivityManager.TYPE_WIFI != Util.getNetWorkType(netInfo) 
        		|| url.startsWith("http://")) {
        	url = url.replace("https://", "http://");
			super.download(url, cs, callback, wassert);
        }
        
        /**
         * 如果当前在wifi下，并且是https请求
         * 则走特殊的逻辑
         */
        else {
        	int flag = getFlag();
    		try {
    			if(flag >= 2){
    				url = url.replace("https://", "http://");
    			}
    			super.download(url, cs, callback, wassert);
    			if(flag == 1){
    				setFlag(0);
    			}
    		} catch (WeiboIOException e) {
    			Util.loge(e.getMessage(), e);
    			url = url.replace("https://", "http://");
    			super.download(url, cs, callback, wassert);
    			flag = flag + 1;
    			setFlag(flag);
    		}
    		
        }
	}

	private String execute(String method, String url, Bundle params, List<FormFile> files,
			WeiboAssert wassert) throws WeiboException {
		
		String result;
		int flag = getFlag();
		
		NetworkInfo netInfo = Util.getNetwrokInfo(mContext);
        if(null == netInfo || !netInfo.isAvailable()) {
        	throw new WeiboIOException("NoSignalException");
        }
		
        /**
         * 如果当前不在wifi下，
         * 或者当前是http请求，
         * 则走下面正常流程
         */
        if(ConnectivityManager.TYPE_WIFI != Util.getNetWorkType(netInfo) 
        		|| url.startsWith("http://")) {
        	url = url.replace("https://", "http://");
        	if("post".equalsIgnoreCase(method)) {
				result = super.post(url, params, files, wassert);
			}else {
				result = super.get(url, params, wassert);
			}
        	return result;
        }
        
        /**
         * 如果当前在wifi下，并且是https请求
         * 则走特殊的逻辑
         */
		try {
			if(flag >= WeiboSDKConfig.HTTPS_RETRIED){
				url = url.replace("https://", "http://");
			}
			if("post".equalsIgnoreCase(method)) {
				result = super.post(url, params, files, wassert);
			}else {
				result = super.get(url, params, wassert);
			}
			if(flag < WeiboSDKConfig.HTTPS_RETRIED){
				setFlag(0);
			}
		} catch (WeiboIOException e) {
			Util.loge(e.getMessage(), e);
			url = url.replace("https://", "http://");
			if("post".equalsIgnoreCase(method)) {
				result = super.post(url, params, files, wassert);
			}else {
				result = super.get(url, params, wassert);
			}
			flag = flag + 1;
			setFlag(flag);
		}
		
		return result;
	}
	
	private int getFlag() {
		return WeiboSDKConfig.getInstance().getInt(
					WeiboSDKConfig.KEY_HTTPSLINK_FLAG);
	}
	
	private void setFlag(int flag) {
		WeiboSDKConfig.getInstance().setProperty(
				WeiboSDKConfig.KEY_HTTPSLINK_FLAG, flag);
	}
	
	
	
}
