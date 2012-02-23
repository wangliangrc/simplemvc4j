package com.sina.weibo;

import com.sina.weibo.models.User;

public class StaticInfo {
	public static User mUser;
	public static String mUsername;
//	public static String mPassword;
	public static int[] mTotals;

	public final static String READMODE = "readmode";

	/**
	 * 预览模式
	 */
	public final static int READ_MODE_0 = 0;
	/**
	 * 经典模式
	 */
	public final static int READ_MODE_1 = 1;
	/**
	 * 文字模式
	 */
	public final static int READ_MODE_2 = 2;
	/**
	 * 默认模式
	 */
	public final static int READ_DEF = READ_MODE_0;
}
