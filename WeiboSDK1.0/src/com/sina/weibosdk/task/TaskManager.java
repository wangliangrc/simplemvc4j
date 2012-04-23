package com.sina.weibosdk.task;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.task.ATask.CallBack;
import com.sina.weibosdk.task.ATask.TaskState;
import com.sina.weibosdk.task.ATask.TaskType;
import com.sina.weibosdk.task.TaskContainer.ExtentedStruct;

public class TaskManager implements CallBack {

    private static TaskManager mSelf = null;

    private static final int KEEP_ALIVE_TIME = 10;
    private ThreadPoolExecutor mApiPool;
    private LinkedBlockingQueue<Runnable> mApiTaskWaitQueue;
    private ThreadPoolExecutor mDownloadPool;
    private LinkedBlockingQueue<Runnable> mDownloadTaskWaitQueue;
    
    private TaskContainer mTaskContainer;

    private TaskManager(int apiPoolCoreSize, int apiPoolMaxSize, 
    		int downloadPoolCoreSize, int downloadPoolMaxSize) {
    	
        mApiTaskWaitQueue = new LinkedBlockingQueue<Runnable>();
        mApiPool = new ThreadPoolExecutor(apiPoolCoreSize, apiPoolMaxSize, KEEP_ALIVE_TIME, 
        		TimeUnit.SECONDS, mApiTaskWaitQueue,
        		mApiThreadFactory);
        
        mDownloadTaskWaitQueue = new LinkedBlockingQueue<Runnable>();
        mDownloadPool = new ThreadPoolExecutor(downloadPoolCoreSize, downloadPoolMaxSize, KEEP_ALIVE_TIME, 
        		TimeUnit.SECONDS, mDownloadTaskWaitQueue,
        		mDownloadThreadFactory);
        
        mTaskContainer = TaskContainer.getInstance();
    }

    public synchronized static TaskManager getInstance() {
    	
        if (mSelf == null) {
        	
            int apiPoolMaxSize = WeiboSDKConfig.getInstance().getInt(
            		WeiboSDKConfig.KEY_API_THREAD_POOL_SIZE);
            int apiPoolCoreSize = (apiPoolMaxSize / 20) + 1;
            
            int downloadPoolMaxSize = WeiboSDKConfig.getInstance().getInt(
            		WeiboSDKConfig.KEY_DOWNLOAD_THREAD_POOL_SIZE);
            int downloadPoolCoreSize = (downloadPoolMaxSize / 20) + 1;
            
            mSelf = new TaskManager(apiPoolCoreSize, apiPoolMaxSize, downloadPoolCoreSize, 
            		downloadPoolMaxSize);
        }
        return mSelf;
    }

    /**
     * 根据taskKey查询出一个Task
     * 
     * @param taskKey
     * @return
     */
    public ATask getTask(long taskKey) {
        return mTaskContainer.getTask(taskKey);
    }

    /**
     * 添加任务
     * 
     * @param task
     * @throws IllegalArgumentException
     */
    public void addTask(ATask task) throws IllegalArgumentException {
        if (mTaskContainer.putTask(task)) {
        	if(task.getTaskType() == TaskType.Api) {
        		mApiPool.execute(task);
        	}else if(task.getTaskType() == TaskType.Download) {
        		mDownloadPool.execute(task);
        	}
        }
    }

    public int getRunningTaskCount() {
        return mApiPool.getActiveCount() 
        		+ mDownloadPool.getActiveCount();
    }

    public int getWaitingTaskCount() {
        return mApiTaskWaitQueue.size() 
        		+ mDownloadTaskWaitQueue.size();
    }

    /**
     * 打印当前线程池场景
     * 
     * @return
     */
    public void print() {
    	
    	List<ExtentedStruct> l = TaskContainer.getInstance().getAllExtentedStruct();
    	
    	String debug = "";
    	debug += "ApiPool Size : " + mApiPool.getMaximumPoolSize();
    	debug += "\r\n";
    	debug += "ApiPool Core Size : " + mApiPool.getCorePoolSize();
    	debug += "\r\n";
    	debug += "Running Task Count : " + mApiPool.getActiveCount();
    	debug += "\r\n";
    	debug += "Waitting Task Count : " + mApiPool.getQueue().size();
    	debug += "\r\n";
    	for(ExtentedStruct es : l) {
    		ATask t = es.getTask();
    		if(t.getTaskType() == TaskType.Api) {
    			debug += "Task : ";
    			debug += t.getUrl();
    			if(t.isRun()) {
    				debug += "  Running...  ";
    			}else if(t.isWait()) {
    				debug += "  Waitting...  ";
    			}else if(t.isStop()) {
    				debug += "  Stoped  ";
    			}
    			debug += "\r\n";
    		}
    	}
    	
        debug += "\r\n";
    	debug += "DownloadPool Size : " + mDownloadPool.getMaximumPoolSize();
    	debug += "\r\n";
    	debug += "DownloadPool Core Size : " + mDownloadPool.getCorePoolSize();
    	debug += "\r\n";
    	debug += "Running Task Count : " + mDownloadPool.getActiveCount();
    	debug += "\r\n";
    	debug += "Waitting Task Count : " + mDownloadPool.getQueue().size();
    	debug += "\r\n";
    	for(ExtentedStruct es : l) {
    		ATask t = es.getTask();
    		if(t.getTaskType() == TaskType.Download) {
    			debug += "Task : ";
    			debug += t.getUrl();
    			if(t.isRun()) {
    				debug += "  Running...  ";
    			}else if(t.isWait()) {
    				debug += "  Waitting...  ";
    			}else if(t.isStop()) {
    				debug += "  Stoped  ";
    			}
    			debug += "\r\n";
    		}
    	}
    	debug += "\r\n";
    	
    	Util.logd(debug);
    }

    /**
     * 从TaskContainer中删除一个Task
     * 
     * @param taskKey
     */
    void removeTask(long taskKey) {
        mTaskContainer.removeTask(taskKey);
    }

    /**
     * 终止一个异步任务
     * 
     * @param aTask
     */
    void cancel(ATask task) {
        // 置空 listener ，保证回调listener不会触发
        task.registerListener(null);

        /**
         * 如果当前任务已经停止，直接返回
         */
        if (task.isStop()) {
            return;
        }
        HttpURLConnection conn;
        try {
            conn = mTaskContainer.getUrlConncetion(task.getTaskKey());
            if (conn != null) {
                conn.disconnect();
            }
        } finally {
            task.setTaskState(TaskState.Stop);
        }
    }

    /**
     * 预留以后扩展
     */
    private ThreadFactory mApiThreadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            return t;
        }
    };
    
    /**
     * 预留以后扩展
     */
    private ThreadFactory mDownloadThreadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            return t;
        }
    };

    @Override
    public boolean onCheckTaskCancel(long key, HttpURLConnection conn) {
        ExtentedStruct es = mTaskContainer.getExtentedStruct(key);
        ATask task = es.getTask();
        es.setUrlConnection(conn);
        if (task.isStop()) {
            HttpURLConnection temp = es.getUrlConnection();
            if (temp != null) {
                temp.disconnect();
            }
            return true;
        }
        return false;
    }

}
