package com.baidu.aiu.util;

import java.io.File;

import android.os.Environment;

public class FileUtils {
	public static String sdcardPath() {
		return Environment.getExternalStorageDirectory() + "";
	}

	/**
	 * @param path
	 *            相对sdcard路径
	 */
	public static void mkdir(String path) {
		File file = new File(sdcardPath() + "/" + path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static String getFileNameFromUrl(String url) {
		String[] ps = url.split("/");

		if (ps != null) {
			return ps[ps.length - 1];
		}

		return null;
	}
}
