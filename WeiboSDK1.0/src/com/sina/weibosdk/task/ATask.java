package com.sina.weibosdk.task;

import java.net.HttpURLConnection;

import android.content.Context;

import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.net.HttpEngine;
import com.sina.weibosdk.net.HttpUrlConnectionEngine;
import com.sina.weibosdk.net.NetRequestProxy;
import com.sina.weibosdk.requestparam.RequestParam;

@SuppressWarnings("rawtypes")
public abstract class ATask implements Runnable {

    public static final String ERROR_MSG_TASKKEY = "the task key is illegal or exist !";

    protected ATaskListener listener;
    protected TaskManager mTaskManager;
    protected Context context;
    protected String SERVER;
    protected HttpEngine mRequestStrategy;
    protected NetRequestProxy mProxy;
    private static HttpEngine mDefaultStrategy;
    
    private String mUrl;
    private RequestParam mParams;
    private long mTaskkey;
    private TaskState mState = TaskState.Wait;
    private String mMethod = "GET";

    private TaskType mType = TaskType.Api;
    
    public ATask(Context context) {
    	if(mDefaultStrategy == null) {
    		mDefaultStrategy = new HttpUrlConnectionEngine(context);
    	}
    	mRequestStrategy = mDefaultStrategy;
    	mProxy = NetRequestProxy.getInstance(context);
        mTaskManager = TaskManager.getInstance();
        SERVER = WeiboSDKConfig.getInstance().getString(WeiboSDKConfig.KEY_API_SERVER);
    }

    public TaskManager getTaskManager() {
        return mTaskManager;
    }

    public long getTaskKey() {
        return mTaskkey;
    }

    public void setTaskKey(long taskkey) {
        this.mTaskkey = taskkey;
    }

    /**
     * 获得当前任务的状态
     * @return
     */
    public TaskState getTaskState() {
        return mState;
    }

    /**
     * 设置当前任务的状态
     * @param state
     */
    public void setTaskState(TaskState state) {
        mState = state;
    }

    /**
     * 获得任务类型
     * @return
     */
    public TaskType getTaskType() {
    	return mType;
    }
    
    /**
     * 设置任务类型
     * @param type
     */
    public void setTaskType(TaskType type) {
    	mType = type;
    }
    
    /**
     * 检查任务是否正在运行
     * @return
     */
    public boolean isRun() {
        if (mState == TaskState.Run) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查任务是否正在等待
     * 
     * @return
     */
    public boolean isWait() {
        if (mState == TaskState.Wait) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查任务是否停止
     * @return
     */
    public boolean isStop() {
        if (mState == TaskState.Stop) {
            return true;
        } else {
            return false;
        }
    }

    public ATaskListener getListener() {
        return listener;
    }

    public void registerListener(ATaskListener mListener) {
        this.listener = mListener;
    }

    /**
     * 执行Get或Post方法。默认执行Get方法
     * @param url
     * @param param
     * @param listener
     * @throws IllegalArgumentException
     */
    public abstract void execute(String url, RequestParam param, ATaskListener listener)
            throws IllegalArgumentException;

    /**
     * 任务取消方法
     */
    public abstract void cancel();

    /**
     * 定义Task状态
     */
    enum TaskState {
        Run, Wait, Stop
    };
    
    /**
     * 定义Task类型
     */
    enum TaskType {
    	Api, Download
    }

    /**
     * 回调接口
     */
    interface CallBack {
        public boolean onCheckTaskCancel(long taskKey, HttpURLConnection conn);
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public RequestParam getParams() {
        return mParams;
    }

    public void setParams(RequestParam mParams) {
        this.mParams = mParams;
    }

    public String getMethod() {
        return mMethod;
    }

    /**
     * 设置http请求方法：Get or Post。默认是Get
     * 
     * @param mMethod
     */
    public void setMethod(String mMethod) {
        this.mMethod = mMethod;
    }

    /**
     * 设置网络请求的策略，如果不设置，用默认的策略
     */
    public void setNetRequestStrategy(HttpEngine strategy) {
    	mRequestStrategy = strategy;
    }
    
    public HttpEngine getNetRequestStrategy() {
    	return mRequestStrategy;
    }

}
