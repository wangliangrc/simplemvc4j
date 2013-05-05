package com.baidu.aiu.monitor;

import android.os.Process;
import android.util.Log;

public class AIUExceptionhandler implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		final String message = ex.getMessage();
		Log.d("baidu", "", ex);
		new Thread() {
			@Override
			public void run() {
				Log.d("baidu", "uncaughtException ");
				Monitor.pushException(message);
				Log.d("baidu", "uncaughtException " + message);
				Process.killProcess(Process.myPid());
			}
		}.start();
	}

}
