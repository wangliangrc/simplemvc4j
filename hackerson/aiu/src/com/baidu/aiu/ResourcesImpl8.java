
package com.baidu.aiu;

import java.lang.reflect.Field;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

public class ResourcesImpl8 implements FixedResources {
    public static final ResourcesImpl8 INSTANCE = new ResourcesImpl8();

    @SuppressWarnings("rawtypes")
    @Override
    public void setResource(Application application, String path) {
        try {
            Class cwrapperClass = null;
            cwrapperClass = Class.forName("android.content.ContextWrapper");
            Field ss = cwrapperClass.getDeclaredField("mBase");
            ss.setAccessible(true);
            Object contextImpls = ss.get((ContextWrapper) application);

            Class implClass = null;
            implClass = Class.forName("android.app.ContextImpl");
            Field s = implClass.getDeclaredField("mMainThread");
            s.setAccessible(true);
            // Object thead = s.get(contextImpls);

            Field la = implClass.getDeclaredField("mPackageInfo");
            la.setAccessible(true);
            Object loadedApk = la.get(contextImpls);

            Class apkClass = null;
            apkClass = Class.forName("android.app.ActivityThread$PackageInfo");
            Field a = apkClass.getDeclaredField("mResDir");
            a.setAccessible(true);
            a.set(loadedApk, path);

            Field re1 = apkClass.getDeclaredField("mResources");
            re1.setAccessible(true);
            re1.set(loadedApk, null);
        } catch (ClassNotFoundException e) {
            Log.d("baidu", "ClassNotFoundException " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            Log.d("baidu", "NoSuchFieldException " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Log.d("baidu", "IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.d("baidu", "IllegalAccessException " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public static void showResource(Context context) {
        try {
            Class cwrapperClass = null;
            cwrapperClass = Class.forName("android.content.ContextWrapper");
            Field ss = cwrapperClass.getDeclaredField("mBase");
            ss.setAccessible(true);
            Object contextImpls = ss.get((ContextWrapper) context);

            Class implClass = null;
            implClass = Class.forName("android.app.ContextImpl");
            Field s = implClass.getDeclaredField("mMainThread");
            s.setAccessible(true);
            // Object thead = s.get(contextImpls);

            Field la = implClass.getDeclaredField("mPackageInfo");
            la.setAccessible(true);
            Object loadedApk = la.get(contextImpls);

            Class apkClass = null;
            apkClass = Class.forName("android.app.ActivityThread$PackageInfo");
            Field a = apkClass.getDeclaredField("mResDir");
            a.setAccessible(true);
            Object resDir = a.get(loadedApk);
            Log.d("baidu", "mResDir: " + resDir.toString());
            Field re1 = apkClass.getDeclaredField("mResources");
            re1.setAccessible(true);
            Object resource = a.get(loadedApk);
            Log.d("baidu", "mResources: " + resource.toString());
        } catch (ClassNotFoundException e) {
            Log.d("baidu", "ClassNotFoundException " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            Log.d("baidu", "NoSuchFieldException " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Log.d("baidu", "IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.d("baidu", "IllegalAccessException " + e.getMessage());
            e.printStackTrace();
        }
    }
}
