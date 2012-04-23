package com.sina.weibosdk.net;

import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.exception.WeiboException;

public interface IDownloadCallback {
	
	public void onStart(CacheStrategy cs);
	public void onProgress(CacheStrategy cs, int percent);
	public void onFinish(CacheStrategy cs);
	public void onError(CacheStrategy cs, WeiboException e);
	
}
