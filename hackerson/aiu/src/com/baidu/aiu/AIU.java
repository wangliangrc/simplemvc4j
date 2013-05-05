package com.baidu.aiu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.baidu.aiu.log.LoggerFactory;
import com.baidu.aiu.util.IOUtils;

/**
 * Created with IntelliJ IDEA.
 * 
 * @author guangongbo
 * @version 1.0 13-3-28
 */
public class AIU extends Application {

    // static {
    // try {
    // init();
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    // }

    /**
     * 系统反射调用本构造函数
     */
    public AIU() {
        sContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // UI handler
        sHandler = new Handler(Looper.getMainLooper());
        // monitor
        // Thread.setDefaultUncaughtExceptionHandler(new AIUExceptionhandler());
        // 替换资源
        final FixedResources resources = FixedResourcesFactory
                        .getFixedResources();
        resources.setResource(this, sLoadedApk.getAbsolutePath());
        // log启动
        LoggerFactory.getLogger().d("应用初始化完成");
    }

    /**
     * 返回内部加载的 apk 文件的路径
     * 
     * @return
     */
    public static File getLoadedApkFile() {
        return sLoadedApk;
    }

    /**
     * 返回 patch 合成的 apk 文件路径
     * 
     * @return
     */
    public static File getCachedApkFile() {
        return sCacheApk;
    }

    /**
     * 在无法获取 Context 的前提下提供获取途径
     * 
     * @return
     */
    public static Context getsApplicationContext() {
        return sContext;
    }

    /**
     * 返回全局 UI handler
     * 
     * @return
     */
    public static Handler getUIHandler() {
        return sHandler;
    }

    // public static void start() {
    // try {
    // init();
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    // }

    private static final String AIU_NAME = "ss.aiu";
    private static final String AIU_SRC = "a.zip";
    private static final String AIU_CACHE = "b.zip";
    private static Context sContext;
    private static Handler sHandler;
    private static FixedClassLoader sFixedClassLoader;
    private static File sApkDir;
    private static File sOptimizeDir;
    private static File sLoadedApk;
    private static File sCacheApk;

    private static void init() throws Exception {
        initValues();

        if (sCacheApk.exists()) {
            Log.d("baidu", "发现增量apk，开始更新");
            cutCacheApk();
        } else if (!sLoadedApk.exists() && !sCacheApk.exists()) {
            Log.d("baidu", "没有发现增量apk，开始拷贝内部预装版本");
            copyAssertApk();
        }

        Log.d("baidu", "开始加载动态apk");
        final int res = sFixedClassLoader.addDexFilePaths(sOptimizeDir,
                        sLoadedApk);
        if (res == 0) {
            Log.d("baidu", "apk 加载成功!");
        } else {
            Log.d("baidu", "apk 加载失败...");
        }
    }

    /**
     * 将 patch 生成的 apk 替换原有的 apk
     */
    private static void cutCacheApk() {
        sLoadedApk.delete();
        sCacheApk.renameTo(sLoadedApk);
    }

    /**
     * 将 assets 中的 ss.aiu 拷贝到指定内部加载位置
     * 
     * @throws ZipException
     * @throws IOException
     */
    private static void copyAssertApk() throws ZipException, IOException {
        final ZipFile apkFile = new ZipFile(
                        sFixedClassLoader.getDexFilePaths()[0]);
        final Enumeration<? extends ZipEntry> entries = apkFile.entries();
        ZipEntry entry = null;
        String name = null;
        final File outFile = sLoadedApk;
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            name = entry.getName();
            if (name.endsWith(AIU_NAME)) {
                Log.d("baidu", "正在拷贝 " + name + "...");
                IOUtils.copy(apkFile.getInputStream(entry),
                                new FileOutputStream(outFile));
                Log.d("baidu", name + " 拷贝完毕!");
                break;
            }
        }
        apkFile.close();
    }

    /**
     * 初始化static 变量
     */
    private static void initValues() {
        final String currentPackageName = getCurrentPackageName();
        sFixedClassLoader = FixedClassLoaderFactory.getFixedClassLoader();
        sOptimizeDir = new File("/data/data/" + currentPackageName + "/caches");
        sApkDir = new File("/data/data/" + currentPackageName + "/aius");
        if (!sOptimizeDir.isDirectory()) {
            sOptimizeDir.mkdirs();
        }
        if (!sApkDir.isDirectory()) {
            sApkDir.mkdirs();
        }
        sLoadedApk = new File(sApkDir, AIU_SRC);
        sCacheApk = new File(sApkDir, AIU_CACHE);
    }

    private static String getCurrentPackageName() {
        if (sContext == null) {
            return sFixedClassLoader.getPackageName();
        } else {
            return sContext.getPackageName();
        }
    }
}
