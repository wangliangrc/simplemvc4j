package com.sina.weibo.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sina.weibo.WeiboApplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class NetEngineFactory {
    private static final int LOAD_FROM_JAVA = 0;
    private static final int LOAD_FROM_NATIVE = 1;

    private static INetEngine mNetEngine = null;

    /**
     * Get instance of NetEngine
     * 
     * @return
     */
    public static INetEngine getNetInstance() {
        return getNetInstance(WeiboApplication.instance);
    }

    /**
     * Get instance of NetEngine
     * 
     * @param context
     * @return
     */
    public static INetEngine getNetInstance(Context context) {
        if (mNetEngine == null) {
            synchronized (NetEngineFactory.class) {
                if (mNetEngine == null) {
                    int loadType = Utils.isLoadNetEngineFromNative(context) ? LOAD_FROM_NATIVE
                            : LOAD_FROM_JAVA;
                    mNetEngine = getNetInstance(
                            context.getApplicationContext(), loadType);
                }
            }
        }
        return mNetEngine;
    }

    /**
     * Get instance of NetEngine
     * 
     * @param context
     * @param loadType
     *            the type of loading class
     * @return
     */
    private static INetEngine getNetInstance(Context context, int loadType) {
        INetEngine engine = null;
        switch (loadType) {
            case LOAD_FROM_JAVA:
                engine = getNetEngineFromJava(context);
                break;

            case LOAD_FROM_NATIVE:
                engine = getNetEngineFromNative(context);
                break;

            default:
                break;
        }

        return engine;
    }

    /**
     * Get instance of class *NetEngine*
     * 
     * @param context
     * @return
     */
    private static INetEngine getNetEngineFromJava(Context context) {
        try {
            Class<?> netEngCls = context.getClassLoader().loadClass(
                    "com.sina.weibo.net.NetEngine");
            Method getNetInstanceMethod = netEngCls.getMethod("getNetInstance",
                    Context.class);
            return (INetEngine) getNetInstanceMethod.invoke(null, context);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get instance of NetEngine from native code
     * 
     * @param context
     * @return
     */
    private static INetEngine getNetEngineFromNative(Context context) {
        return WeiboApplication.instance.getNetInstance(context,
                getVersionCode(context));
    }

    private static String getVersionCode(Context context) {
        try {
            String pName = Constants.PACKAGE_NAME;
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo(pName,
                    PackageManager.GET_CONFIGURATIONS);
            return String.valueOf(pinfo.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
