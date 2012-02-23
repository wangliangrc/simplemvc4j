package com.sina.weibotv.controller;

import static com.clark.mvc.MultiCore.sendSignal;
import static com.clark.mvc.MultiCore.setUIWorker;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import com.clark.mvc.Command;
import com.clark.mvc.Signal;
import com.clark.mvc.UIWorker;
import com.sina.weibotv.view.activity.PageIndex;

public class GlobalCommand {
    static final String TAG = GlobalCommand.class.getSimpleName();

    @Command("应用程序启动")
    static void applicationOnCreate(Signal signal) {
        Log.d(TAG, "TV微博启动");
        // 保证 MVC 的消息派发线程是 UI 线程
        final Handler handler = new Handler();
        setUIWorker(new UIWorker() {
            @Override
            public void postTask(Runnable task) {
                handler.post(task);
            }
        });
    }

    @Command("应用程序关闭")
    static void applicationOnTerminate(Signal signal) {
        Log.d(TAG, "TV微博关闭");
        // 退出整个程序
        // 关闭进程，保证光标模式还原
        System.exit(0);
        Process.killProcess(Process.myPid());
    }

    @Command("Activity创建")
    static void activityOnCreate(Signal signal) {
        Object[] objects = (Object[]) signal.body;
        Log.d(TAG, ((Class<?>) objects[0]).getCanonicalName()
                + " 创建一个实例");
    }

    @Command("Activity销毁")
    static void activityOnDestroy(Signal signal) {
        Class<?> clazz = (Class<?>) signal.body;
        Log.d(TAG, clazz.getCanonicalName() + " 销毁一个实例");
        // 如果 MainActivity 退出就算做程序退出
        if (clazz == PageIndex.class) {
            sendSignal("应用程序关闭");
        }
    }
}
