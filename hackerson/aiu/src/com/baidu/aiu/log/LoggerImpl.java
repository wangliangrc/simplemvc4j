package com.baidu.aiu.log;

import android.text.TextUtils;

import com.baidu.aiu.AIU;
import com.baidu.aiu.util.FloatWindowManager;

class LoggerImpl implements Logger {
	LoggerImpl() {
	}

	@Override
	public void d(final CharSequence text) {
		if (!TextUtils.isEmpty(text)) {
			AIU.getUIHandler().post(new Runnable() {

				@Override
				public void run() {
					FloatWindowManager.getInstance().show();
					FloatWindowManager.getInstance().setText(text.toString());
				}
			});
		}
	}

}
