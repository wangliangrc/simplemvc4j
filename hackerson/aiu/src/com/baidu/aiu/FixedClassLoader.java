package com.baidu.aiu;

import java.io.File;

/**
 * Created with IntelliJ IDEA. User: baidu Date: 3/31/13 Time: 9:30 PM To change
 * this template use File | Settings | File Templates.
 */
interface FixedClassLoader {

	/**
	 * 获取应用程序的包名
	 * 
	 * @return
	 */
	String getPackageName();

	/**
	 * 获取本地库目录路径
	 * 
	 * @return
	 */
	File[] getNativeDirectoryPaths();

	/**
	 * 获取Dex文件路径
	 * 
	 * @return
	 */
	File[] getDexFilePaths();

	/**
	 * 添加自定义Dex文件路径
	 * 
	 * @param optimizedDirectory
	 * @param apks
	 * @return
	 */
	int addDexFilePaths(File optimizedDirectory, File... apks);

	/**
	 * 添加本地库目录路径
	 * 
	 * @param nativeLibraryPaths
	 * @return
	 */
	int addNativeDirectoryPaths(File... nativeLibraryPaths);
}
