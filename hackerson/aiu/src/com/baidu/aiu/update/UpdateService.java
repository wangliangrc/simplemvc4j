package com.baidu.aiu.update;

import java.io.File;
import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.anzhi.market.util.BSDiff;
import com.baidu.aiu.AIU;
import com.baidu.aiu.api.UpdateApi;
import com.baidu.aiu.api.model.UpdateVersionModel;
import com.baidu.aiu.log.Logger;
import com.baidu.aiu.log.LoggerFactory;
import com.baidu.aiu.util.FloatWindowManager;
import com.baidu.aiu.util.HttpUtils;
import com.baidu.aiu.util.IOUtils;
import com.baidu.aiu.util.UpdateUtils;

public class UpdateService extends IntentService {

	private static final String TAG = UpdateService.class.getSimpleName();

	private static final String PATCH_NAME = "/patch.aiu";

	private String mCachePath;
	private String mSourcePath;

	public UpdateService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "UpdateService start");
		final Logger logger = LoggerFactory.getLogger();
		logger.d("增量更新引擎-开始升级");

		mCachePath = AIU.getCachedApkFile().getAbsolutePath();
		mSourcePath = AIU.getLoadedApkFile().getAbsolutePath();
		if (mSourcePath == null || mCachePath == null) {
			Log.d(TAG, "intent data is null , kill self");
			stopSelf();
			return;
		}
		logger.d("开始检测升级");
		UpdateVersionModel updateResult = UpdateApi.update(
				UpdateUtils.getCurrentVersion(this.getApplicationContext()),
				UpdateUtils.getAppName(this.getApplicationContext()));

		if (updateResult != null && updateResult.isNeedUpdate()) {
			Log.d(TAG, "need update");
			logger.d("检测需要升级");
			Log.d(TAG, "Delegate.KEY_AIU_CACHE : " + mCachePath);
			updateAIU(updateResult.getVersion(), updateResult.getUrlPatch());
		} else {
			Log.d(TAG, "not need update");
			logger.d("检测无更新内容");
			delete(mCachePath);
		}

		logger.d("增量更新引擎-结束升级");
		FloatWindowManager.getInstance().disableButton();
		Log.d(TAG, "UpdateService end");
		stopSelf();
	}

	private void updateAIU(String version, String downloadUrl) {
		Log.d(TAG, "downloadUrl : " + downloadUrl);
		boolean downloadResult = HttpUtils.download(downloadUrl,
				getFatherPath(mCachePath), PATCH_NAME);
		final Logger logger = LoggerFactory.getLogger();
		logger.d("开始下载AIU包：" + downloadUrl);
		try {
			logger.d("AIU包 MD5：" + IOUtils.getMD5(IOUtils.readAll(getFatherPath(mCachePath) + PATCH_NAME)));
		} catch (IOException e) {
			Log.d(TAG, "获取MD5失败");
			e.printStackTrace();
		}
		
		if (downloadResult) {
			Log.d(TAG, "download success");
			logger.d("AIU包下载成功");
			try {
				logger.d("开始增量更新...");
				BSDiff.doBspatch(mSourcePath, mCachePath,
						getFatherPath(mCachePath) + PATCH_NAME);
				UpdateUtils.setCurrentVersion(getApplicationContext(), version);
				Log.e(TAG, "doBspatch success");
				logger.d("增量更新结束");
				UpdateUtils.updateAppName(getApplicationContext());
			} catch (Exception exception) {
				Log.e(TAG, "doBspatch error " + exception.getMessage());
				logger.d("增量更新失败");
				exception.printStackTrace();
				delete(mCachePath);
			}
			delete(getFatherPath(mCachePath) + PATCH_NAME);
		} else {
			Log.e(TAG, "download aiu failed");
		}
	}

	private String getFatherPath(String path) {
		return (new File(path)).getParent();
	}

	private void delete(String fileName) {
		File file = new File(fileName);
		if (file.exists())
			file.delete();
	}

}
