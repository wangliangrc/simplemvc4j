package com.clark.tools;

import android.util.Log;

/**
 * 日志的相关工具方法。
 * 
 * @author guangongbo
 *
 */
public class LG {
    protected static boolean sDebug = false;

    public static void open() {
        sDebug = true;
    }

    public static void close() {
        sDebug = false;
    }

    private String mTag;
    private static final String DEFAULT_TAG = "default";

    public LG(Object tag) {
        if (tag instanceof CharSequence) {
            mTag = tag.toString();
        } else if (tag instanceof Class) {
            mTag = ((Class<?>) tag).getSimpleName();
        } else if (tag instanceof Object) {
            mTag = tag.getClass().getSimpleName();
        } else {
            mTag = DEFAULT_TAG;
        }
    }

    public void v(CharSequence msg) {
        if (sDebug) {
            Log.v(mTag, msg == null ? "" : msg.toString());
        }
    }

    public void v(CharSequence msg, Throwable tr) {
        if (sDebug) {
            if (tr == null) {
                v(msg);
            } else {
                Log.v(mTag, msg == null ? "" : msg.toString(), tr);
            }
        }
    }

    public void d(CharSequence msg) {
        if (sDebug) {
            Log.d(mTag, msg == null ? "" : msg.toString());
        }
    }

    public void d(CharSequence msg, Throwable tr) {
        if (sDebug) {
            if (tr == null) {
                d(msg);
            } else {
                Log.d(mTag, msg == null ? "" : msg.toString(), tr);
            }
        }
    }

    public void e(CharSequence msg) {
        if (sDebug) {
            Log.e(mTag, msg == null ? "" : msg.toString());
        }
    }

    public void e(CharSequence msg, Throwable tr) {
        if (sDebug) {
            if (tr == null) {
                e(msg);
            } else {
                Log.e(mTag, msg == null ? "" : msg.toString(), tr);
            }
        }
    }

    public void w(CharSequence msg) {
        if (sDebug) {
            Log.w(mTag, msg == null ? "" : msg.toString());
        }
    }

    public void w(CharSequence msg, Throwable tr) {
        if (sDebug) {
            if (tr == null) {
                w(msg);
            } else {
                Log.w(mTag, msg == null ? "" : msg.toString(), tr);
            }
        }
    }

    public void i(CharSequence msg) {
        if (sDebug) {
            Log.i(mTag, msg == null ? "" : msg.toString());
        }
    }

    public void i(CharSequence msg, Throwable tr) {
        if (sDebug) {
            if (tr == null) {
                i(msg);
            } else {
                Log.i(mTag, msg == null ? "" : msg.toString(), tr);
            }
        }
    }

}
