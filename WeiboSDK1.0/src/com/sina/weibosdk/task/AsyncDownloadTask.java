package com.sina.weibosdk.task;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.net.IDownloadCallback;
import com.sina.weibosdk.requestparam.RequestParam;

@SuppressWarnings({ "rawtypes" })
public class AsyncDownloadTask extends AsyncRequestTask {

	private ATask mApiTask;
	private Context mContext;
	private IDownloadCallback mDownloadCallback;
	private CacheStrategy mCacheStrategy;
	
	public AsyncDownloadTask(Context context) {
		super(context);
		mContext = context;
		setTaskType(TaskType.Download);
	}
	
	public AsyncDownloadTask(Context context, long taskKey) {
		super(context, taskKey);
		mContext = context;
		setTaskType(TaskType.Download);
	}

	@Override
	public void run() {
		try {
			
			if(isStop()) {
				return;
			}
			setTaskState(TaskState.Run);
			mProxy.download(getUrl(), mCacheStrategy, mDownloadCallback, 
					mAssert, mRequestStrategy);
		}catch(Exception e) {
			Util.loge(e.getMessage(), e);
		}finally {
			setTaskState(TaskState.Stop);
            mTaskManager.removeTask(getTaskKey());
		}
		
	}

	@Override
	public void execute(String url, RequestParam param, ATaskListener listener)
			throws IllegalArgumentException {
		mApiTask = new AsyncRequestTask(mContext);
		mApiTask.setNetRequestStrategy(getNetRequestStrategy());
		mApiTask.execute(url, param, listener);
	}
	
	public void download(String url, IDownloadCallback callback) 
			throws IllegalArgumentException {
		if(TextUtils.isEmpty(url)) {
			throw new IllegalArgumentException("url is empty !");
		}
		setUrl(url);
		mDownloadCallback = callback;
		TaskManager.getInstance().addTask(this);
	}

	@Override
	public void cancel() {
		if(mApiTask != null) {
			mApiTask.cancel();
		}
		super.cancel();
	}
	
	public void setCacheStrategy(CacheStrategy strategy) {
		this.mCacheStrategy = strategy;
	}
	
	public CacheStrategy getCacheStrategy() {
		return this.mCacheStrategy;
	}
	
	
	
}
