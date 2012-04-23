package com.sina.weibosdk.task;

import java.util.Random;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.exception.WeiboInterruptException;
import com.sina.weibosdk.requestparam.RequestParam;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AsyncRequestTask extends ATask {

    protected WeiboAssert mAssert;
    private static final String SERVER = WeiboSDKConfig.getInstance().getString(
            WeiboSDKConfig.KEY_API_SERVER);

    public AsyncRequestTask(Context context, long taskKey) {
    	super(context);
    	init(taskKey);
    }

    /**
     * 自动生成task key
     * @param context
     */
    public AsyncRequestTask(Context context) {
    	super(context);
        long taskKey = genTaskKey();
        init(taskKey);
    }

    private void init(long taskKey) {
    	setTaskKey(taskKey);
        mAssert = new WeiboAssert(taskKey);
        mAssert.setCallBack(mTaskManager);
    }
    
    private long genTaskKey() {
        long key = System.nanoTime() + new Random().nextInt();
        return key;
    }

    @Override
    public void execute(String url, RequestParam param, ATaskListener listener)
            throws IllegalArgumentException {
    	if(TextUtils.isEmpty(url)) {
    		throw new IllegalArgumentException("url is empty !");
    	}
    	if(param == null) {
    		throw new IllegalArgumentException("param is null !");
    	}
        setUrl(url);
        setParams(param);
        registerListener(listener);
        mTaskManager.addTask(this);
    }

    @Override
    public void run() {
        try {
            if (isStop()) {
                return;
            }
            setTaskState(TaskState.Run);
            String resp = "";
            if ("POST".equalsIgnoreCase(getMethod())) {
                resp = mProxy.post(getFullUrl(getUrl()), getParams().getBundle(), 
                		getParams().getFiles(), mAssert, getNetRequestStrategy());
            } else {
                resp = mProxy.get(getFullUrl(getUrl()), getParams().getBundle(), mAssert, 
                		getNetRequestStrategy());
            }

            if (listener != null && !TextUtils.isEmpty(resp)) {
                listener.onComplete(this, listener.parse(resp));
            }
        } catch (WeiboException e) {
        	if(e instanceof WeiboInterruptException) {
        		return;
        	}
            if (null != listener) {
                listener.onWeiboException(this, e);
            }
            Util.loge(e.getMessage(), e);
        } catch (Exception e) {
        	Util.loge(e.getMessage(), e);
        } finally {
            setTaskState(TaskState.Stop);
            mTaskManager.removeTask(getTaskKey());
        }
    }

    /**
     * 取消掉当前的Task(只有异步任务具有这个方法)
     */
    @Override
    public void cancel() {
        ATask task = mTaskManager.getTask(getTaskKey());
        if (task != null) {
            mTaskManager.cancel(task);
        }
    }

    private String getFullUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        } else if (url.startsWith("/")) {
            return SERVER + url.substring(1);
        } else {
            return SERVER + url;
        }
    }
}
