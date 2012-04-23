package com.sina.weibosdk.net;

import java.util.HashMap;
import java.util.Map;

import com.sina.weibosdk.cache.CacheStrategy;

import android.content.Context;

/**
 * 用来管理请求的URL，防止用一个连接多次的请求
 */
@SuppressWarnings("unused")
class NetManager {

	private Map<String, DownloadState> mDownloadMap = new HashMap<String, DownloadState>(); 
	private Object mDownloadLock;
	
	private static NetManager mSelf;
	
	private Context mContext;
	
	private NetManager(Context ctx) {
		mContext = ctx;
		mDownloadLock = new Object();
	}
	
	public static synchronized NetManager getInstance(Context ctx) {
		if(mSelf == null) {
			mSelf = new NetManager(ctx);
		}
		return mSelf;
	}
	
	public DownloadState get(String url, CacheStrategy cs) {
		synchronized (mDownloadLock) {
			String key = key(url, cs);
			DownloadState f = mDownloadMap.get(key);
			if(f == null) {
				f = new DownloadState();
				mDownloadMap.put(key, f);
			}
			return f;
		}
	}
	
	private String key(String url, CacheStrategy cs) {
		return url;
	}
	
	class DownloadState {
		private boolean mIsDownloading;
		
		public boolean isDownloading() {
			return mIsDownloading;
		}
		
		public void start() {
			mIsDownloading = true;
		}
		
		public void finish() {
			mIsDownloading = false;
		}
	}
}
