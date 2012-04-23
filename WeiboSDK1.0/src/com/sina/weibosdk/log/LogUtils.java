package com.sina.weibosdk.log;

import android.text.TextUtils;
import android.util.Log;

import com.sina.weibosdk.WeiboSDKConfig;

public final class LogUtils {

    /**
     * 最低显示的Log等级
     */
    private static final int LOGLEVEL = WeiboSDKConfig.getInstance().getInt(
            WeiboSDKConfig.KEY_LOG_LEVEL);
    /**
     * 控制Log的开关
     */
    private static final boolean ISDEBUG = WeiboSDKConfig.getInstance().getBoolean(
            WeiboSDKConfig.KEY_IS_DEBUG);

    private String tag = "weibosdk";

    public static LogUtils creatLog() {
        return new LogUtils(null);
    }

    public static LogUtils creatLog(String tag) {
        return new LogUtils(tag);
    }

    private LogUtils(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            this.tag = tag;
        }
    }

    public void verbose(String s) {
        if (isLoggable(Log.VERBOSE)) {
            Log.v(tag, s);
        }
    }

    public void debug(String s) {
        if (isLoggable(Log.DEBUG)) {
            Log.d(tag, s);
        }
    }

    public void info(String s) {
        if (isLoggable(Log.INFO)) {
            Log.i(tag, s);
        }
    }

    public void info(String s, Throwable t) {
        if (isLoggable(Log.INFO)) {
            Log.i(tag, s, t);
        }
    }

    public void warning(String s) {
        if (isLoggable(Log.WARN)) {
            Log.w(tag, s);
        }
    }

    public void warning(String s, Throwable t) {
        if (isLoggable(Log.WARN)) {
            Log.w(tag, s, t);
        }
    }

    public void error(String s) {
        if (isLoggable(Log.ERROR)) {
            Log.e(tag, s);
        }
    }

    public void error(String s, Throwable t) {
        if (isLoggable(Log.ERROR)) {
            Log.e(tag, s, t);
        }
    }

    private boolean isLoggable(int level) {
        return level >= LOGLEVEL && ISDEBUG;
    }
}
