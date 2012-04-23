package com.sina.weibosdk.task;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护整个task对象，和一些额外数据
 */
class TaskContainer {

	private Map<Long, ExtentedStruct> mInfos;
	
	private static TaskContainer mSelf;
	
	private TaskContainer() {
		mInfos = new ConcurrentHashMap<Long, TaskContainer.ExtentedStruct>();
	}
	
	public static TaskContainer getInstance() {
		if(mSelf == null) {
			mSelf = new TaskContainer();
		}
		return mSelf;
	}
	
	/**
	 * 添加新的Task
	 * @param task
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean putTask(ATask task) throws IllegalArgumentException {
		long taskKey = task.getTaskKey();
		if(taskKey == 0) {
			throw new IllegalArgumentException(ATask.ERROR_MSG_TASKKEY);
		}
		ExtentedStruct es = mInfos.get(taskKey);
		if(es == null) {
			ExtentedStruct struct = new ExtentedStruct();
			struct.setTask(task);
			mInfos.put(taskKey, struct);
			return true;
		}else {
			throw new IllegalArgumentException(ATask.ERROR_MSG_TASKKEY);
		}
	}
	
	/**
	 * 获取Task
	 * @param key
	 * @return
	 */
	public ATask getTask(long key) {
		ExtentedStruct es = mInfos.get(key);
		if(es == null) {
			return null;
		}
		return es.getTask();
	}
	
	/**
	 * 获得所有ExtentedStruct
	 * @return
	 */
	public List<ExtentedStruct> getAllExtentedStruct() {
		Collection<ExtentedStruct> vs = mInfos.values();
		return new ArrayList<TaskContainer.ExtentedStruct>(vs);
	}
	
	public ExtentedStruct getExtentedStruct(long key) {
		return mInfos.get(key);
	}
	
	public void setUrlConnection(long key, HttpURLConnection conn) {
		mInfos.get(key).setUrlConnection(conn);
	}
	
	public void removeTask(long key) {
		mInfos.remove(key);
	}
	
	/**
	 * 获得URLConnction
	 * @param taskKey
	 * @return
	 */
	public HttpURLConnection getUrlConncetion(long taskKey) {
		ExtentedStruct es = mInfos.get(taskKey);
		if(es == null) {
			return null;
		}
		return es.getUrlConnection();
	}
	
	/**
	 * 一个可扩展的数据结构
	 */
	class ExtentedStruct {
		private ATask mTask;
		private HttpURLConnection mConnection;
		
		public void setTask(ATask task) {
			mTask = task;
		}
		
		public ATask getTask() {
			return mTask;
		}
		
		public void setUrlConnection(HttpURLConnection conn) {
			mConnection = conn;
			conn.disconnect();
		}
		
		public HttpURLConnection getUrlConnection() {
			return mConnection;
		}
		
	}
	
	
}
