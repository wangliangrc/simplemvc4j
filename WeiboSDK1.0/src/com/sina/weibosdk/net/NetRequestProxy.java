package com.sina.weibosdk.net;

import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.entity.FormFile;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.net.NetManager.DownloadState;
import com.sina.weibosdk.task.WeiboAssert;

public final class NetRequestProxy {

	private static NetRequestProxy mSelf;
	
	private Context mContext;
	private NetManager mNetManager;
	
	private NetRequestProxy(Context ctx) {
		mContext = ctx;
		mNetManager = NetManager.getInstance(mContext);
	}
	
	public static synchronized NetRequestProxy getInstance(Context ctx) {
		if(mSelf == null) {
			mSelf = new NetRequestProxy(ctx); 
		}
		return mSelf;
	}
	
	public String get(String url, Bundle params, WeiboAssert wassert, HttpEngine requestStrategy) 
			throws WeiboException {
		try {
			return requestStrategy.get(url, params, wassert);
		} finally {
			WeiboSDKConfig.getInstance().cleanThreadVars();
		}
    }
        
	public String post(String url, Bundle params, List<FormFile> files,
			WeiboAssert wassert, HttpEngine requestStrategy)  throws WeiboException {
		try {
			return requestStrategy.post(url, params, files, wassert);
		} finally {
			WeiboSDKConfig.getInstance().cleanThreadVars();
		}
	}
	
	public void download(String url, CacheStrategy cs, IDownloadCallback callback, 
    		WeiboAssert wassert, HttpEngine requestStrategy) throws WeiboException {
		
		DownloadState ds = mNetManager.get(url, cs);
		/**
		 * 加锁，确保相同的url，只有一个任务在下载中
		 */
		synchronized (ds) {
			if(ds.isDownloading()) {
				return;
			}
			ds.start();
		}
		try {
			requestStrategy.download(url, cs, callback, wassert);
		} finally {
			WeiboSDKConfig.getInstance().cleanThreadVars();
			ds.finish();
		}
	}
	
}
