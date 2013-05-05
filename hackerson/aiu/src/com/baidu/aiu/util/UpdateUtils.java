package com.baidu.aiu.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.baidu.aiu.log.Logger;
import com.baidu.aiu.log.LoggerFactory;

public class UpdateUtils {

	/**
	 * 默认版本号
	 */
	private static final String DEFAULT_VERSION = "1.0.0";

	/**
	 * 默认app名称
	 */
	private static final String DEFAULT_APP = "app0";

	/**
	 * 获取当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentVersion(Context context) {
		// SharedPreferences sharedPref = context.getSharedPreferences(
		// Config.PREFERENCE_AIU_KEY, Context.MODE_PRIVATE);
		// return sharedPref.getString(Config.PREFERENCE_AIU_KEY_VERSION,
		// DEFAULT_VERSION);
		return DEFAULT_VERSION;
	}

	/**
	 * 设置当前版本号
	 * 
	 * @param context
	 * @param version
	 *            版本号
	 */
	public static void setCurrentVersion(Context context, String version) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Config.PREFERENCE_AIU_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(Config.PREFERENCE_AIU_KEY_VERSION, version);
		editor.commit();
	}

	/**
	 * 设置应用名称
	 * 
	 * @param context
	 * @param name
	 */
	public static void setAppName(Context context, String name) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Config.PREFERENCE_AIU_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(Config.PREFERENCE_AIU_KEY_APP, name);
		editor.commit();
	}

	/**
	 * 获取应用名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		Logger logger = LoggerFactory.getLogger();
		SharedPreferences sharedPref = context.getSharedPreferences(
				Config.PREFERENCE_AIU_KEY, Context.MODE_PRIVATE);
		String appName = sharedPref.getString(Config.PREFERENCE_AIU_KEY_APP,
				DEFAULT_APP);

		logger.d("当前APP名称:" + appName);
		return appName;

	}
	
	public static void updateAppName(Context context){
	    Logger logger = LoggerFactory.getLogger();
	    SharedPreferences sharedPref = context.getSharedPreferences(
	                    Config.PREFERENCE_AIU_KEY, Context.MODE_PRIVATE);
	    String appName = sharedPref.getString(Config.PREFERENCE_AIU_KEY_APP,
	                    DEFAULT_APP);
	    
	    try {
            int index = Integer.valueOf(appName.substring(appName.length() - 1,
                    appName.length())) + 1;
            index %= 4;
            setAppName(context, "app" + index);
        } catch (NumberFormatException e) {
            logger.d("App Name 错误");
            Log.d("baidu", "", e);
        }
	}

	private static final String IPPath = Environment
			.getExternalStorageDirectory() + "/ip";

	private static final String DEFAULT_IP = "10.40.71.98";

	public static String getIp() {
		File file = new File(IPPath);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files.length > 0) {
				return files[0].getName();
			}
		}

		return DEFAULT_IP;
	}

}
