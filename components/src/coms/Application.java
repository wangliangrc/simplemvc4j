package coms;

import java.util.HashSet;
import java.util.Set;

import android.content.res.Configuration;
import android.os.Process;

public class Application extends android.app.Application {
    private Set<Activity> mActivities = new HashSet<Activity>();

    /**
     * 进程退出回调
     */
    public void onExit() {
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    void attach(Activity activity) {
        mActivities.add(activity);
    }

    void detach(Activity activity) {
        mActivities.remove(activity);

        if (mActivities.isEmpty()) {
            onExit();
        }
    }

    void exit() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
    }
}
