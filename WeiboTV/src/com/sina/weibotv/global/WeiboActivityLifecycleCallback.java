package com.sina.weibotv.global;

import static com.clark.mvc.MultiCore.sendSignal;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

class WeiboActivityLifecycleCallback implements ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        sendSignal("Activity创建", new Object[] { activity.getClass(),
                savedInstanceState });
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        sendSignal("Activity销毁", activity.getClass());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        sendSignal("Activity暂停", activity.getClass());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        sendSignal("Activity恢复", activity.getClass());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        sendSignal("Activity保存状态",
                new Object[] { activity.getClass(), outState });
    }

    @Override
    public void onActivityStarted(Activity activity) {
        sendSignal("Activity重启", activity.getClass());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        sendSignal("Activity停止", activity.getClass());
    }

}
