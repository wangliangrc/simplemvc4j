package com.sina.weibotv.global;

import static com.clark.mvc.MultiCore.sendSignal;
import android.app.Application;

import com.sina.weibotv.controller.CommandTable;

public class WeiboTV extends Application {
    private ActivityLifecycleCallbacks lifecycleCallbacks = new WeiboActivityLifecycleCallback();
    private PackageInfoCollector packageInfoCollector;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 Controller
        CommandTable.init();
        // 收集Manifest文件配置信息
        packageInfoCollector = new PackageInfoCollector(this);
        registerActivityLifecycleCallbacks(lifecycleCallbacks);
        sendSignal("应用程序启动", this);
    }

    @Override
    public void onTerminate() {
        sendSignal("应用程序关闭", this);
        unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        super.onTerminate();
    }

    public PackageInfoCollector getPackageInfoCollector() {
        return packageInfoCollector;
    }

    public boolean isInDebugMode() {
        return ApplicationFlags.isDebuggable(packageInfoCollector
                .applicationFlags());
    }

    public boolean isInReleaseMode() {
        return !isInDebugMode();
    }

}
