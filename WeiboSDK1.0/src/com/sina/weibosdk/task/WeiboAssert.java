package com.sina.weibosdk.task;

import java.net.HttpURLConnection;
import com.sina.weibosdk.exception.WeiboInterruptException;
import com.sina.weibosdk.task.ATask.CallBack;

/**
 * 对逻辑流程进行
 */
public class WeiboAssert {

	private long taskKey;
	private CallBack mCallBack;
	
	public WeiboAssert(long taskKey) {
		this.taskKey = taskKey; 
	}
	
	/**
	 * Assert是否继续执行
	 * @return
	 */
	public boolean assertContinueRunning(HttpURLConnection conn) 
			throws WeiboInterruptException {
		if(mCallBack != null) {
			if(mCallBack.onCheckTaskCancel(taskKey, conn)) {
				throw new WeiboInterruptException();
			}else {
				return true;
			}
		}
		return true;
	}
	
	public void setCallBack(CallBack callBack) {
		mCallBack = callBack;
	}
}
